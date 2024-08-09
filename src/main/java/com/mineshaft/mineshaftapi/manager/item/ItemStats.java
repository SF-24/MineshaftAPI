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

    NULL("",9),

    DAMAGE(ChatColor.DARK_GREEN.toString(),1),
    RANGED_DAMAGE(ChatColor.DARK_GREEN.toString(),2),

    SPEED(ChatColor.WHITE.toString(),5),

    DEFENCE(ChatColor.GREEN.toString(),4),
    HEALTH(ChatColor.RED.toString(),3),

    ATTACK_SPEED(ChatColor.AQUA.toString(),8),

    ATTACK_REACH(ChatColor.YELLOW.toString(),6),
    MINING_REACH(ChatColor.YELLOW.toString(),7),
    REACH(ChatColor.YELLOW.toString(),6);

    private final String colour;
    private final int priority;

    ItemStats(String colour, int priority) {
        this.priority=priority;
        this.colour=colour;
    }

    public int getPriority() {return priority;}
    public String getColour() {return colour;}
}
