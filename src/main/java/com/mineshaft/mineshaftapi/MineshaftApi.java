/*
 * Copyright (c) 2025. Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.mineshaft.mineshaftapi;

import com.mineshaft.mineshaftapi.command.*;
import com.mineshaft.mineshaftapi.dependency.DependencyInit;
import com.mineshaft.mineshaftapi.dependency.KeyListener;
import com.mineshaft.mineshaftapi.dependency.VaultDependency;
import com.mineshaft.mineshaftapi.dependency.mythic_mob.MythicListener;
import com.mineshaft.mineshaftapi.dependency.world_guard.RegionManager;
import com.mineshaft.mineshaftapi.listener.*;
import com.mineshaft.mineshaftapi.manager.config.ConfigManager;
import com.mineshaft.mineshaftapi.manager.event.EventManager;
import com.mineshaft.mineshaftapi.manager.event.PendingAbilities;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.crafting.GlobalRecipeCache;
import com.mineshaft.mineshaftapi.manager.item.crafting.RecipeRegistrar;
import com.mineshaft.mineshaftapi.manager.player.AbilityType;
import com.mineshaft.mineshaftapi.manager.player.PlayerManager;
import com.mineshaft.mineshaftapi.manager.player.combat.ActionManager;
import com.mineshaft.mineshaftapi.manager.player.combat.CooldownManager;
import com.mineshaft.mineshaftapi.util.Language;
import com.mineshaft.mineshaftapi.util.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public final class MineshaftApi extends JavaPlugin {

    @Getter
    PacketListener packetListener;

    @Getter
    static Random random = new Random();

    @Getter
    static RecipeRegistrar recipeRegistrar;

    @Getter
    static GlobalRecipeCache globalRecipeCache;

    @Getter
    HashMap<String,AbilityType> abilities = new HashMap<>();

    // Managers, some including caches
    ConfigManager configManager = new ConfigManager(this);
    CooldownManager cooldownManager = new CooldownManager();
    ItemManager itemManager;
    EventManager eventManager;
    DependencyInit dependencyInit = new DependencyInit();
    ActionManager actionManager = new ActionManager();

    // WorldGuard region manager and cache
    RegionManager regionManager;

    // Caches
    HashMap<UUID, PendingAbilities> pendingAbilities = new HashMap<>();

    @Override
    public void onEnable() {
        // Ascii text generator: https://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20

        Logger.logInfo("   __  ____              __        _____    ___   ___  ____");
        Logger.logInfo("  /  |/  (_)__  ___ ___ / /  ___ _/ _/ /_  / _ | / _ \\/  _/");
        Logger.logInfo(" / /|_/ / / _ \\/ -_|_-</ _ \\/ _ `/ _/ __/ / __ |/ ___// / ");
        Logger.logInfo("/_/  /_/_/_//_/\\__/___/_//_/\\_,_/_/ \\__/ /_/ |_/_/  /___/");
        Logger.logInfo("                                                           ");
        Logger.logInfo("Loaded version 1.1.1");

        // Coming soon. Language detection
        switch (getDebugLanguage()) {
            case POLISH:
                Logger.logInfo("Wtyczka mineshaft API została włączona");
            case ENGLISH:
                Logger.logInfo("Plugin mineshaft API has been enabled");
        }

        /*
         * Dependency checks. load dependency specific managers and listeners
         * */
        if (DependencyInit.hasMythicMobs()) {
            // Register placeholders
            Bukkit.getPluginManager().registerEvents(new MythicListener(), MineshaftApi.getInstance());
        } else {
            // Log warning
            Logger.logWarning("MythicMobs is not installed. Integration has not been enabled");
        }
        // Load AriKeys
        if(DependencyInit.hasAriKeys()) {
            Logger.logDebug("AriKeys event has been enabled");
            Bukkit.getPluginManager().registerEvents(new KeyListener(), this);
        }

        // Load WorldGuard
        if(DependencyInit.hasWorldGuard()) {
            regionManager = new RegionManager();
        }
        /*
        * End of dependency loading.
        * */

        // Register listeners
        packetListener=new PacketListener();
        Bukkit.getPluginManager().registerEvents(packetListener, this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new EquipListener(), this);
        Bukkit.getPluginManager().registerEvents(new PacketListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldListener(), this);
        Bukkit.getPluginManager().registerEvents(new CraftListener(), this);

        // Initialise commands
        // Null warnings can be ignored, as these are declared in the plugin.yml and will never return null.
        getCommand("mineshaft").setExecutor(new MineshaftCommand());
        getCommand("mineshaft").setTabCompleter(new MineshaftTabCompleter());
        getCommand("player_data").setExecutor(new PlayerDataCommand());
        getCommand("player_data").setTabCompleter(new PlayerDataTabCompleter());
        getCommand("balance").setExecutor(new MonetaryBalanceCommand());
        getCommand("getitem").setExecutor(new GetItemCommand());
        getCommand("getitem").setTabCompleter(new GetItemTabCompleter());
        getCommand("heal").setExecutor(new HealCommand());

        // Initialise registrar
        recipeRegistrar = new RecipeRegistrar("mineshaft");

        // Initialise custom items
        itemManager=new ItemManager();
        itemManager.initialiseItems();

        // Load events
        eventManager=new EventManager();
        eventManager.initialiseEvents();

        // Initialise plugin dependencies
        dependencyInit.initialiseDependencies();

    }

    @Override
    public void onDisable() {
        switch (getDebugLanguage()) {
            case POLISH:
                Logger.logInfo("Wtyczka mineshaft API została wyłączona");
            case ENGLISH:
                Logger.logInfo("Plugin mineshaft API has been disabled");
        }
        PlayerManager.KickPlayers();

        // Deregister recipes
        getRecipeRegistrar().clearRecipes();
        globalRecipeCache.clearLockedRecipes();
    }

    public static Language getLanguage(Player player) {
        return Language.ENGLISH;
    }

    public static Language getDebugLanguage() {return Language.ENGLISH;}

    public static MineshaftApi getInstance() {
        return MineshaftApi.getPlugin(MineshaftApi.class);
    }

    private final String pluginFolder = "plugins" + File.separator + "MineshaftApi";

    public String getPluginFolder() { return MineshaftApi.getInstance().pluginFolder;}

    public String getPluginPath() {return getPluginFolder(); }

    public String getBlockCachePath() {return getPluginDataPath() + File.separator + "BlockCache"; }

    public String getPluginDataPath() {return getPluginFolder() + File.separator + "Data"; }

    public String getPlayerDataPath() {return getPluginDataPath() + File.separator + "PlayerData"; }

    public String getItemPath() {return getPluginFolder() + File.separator + "Items"; }

    public String getEventPath() {return getPluginFolder() + File.separator + "Events"; }

    public ItemManager getItemManagerInstance() {return itemManager;}

    public EventManager getEventManagerInstance() {return eventManager;}

    public CooldownManager getCooldownManager() {return cooldownManager;}

    public ConfigManager getConfigManager() {return configManager;}

    public RegionManager getRegionManager() {return regionManager;}

    public boolean hasVaultDependency() { return DependencyInit.hasVault(); }

    public VaultDependency getVault() {return dependencyInit.getVault();}

    public DependencyInit getDependencyInit() {return dependencyInit;}

    public static void reloadItems() {
        MineshaftApi.getInstance().itemManager.initialiseItems();
    }

    public static void reloadEvents() {
        MineshaftApi.getInstance().eventManager.initialiseEvents();
    }

    public static void reloadPlugin() {
        reloadItems();
        reloadEvents();
        reloadConfigs();
    }

    public static void reloadConfigs() {
        MineshaftApi.getInstance().getConfigManager().reloadConfigs();
        MineshaftApi.getInstance().getRegionManager().reload();
    }

    public static Player getAnyPlayer() {
        return Bukkit.getServer().getOnlinePlayers().iterator().next();
    }

    public static String getVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();

        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public ActionManager getActionManager() {return actionManager;}

    public PendingAbilities getPendingAbilities(UUID uuid) {
        if(pendingAbilities.get(uuid)!=null) {
            return pendingAbilities.get(uuid);
        }
        return new PendingAbilities();
    }

    public void removePendingAility(UUID uuid, PendingAbilities.PendingAbilityType pendingAbilityType) {
        PendingAbilities pendingAbilities = getPendingAbilities(uuid);
        pendingAbilities.removeAbility(pendingAbilityType);
        this.pendingAbilities.put(uuid,pendingAbilities);
    }

    public void setPendingAbilities(UUID uuid, PendingAbilities pendingAbilities) {
        this.pendingAbilities.put(uuid, pendingAbilities);
    }

    public void clearPendingAbilities(UUID uuid) {
        this.pendingAbilities.remove(uuid);
    }

    public static @Nullable Plugin getPlugin() {return Bukkit.getPluginManager().getPlugin("MineshaftApi");}

    public void cacheAbility(String ability, AbilityType abilityType) {
        Logger.logDebug("MineshaftApi has logged MineshaftRpg ability " + ability);
        abilities.put(ability,abilityType);
    }

    public void clearAbilities() {
        abilities.clear();
    }
}
