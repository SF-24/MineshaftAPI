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

package com.mineshaft.mineshaftapi.manager.item.fields;

import org.bukkit.ChatColor;

public enum ItemRarity {

    STANDARD(ChatColor.WHITE.toString(),ChatColor.GRAY.toString(), ""),
    COMMON(ChatColor.WHITE.toString(), ChatColor.GRAY.toString(), "Common"),
    UNCOMMON(ChatColor.GREEN.toString(),ChatColor.DARK_GREEN.toString(), "Uncommon"),
    RARE(ChatColor.BLUE.toString(), ChatColor.DARK_AQUA.toString(), "Rare"),
    EXOTIC(ChatColor.DARK_PURPLE.toString(),ChatColor.DARK_PURPLE.toString(), "Exotic"),
    LEGENDARY(ChatColor.GOLD.toString(),ChatColor.GOLD.toString(), "Legendary");

    private final String colourCode;
    private final String name;
    private final String secondaryColourCode;

    ItemRarity(String colourCode, String secondaryColourCode, String name) {
        this.colourCode = colourCode;
        this.secondaryColourCode=secondaryColourCode;
        this.name = name;
    }

    public String getName() {return name;}
    public String getColourCode() {return colourCode;}
    public String getSecondaryColourCode() {return secondaryColourCode;}
}
