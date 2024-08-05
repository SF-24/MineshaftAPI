/*
 *     This program is a Minecraft plugin developed in Java for the Spigot API.
 *     It adds multiple RPG features intended for Multiplayer gameplay.
 *
 *     Copyright (C) 2024  Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.mineshaft.mineshaftapi;

import com.mineshaft.mineshaftapi.command.*;
import com.mineshaft.mineshaftapi.dependency.DependencyInit;
import com.mineshaft.mineshaftapi.dependency.VaultDependency;
import com.mineshaft.mineshaftapi.listener.InteractListener;
import com.mineshaft.mineshaftapi.listener.JoinListener;
import com.mineshaft.mineshaftapi.manager.PlayerManager;
import com.mineshaft.mineshaftapi.manager.event.EventManager;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.util.Language;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MineshaftApi extends JavaPlugin {

    ItemManager itemManager;
    EventManager eventManager;
    DependencyInit dependencyInit = new DependencyInit();

    @Override
    public void onEnable() {
        switch (getDebugLanguage()) {
            case POLISH:
                Logger.logInfo("Wtyczka mineshaft API została włączona");
            case ENGLISH:
                Logger.logInfo("Plugin mineshaft API has been enabled");
        }

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);

        getCommand("mineshaft").setExecutor(new MineshaftCommand());
        getCommand("menu").setExecutor(new MenuCommand());
        getCommand("balance").setExecutor(new MonetaryBalanceCommand());
        getCommand("getitem").setExecutor(new GetItemCommand());
        getCommand("heal").setExecutor(new HealCommand());

        // Initialise custom items
        itemManager=new ItemManager();
        itemManager.initialiseItems();

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
    }

    public static Language getLanguage(Player player) {
        return Language.ENGLISH;
    }

    public static Language getDebugLanguage() {return Language.ENGLISH;}

    public static MineshaftApi getInstance() {
        return MineshaftApi.getPlugin(MineshaftApi.class);
    }

    private final String pluginFolder = "plugins" + File.separator + "Mineshaft";

    public String getPluginFolder() { return MineshaftApi.getInstance().pluginFolder;}

    public String getPluginDataPath() {return getPluginFolder() + File.separator + "Data"; }

    public String getPlayerDataPath() {return getPluginDataPath() + File.separator + "PlayerData"; }

    public String getItemPath() {return getPluginDataPath() + File.separator + "Items"; }

    public String getEventPath() {return getPluginDataPath() + File.separator + "Events"; }

    public ItemManager getItemManagerInstance() {return itemManager;}

    public EventManager getEventManagerInstance() {return eventManager;}

    public boolean hasVaultDependency() { return dependencyInit.hasVault(); }

    public VaultDependency getVault() {return dependencyInit.getVault();}

    public static void reloadItems() {
        MineshaftApi.getInstance().itemManager.initialiseItems();
    }

    public static void reloadEvents() {
        MineshaftApi.getInstance().eventManager.initialiseEvents();
    }

    public static void reloadPlugin() {
        reloadItems();
        reloadEvents();
    }

    public static Player getAnyPlayer() {
        return Bukkit.getServer().getOnlinePlayers().iterator().next();
    }


}
