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

package com.mineshaft.mineshaftapi.manager.item;

import com.mineshaft.mineshaftapi.util.formatter.TextFormatter;
import org.bukkit.ChatColor;

public enum RangedItemStats {
    NULL(ChatColor.WHITE.toString(), 0),
    FIRING_SPEED(ChatColor.AQUA.toString(),11),
    FIRING_RANGE(ChatColor.BLUE.toString(),12);

    private final String colour;
    private final int priority;

    RangedItemStats(String colour, int priority) {
        this.priority=priority;
        this.colour=colour;
    }

    public int getPriority() {return priority;}
    public String getColour() {return colour;}
    public String getName() {
        return TextFormatter.capitaliseStringFully(TextFormatter.convertStringToName(this.name().replace("_CUSTOM", "")));
    }

}
