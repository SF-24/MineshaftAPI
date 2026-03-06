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

package com.mineshaft.mineshaftapi.dependency;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.dependency.beton_quest.BetonQuestDependencyInit;
import com.mineshaft.mineshaftapi.dependency.mythic_mob.MythicListener;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;

public class DependencyInit {

//    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DependencyInit.class);
    VaultDependency vaultDependency = null;

    public void initialiseDependencies() {
        if(hasBetonQuest()) {
            try {
                Logger.logInfo("Enabling BetonQuest integration");
                BetonQuestDependencyInit.init();
            } catch(Exception ignored) {
                Logger.logError("Failed to load BetonQuest integration");
            }
        } else {
            Logger.logInfo("BetonQuest is not enabled. Plugin compatibility features have been disabled.");
        }
        if (hasPlaceholderAPI()) {
            // Register placeholders
            Logger.logInfo("Enabling PlaceholderAPI hooks");
            try {
            new MineshaftPlaceholderExpansion(MineshaftApi.getInstance()).register();
            } catch(Exception ignored) {
                Logger.logError("Failed to load PlaceholderAPI integration");
            }
        } else {
            // Log warning
            Logger.logInfo("PlaceholderAPI is not installed. While this plugin is not required, however some functionality will be disabled");
        }
        if (MineshaftApi.getInstance().getConfigManager().useVault() && hasVault()) {
            Logger.logInfo("Enabling vault integration");
            try {
            vaultDependency = new VaultDependency();
            } catch(Exception ignored) {
                Logger.logError("Failed to load Vault integration");
            }
        } else {
            if (MineshaftApi.getInstance().getConfigManager().disableDependencyWarnings() || !MineshaftApi.getInstance().getConfigManager().useVault()) {
                Logger.logInfo("Vault has been disabled in the config. Due to this, some functionality and compatibility features will be disabled");
            } else {
                // Log warning
                Logger.logInfo("Vault is not installed. While this plugin is not required, some functionality and compatibility features will be disabled");
            }
        }
        if(hasMythicMobs()) {
            // Register placeholders
            Logger.logInfo("Loading MythicMobs integration");
            try {
            Bukkit.getPluginManager().registerEvents(new MythicListener(), MineshaftApi.getInstance());
            } catch(Exception ignored) {
                Logger.logError("Failed to load MythicMobs integration");
            }
        } else {
            // Log warning
            Logger.logInfo("MythicMobs is not installed. Integration has not been enabled");
        }
        if(hasCraftEngine()) {
            try {
                Logger.logInfo("Enabling CraftEngine integration. WIP!");
                // TODO: Register
            } catch(Exception ignored) {
                Logger.logError("Failed to load CraftEngine integration");
            }
        } else {
            Logger.logInfo("Craft engine has not bee installed. The integration will not be enabled.");
        }
    }

    public static boolean hasPlaceholderAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static boolean hasAriKeys() {
        return Bukkit.getPluginManager().getPlugin("AriKeys") != null && Bukkit.getPluginManager().isPluginEnabled("AriKeys");
    }

    public static boolean hasVault() {
        return Bukkit.getPluginManager().getPlugin("Vault") != null && Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    public static boolean hasMythicMobs() {
        return Bukkit.getPluginManager().getPlugin("MythicMobs") != null && Bukkit.getPluginManager().isPluginEnabled("MythicMobs");
    }

    public static boolean hasBetonQuest() {
        return Bukkit.getPluginManager().getPlugin("BetonQuest") != null && Bukkit.getPluginManager().isPluginEnabled("BetonQuest") && hasBqClass();
    }

    public static boolean hasBqClass() {
        try {
            Class.forName("org.betonquest.betonquest.api.quest.PrimaryServerThreadData");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static boolean hasWorldGuard() {
        return Bukkit.getPluginManager().getPlugin("WorldGuard") != null && Bukkit.getPluginManager().isPluginEnabled("WorldGuard");
    }

    public static boolean hasCraftEngine() {
        return Bukkit.getPluginManager().getPlugin("CraftEngine") != null && Bukkit.getPluginManager().isPluginEnabled("CraftEngine");
    }

    public VaultDependency getVault() {return vaultDependency;}
}
