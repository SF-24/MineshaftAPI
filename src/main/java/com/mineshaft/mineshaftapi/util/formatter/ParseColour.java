/*
 * Copyright (c) 2026. Sebastian Frynas
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

package com.mineshaft.mineshaftapi.util.formatter;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;

public class ParseColour {

    public static ChatColor getChatColour(NamedTextColor colour) {
        if (colour.equals(NamedTextColor.RED)) { // c
            return ChatColor.RED;
        } else if(colour.equals(NamedTextColor.GREEN)) { // a
            return ChatColor.GREEN;
        } else if (colour.equals(NamedTextColor.BLUE)) { // 9
            return ChatColor.BLUE;
        } else if (colour.equals(NamedTextColor.DARK_RED)) { // 4
            return ChatColor.DARK_RED;
        } else if (colour.equals(NamedTextColor.DARK_GREEN)) { // 2
            return ChatColor.DARK_GREEN;
        } else if (colour.equals(NamedTextColor.DARK_AQUA)) { // 3
            return ChatColor.DARK_AQUA;
        } else if (colour.equals(NamedTextColor.LIGHT_PURPLE)) { // d
            return ChatColor.DARK_PURPLE;
        } else if (colour.equals(NamedTextColor.DARK_GRAY)) { // 8
            return ChatColor.DARK_GRAY;
        } else if (colour.equals(NamedTextColor.DARK_BLUE)) { // 1
            return ChatColor.DARK_BLUE;
        } else if (colour.equals(NamedTextColor.BLACK)) { // 0
            return ChatColor.BLACK;
        } else if (colour.equals(NamedTextColor.DARK_PURPLE)) { // 5
            return ChatColor.DARK_PURPLE;
        } else if (colour.equals(NamedTextColor.GRAY)) { // 7
            return ChatColor.GRAY;
        } else if (colour.equals(NamedTextColor.GOLD)) { // 6
            return ChatColor.GOLD;
        } else if (colour.equals(NamedTextColor.AQUA)) { // b
            return ChatColor.AQUA;
        } else if (colour.equals(NamedTextColor.YELLOW)) { // e
            return ChatColor.YELLOW;
        } else if (colour.equals(NamedTextColor.WHITE)) {
            return ChatColor.WHITE;
        } else {
            return ChatColor.WHITE;
        }
    }

}
