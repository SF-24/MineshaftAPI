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
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class MineshaftPlaceholderExpansion extends PlaceholderExpansion {

    private final MineshaftApi plugin;

    public MineshaftPlaceholderExpansion(MineshaftApi plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "mineshaft";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    // If not set to true, expansion will be disabled on plugin reload
    @Override
    public boolean persist() {
        return true;
    }

    // Request placeholders
    // TODO: Make code actually do something
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("placeholder1")) {
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }

        if (params.equalsIgnoreCase("placeholder2")) {
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }
        return null; //
    }

}
