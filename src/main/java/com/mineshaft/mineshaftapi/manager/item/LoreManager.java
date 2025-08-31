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

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item.armour.ArmourManager;
import com.mineshaft.mineshaftapi.manager.item.armour.ArmourResistanceTypes;
import com.mineshaft.mineshaftapi.manager.item.armour.ArmourType;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemCategory;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemRarity;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemSubcategory;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemSubcategoryProperty;
import com.mineshaft.mineshaftapi.manager.item.item_properties.ItemAmmunitionManager;
import com.mineshaft.mineshaftapi.manager.item.item_slots.SlotManager;
import com.mineshaft.mineshaftapi.util.formatter.NumericFormatter;
import com.mineshaft.mineshaftapi.util.formatter.TextFormatter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class LoreManager {

    public static ArrayList<String> getLore(UUID uniqueId) {
        return getLore(uniqueId,null);
    }


    public static ArrayList<String> getLore(UUID uniqueId, ItemStack item) {
        // Get the item
        ArrayList<String> lore = new ArrayList<>();

        // Rarity String, including subcategory
        if (getRarityString(uniqueId) != null) {
            lore.add(getRarityString(uniqueId));
        }

        // Subcategory properties
        if(getSubcategoryPropertyLore(ItemManager.getItemSubcategory(uniqueId))!=null) {
            lore.add(getSubcategoryPropertyLore(ItemManager.getItemSubcategory(uniqueId)));
        }

        // Armour type
        if(getArmourLore(uniqueId)!=null) {
            lore.add(getArmourLore(uniqueId));
        }

        // Armour resistances
        lore.addAll(getArmourResistances(uniqueId));

        // Empty line break
        if(getArmourLore(uniqueId)!=null||getRarityString(uniqueId)!=null||!getArmourResistances(uniqueId).isEmpty()) {
            lore.add(" ");
        }

        // Item stats

        lore.addAll(getStatLore(uniqueId, item));

        lore.addAll(getRangedStatLore(uniqueId,item));

        // Empty line break
        if((!getRangedStatLore(uniqueId,item).isEmpty() || !getStatLore(uniqueId,item).isEmpty()) && (ItemManager.useAmmunition(uniqueId))) {
            lore.add(" ");
        }

        if(ItemManager.useAmmunition(uniqueId)) {
            lore.add(LoreManager.getAmmunitionString(ItemAmmunitionManager.getMaxAmmunition(uniqueId),ItemAmmunitionManager.getMaxAmmunition(uniqueId)));
        }

        if((!getStatLore(uniqueId,item).isEmpty()||!getRangedStatLore(uniqueId,item).isEmpty())&&!SlotManager.getSocketTypes(uniqueId).isEmpty()) {
            lore.add(" ");
            lore.add(ChatColor.BLUE+"Item Modifications");

            // Get the slot names
            HashMap<String,String> slotTypes = SlotManager.getSocketTypes(uniqueId);

            // Loop through the slots
            for(String slotName : slotTypes.keySet()) {
                // Display the slot item
                if (item != null && SlotManager.getSlot(item, slotName).getType() != Material.AIR) {
                    if(SlotManager.getSlot(item,slotName).hasItemMeta()) {
                        lore.add("  " + ChatColor.WHITE + slotTypes.get(slotName) + ": " + ChatColor.BLUE + SlotManager.getSlot(item, slotName).getItemMeta().getDisplayName());
                    } else {
                        lore.add("  " + ChatColor.WHITE + slotTypes.get(slotName) + ": " + ChatColor.BLUE + TextFormatter.convertStringToName(SlotManager.getSlot(item, slotName).getType().name()));
                    }
                } else {
                    // Display an empty slot
                    lore.add("  " + ChatColor.WHITE + slotTypes.get(slotName) + ": empty" );
                }
            }
        }

        // TODO: Display custom slots

        return lore;
    }

    public static String getStatString(ItemStats stat, Double value, ItemCategory category, int arg) {
        if(stat.equals(ItemStats.DAMAGE)||stat.equals(ItemStats.ATTACK_SPEED)||stat.equals(ItemStats.RANGED_DAMAGE)) {
            if(category.equals(ItemCategory.WEAPON_MELEE)||category.equals(ItemCategory.WEAPON_RANGED)||category.equals(ItemCategory.TOOL_AXE)||category.equals(ItemCategory.TOOL_PICKAXE)||category.equals(ItemCategory.TOOL_SHOVEL)||category.equals(ItemCategory.TOOL_HOE)) {
                return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value);
            }
        } else if(stat.equals(ItemStats.ARMOUR_CLASS)) {
            if(arg>0) {
                return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value) + ChatColor.DARK_GREEN + " + DEX" + ChatColor.WHITE + "(" + arg + ")";
            } else if(arg<0) {
                return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value) + ChatColor.DARK_GREEN + " + DEX";
            }
        }
        return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + "+" + NumericFormatter.formatNumberAdvanced(value);
    }

    public static String getRangedStatString(RangedItemStats stat, Double value) {
        if(value==null||value<0) value=0d;
        return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value);
    }

    public static String getAmmunitionString(int ammunition, int maxAmmunition) {
        return (ChatColor.GRAY + "Ammunition: " + ChatColor.DARK_GREEN + ammunition + ChatColor.DARK_GRAY + "/" + maxAmmunition);
    }

    public static String getRarityString(UUID uniqueId) {
        if(ItemManager.getItemRarity(uniqueId)==ItemRarity.STANDARD) return null;
        if (MineshaftApi.getInstance().getConfigManager().useItalicItemRarity()) {
            String italic = ChatColor.ITALIC.toString();
            return(ItemManager.getItemRarity(uniqueId).getSecondaryColourCode() + italic + ItemManager.getItemRarity(uniqueId).getName() + " " + getItemSubcategoryDisplay(uniqueId));
        } else {
            return(ItemManager.getItemRarity(uniqueId).getSecondaryColourCode() + ItemManager.getItemRarity(uniqueId).getName() + " " + getItemSubcategoryDisplay(uniqueId));
        }
    }

    public static String getItemSubcategoryDisplay(UUID uniqueId) {
        // TODO: parent item display check
        if (ItemManager.getItemRarity(uniqueId) != ItemRarity.STANDARD) {
            // OLD CODE
//            if (category.equals(ItemCategory.WEAPON_MELEE) || category.equals(ItemCategory.WEAPON_RANGED)) {
//                itemDisplay = "Weapon";
//            } else if (category.equals(ItemCategory.ARMOUR_CHESTPLATE)) {
//                itemDisplay = "Chestplate";
//            } else if (category.equals(ItemCategory.ARMOUR_LEGGINGS)) {
//                itemDisplay = "Leggings";
//            } else if (category.equals(ItemCategory.ARMOUR_BOOTS)) {
//                itemDisplay = "Boots";
//            } else if (category.equals(ItemCategory.ARMOUR_HELMET)) {
//                itemDisplay = "Helmet";
//            } else if (category.equals(ItemCategory.TOOL_AXE)) {
//                itemDisplay = "Axe";
//            } else if (category.equals(ItemCategory.TOOL_PICKAXE)) {
//                itemDisplay = "Pickaxe";
//            } else if (category.equals(ItemCategory.TOOL_SHOVEL)) {
//                itemDisplay = "Shovel";
//            } else if (category.equals(ItemCategory.TOOL_HOE)) {
//                itemDisplay = "Hoe";
//            } else if (category.equals(ItemCategory.ITEM_CONSUMABLE)) {
//                itemDisplay = "Consumable";
//            }

            if (ItemManager.getItemSubcategory(uniqueId) != null && !ItemManager.getItemSubcategory(uniqueId).equals(ItemSubcategory.DEFAULT)) {
                return TextFormatter.convertStringToName(ItemManager.getItemSubcategoryOverride(uniqueId));
            } else if(ItemManager.getItemSubcategory(ItemManager.getParentName(uniqueId)) != null) {
                return TextFormatter.convertStringToName(ItemManager.getItemSubcategoryOverride(ItemManager.getUuid(ItemManager.getParentName(uniqueId))));
            }
        }
        return "";
    }

    //
    public static String getArmourLore(UUID uniqueId) {
        if (!ArmourManager.getArmourType(uniqueId).equals(ArmourType.NONE)) {
            return(ChatColor.GRAY + ArmourManager.getArmourType(uniqueId).getName());
        }
        return null;
    }

    public static ArrayList<String> getArmourResistances(UUID uniqueId) {
        ArrayList<String> lore = new ArrayList<>();
        for(ArmourResistanceTypes element : ArmourManager.getArmourResistances(uniqueId)) {
            lore.add(ChatColor.GRAY + element.getDisplay());
        }
        return lore;
    }

    public static String getSubcategoryPropertyLore(ItemSubcategory subcategory) {
        if(!subcategory.getPropertyList().isEmpty()) {
            String properties = "";

            for (int priority = 0; priority < 6; priority++) {
                for (ItemSubcategoryProperty property : subcategory.getPropertyList()) {
                    if(priority == property.getPriority() && property.getName()!=null) {
                        if(!properties.isEmpty()) {
                            properties+=", "+property.getName();
                        } else {
                            properties= ChatColor.GRAY + property.getName();
                        }
                    }
                }
            }
            return(properties);
        }
        return null;
    }

    public static ArrayList<String> getStatLore(UUID uniqueId, ItemStack itemStack) {
        ArrayList<String> lore = new ArrayList<>();
        HashMap<ItemStats, Double> statMap;
        if(itemStack==null || itemStack.getType()==Material.AIR) {
            statMap=ItemManager.getStatMap(ItemManager.getItemName(uniqueId), "stats.");
        } else {
            statMap=ItemManager.getItemStatMap(itemStack);
        }

        int lowestPriority = 100;
        int highestPriority= 0;

        // Get stat priorities
        for (ItemStats stat : statMap.keySet()) {
            if(stat.getPriority()<lowestPriority) lowestPriority=stat.getPriority();
            if(stat.getPriority()>highestPriority) highestPriority=stat.getPriority();
        }

        if(lowestPriority<0) lowestPriority=0;

        for(int i = lowestPriority; i<=highestPriority; i++) {
            for(ItemStats stat : statMap.keySet()) {
                if (i == stat.getPriority()) {
                    // Display armour class.
                    if (stat.equals(ItemStats.ARMOUR_CLASS) && statMap.get(stat)!=0) {
                        lore.add(LoreManager.getStatString(stat, statMap.get(stat), ItemManager.getItemCategory(uniqueId), (int) Math.floor(
                                ItemManager.getStatMap(ItemManager.getItemName(uniqueId),"stats.").get(ItemStats.MAXIMUM_ADDED_DEX_MODIFIER)
                        )));
                    } else if(statMap.get(stat)!=0) {
                        lore.add(LoreManager.getStatString(stat, statMap.get(stat), ItemManager.getItemCategory(uniqueId), 0));
                    }
                }
            }
        }
        return lore;
    }

    public static ArrayList<String> getRangedStatLore(UUID uniqueId, ItemStack itemStack) {
        ArrayList<String> lore = new ArrayList<>();
        HashMap<RangedItemStats, Double> statMap;

        if(itemStack==null || itemStack.getType()==Material.AIR) {
            statMap=ItemManager.getRangedStatMap(ItemManager.getItemName(uniqueId), "stats.");
        } else {
            statMap=ItemManager.getRangedItemStatMap(itemStack);
        }

        int lowestPriority = 100;
        int highestPriority= 0;

        // Get stat priorities
        for (RangedItemStats stat : statMap.keySet()) {
            if(stat.getPriority()<lowestPriority) lowestPriority=stat.getPriority();
            if(stat.getPriority()>highestPriority) highestPriority=stat.getPriority();
        }

        if(lowestPriority<0) lowestPriority=0;

        for(int i = lowestPriority; i<=highestPriority; i++) {
            for(RangedItemStats stat : statMap.keySet()) {
                if (i == stat.getPriority() && statMap.get(stat)!=null) {
                    lore.add(LoreManager.getRangedStatString(stat, statMap.get(stat)));
                }
            }
        }
        return lore;
    }

}
