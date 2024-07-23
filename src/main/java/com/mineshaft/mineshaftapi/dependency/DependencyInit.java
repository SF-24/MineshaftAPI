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
import com.mineshaft.mineshaftapi.text.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;

public class DependencyInit {

    VaultDependency vaultDependency = null;

    public void initialiseDependencies() {

        if (hasPlaceholderAPI()) {
            // Register placeholders
            new MineshaftPlaceholderExpansion(MineshaftApi.getInstance()).register();
        } else {
            // Log warning
            Logger.logWarning("PlaceholderAPI is not installed. While this plugin is not required, however some functionality will be disabled");
        }
        if(hasVault()) {
            vaultDependency=new VaultDependency();
        } else {
            // Log warning
            Logger.logWarning("Vault is not installed. While this plugin is not required, however some functionality and compatibility features will be disabled");
        }
    }

    public static boolean hasPlaceholderAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static boolean hasVault() {
        return Bukkit.getPluginManager().getPlugin("Vault") != null && Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    public VaultDependency getVault() {return vaultDependency;}
}
