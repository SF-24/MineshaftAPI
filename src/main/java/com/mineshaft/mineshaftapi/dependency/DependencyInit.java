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

package com.mineshaft.mineshaftapi.dependency;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.dependency.mythic_mob.MythicEventListener;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;

public class DependencyInit {

    VaultDependency vaultDependency = null;

    public void initialiseDependencies() {

        if (hasMythicMobs()) {
            // Register placeholders
            Bukkit.getPluginManager().registerEvents(new MythicEventListener(), MineshaftApi.getInstance());
        } else {
            // Log warning
            Logger.logWarning("MythicMobs is not installed. Integration has not been enabled");
        }
        if (hasPlaceholderAPI()) {
            // Register placeholders
            new MineshaftPlaceholderExpansion(MineshaftApi.getInstance()).register();
        } else {
            // Log warning
            Logger.logWarning("PlaceholderAPI is not installed. While this plugin is not required, however some functionality will be disabled");
        }
        if (MineshaftApi.getInstance().getConfigManager().useVault() && hasVault()) {
            vaultDependency = new VaultDependency();
        } else {
            if (!MineshaftApi.getInstance().getConfigManager().useVault()) {
                Logger.logInfo("Vault has been disabled in the config. Due to this, some functionality and compatibility features will be disabled");
            } else {
                // Log warning
                Logger.logWarning("Vault is not installed. While this plugin is not required, however some functionality and compatibility features will be disabled");
            }
        }
    }

    public static boolean hasPlaceholderAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static boolean hasVault() {
        return Bukkit.getPluginManager().getPlugin("Vault") != null && Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    public static boolean hasMythicMobs() {
        return Bukkit.getPluginManager().getPlugin("MythicMobs") != null && Bukkit.getPluginManager().isPluginEnabled("MythicMobs");
    }

    public VaultDependency getVault() {return vaultDependency;}
}
