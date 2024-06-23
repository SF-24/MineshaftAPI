package com.mineshaft.mineshaftapi.manager.item.fields;

import org.bukkit.ChatColor;

public enum ItemRarity {

    COMMON(ChatColor.WHITE.toString(), "Common"),
    RARE(ChatColor.BLUE.toString(), "Rare"),
    EXOTIC(ChatColor.DARK_PURPLE.toString(), "Exotic"),
    LEGENDARY(ChatColor.GOLD.toString(), "Legendary");

    private final String colourCode;
    private final String name;

    ItemRarity(String colourCode, String name) {
        this.colourCode = colourCode;
        this.name = name;
    }

    public String getName() {return name;}
    public String getColourCode() {return colourCode;}
}
