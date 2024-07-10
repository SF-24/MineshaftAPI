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

package com.mineshaft.mineshaftapi.manager.item;

import org.bukkit.ChatColor;

public enum ItemStats {

    NULL(""),

    DAMAGE(ChatColor.DARK_GREEN.toString()),

    SPEED(ChatColor.WHITE.toString()),

    DEFENCE(ChatColor.GREEN.toString()),
    HEALTH(ChatColor.RED.toString());

    private final String colour;

    ItemStats(String colour) {
        this.colour=colour;
    }

    public String getColour() {return colour;}
}
