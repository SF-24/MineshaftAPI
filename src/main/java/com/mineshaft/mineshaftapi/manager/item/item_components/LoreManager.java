/*
 * Copyright (c) 2025-2026. Sebastian Frynas
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

package com.mineshaft.mineshaftapi.manager.item.item_components;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.item.RangedItemStats;
import com.mineshaft.mineshaftapi.manager.item.configuration_fields.ItemCategory;
import com.mineshaft.mineshaftapi.manager.item.configuration_fields.ItemRarity;
import com.mineshaft.mineshaftapi.manager.item.configuration_fields.ItemSubcategory;
import com.mineshaft.mineshaftapi.manager.item.configuration_fields.ItemSubcategoryProperty;
import com.mineshaft.mineshaftapi.manager.item.item_components.armour.ArmourManager;
import com.mineshaft.mineshaftapi.manager.item.item_components.armour.ArmourResistanceTypes;
import com.mineshaft.mineshaftapi.manager.item.item_components.armour.ArmourType;
import com.mineshaft.mineshaftapi.manager.item.item_components.modifier_slots.SlotManager;
import com.mineshaft.mineshaftapi.util.formatter.NumericFormatter;
import com.mineshaft.mineshaftapi.util.formatter.TextFormatter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class LoreManager {

    // Get the full lore
    public static ArrayList<String> getItemLoreArrayList(UUID uniqueId) {
        return getItemLoreArrayList(uniqueId,null);
    }

    // Get the full lore
    public static ArrayList<String> getItemLoreArrayList(UUID uniqueId, ItemStack item) {
        // Get the item
        ArrayList<String> lore = new ArrayList<>();

        // Rarity String, including subcategory
        if (LoreSection.getRarityLoreLine(uniqueId) != null) {
            lore.add(LoreSection.getRarityLoreLine(uniqueId));
        }

        // Subcategory properties
        if(LoreSection.getItemSubcategoryPropertiesLoreLine(ItemManager.getItemSubcategory(uniqueId))!=null) {
            lore.add(LoreSection.getItemSubcategoryPropertiesLoreLine(ItemManager.getItemSubcategory(uniqueId)));
        }

        // Armour type
        if(LoreSection.getArmourTypeLoreLine(uniqueId)!=null) {
            lore.add(LoreSection.getArmourTypeLoreLine(uniqueId));
        }

        // Armour resistances
        lore.addAll(LoreSection.getArmourResistancesLoreSection(uniqueId));

        // Empty line break
        if(LoreSection.getArmourTypeLoreLine(uniqueId)!=null||LoreSection.getRarityLoreLine(uniqueId)!=null||!LoreSection.getArmourResistancesLoreSection(uniqueId).isEmpty()) {
            lore.add(" ");
        }

        // Item stats

        lore.addAll(LoreSection.getItemAttributeLoreSection(uniqueId, item));

        lore.addAll(LoreSection.getRangedItemAttributeLoreSection(uniqueId,item));

        // Empty line break
        if((!LoreSection.getRangedItemAttributeLoreSection(uniqueId,item).isEmpty() || !LoreSection.getItemAttributeLoreSection(uniqueId,item).isEmpty()) && (ItemManager.useAmmunition(uniqueId))) {
            lore.add(" ");
        }

        if(ItemManager.useAmmunition(uniqueId)) {
            lore.add(LoreManager.LoreSection.getAmmunitionLoreLine(ItemAmmunitionManager.getAmmunitionCountInWeapon(item),ItemAmmunitionManager.getMaximumAmmunitionCapacityInWeapon(uniqueId)));
        }

        if((!LoreSection.getItemAttributeLoreSection(uniqueId,item).isEmpty()||!LoreSection.getRangedItemAttributeLoreSection(uniqueId,item).isEmpty())&&!SlotManager.getSocketTypes(uniqueId).isEmpty()) {
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

    public static class LoreSection {
        public static String getStatLoreLine(ItemStats stat, Double value, ItemCategory category, int arg) {
            if(stat.equals(ItemStats.DAMAGE)||stat.equals(ItemStats.ATTACK_SPEED)||stat.equals(ItemStats.RANGED_DAMAGE)) {
                if(category.equals(ItemCategory.WEAPON_MELEE)||category.equals(ItemCategory.WEAPON_RANGED)||category.equals(ItemCategory.TOOL_AXE)||category.equals(ItemCategory.TOOL_PICKAXE)||category.equals(ItemCategory.TOOL_SHOVEL)||category.equals(ItemCategory.TOOL_HOE)) {
                    return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value);
                }
            } else if(stat.equals(ItemStats.ARMOUR_CLASS)) {
                if(arg>0) {
                    return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value) + ChatColor.DARK_GREEN + " + DEX" + ChatColor.WHITE + " (" + arg + ")";
                } else if(arg<0) {
                    return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value) + ChatColor.DARK_GREEN + " + DEX";
                }
            }
            return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + "+" + NumericFormatter.formatNumberAdvanced(value);
        }

        public static String getRangedStatLoreLine(RangedItemStats stat, Double value) {
            if(value==null||value<0) value=0d;
            return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value);
        }

        public static String getAmmunitionLoreLine(int ammunition, int maxAmmunition) {
            return (ChatColor.GRAY + "Ammunition: " + ChatColor.DARK_GREEN + ammunition + ChatColor.DARK_GRAY + "/" + maxAmmunition);
        }

        public static String getRarityLoreLine(UUID uniqueId) {
            return getRarityLoreLine(ItemManager.getItemRarity(uniqueId), getItemSubcategoryDisplayedName(uniqueId));
        }

        public static String getRarityLoreLine(ItemRarity itemRarity, String itemSubcategory) {
            if(itemRarity==ItemRarity.STANDARD) return null;
            String formatting = "";
            if (MineshaftApi.getInstance().getConfigManager().useItalicItemRarity()) {
                formatting += ChatColor.ITALIC.toString();
            }
            if (MineshaftApi.getInstance().getConfigManager().useBoldItemRarity()) {
                formatting += ChatColor.BOLD.toString();
            }
            if (MineshaftApi.getInstance().getConfigManager().useCapitalisedItemRarity()) {
                return(itemRarity.getColourCode() + formatting + itemRarity.getName() + " " + itemSubcategory);
            } else {
                return(itemRarity.getColourCode() + formatting + itemRarity.getName().toUpperCase() + " " + itemSubcategory);
            }
        }


        public static String getItemSubcategoryDisplayedName(UUID uniqueId) {
            if (ItemManager.getItemRarity(uniqueId) != ItemRarity.STANDARD) {
                if (ItemManager.getItemSubcategory(uniqueId) != null && !ItemManager.getItemSubcategory(uniqueId).equals(ItemSubcategory.DEFAULT)) {
                    return TextFormatter.convertStringToName(ItemManager.getItemSubcategoryOverride(ItemManager.getItemDefinition(uniqueId)));
                } else if(ItemManager.getItemSubcategory(ItemManager.getParentName(ItemManager.getItemDefinition(uniqueId))) != null) {
                    return TextFormatter.convertStringToName(ItemManager.getItemSubcategoryOverride(ItemManager.getItemDefinition(ItemManager.getUuid(ItemManager.getParentName(ItemManager.getItemDefinition(uniqueId))))));
                }
            }
            return "";
        }

        // TODO: parent item display check

        public static String getItemSubcategoryDisplayedName(ItemRarity itemRarity, ItemSubcategory itemSubcategory) {
            if (itemRarity != ItemRarity.STANDARD) {
                if (itemSubcategory != null && !itemSubcategory.equals(ItemSubcategory.DEFAULT)) {
                    return TextFormatter.convertStringToName(itemSubcategory.name().toLowerCase());
                }
            }
            return "";
        }

        // Get the armour section of lore.
        public static String getArmourTypeLoreLine(UUID uniqueId) {
            if (!ArmourManager.getArmourType(uniqueId).equals(ArmourType.NONE)) {
                return(ChatColor.GRAY + ArmourManager.getArmourType(uniqueId).getName());
            }
            return null;
        }

        // Get the armour resistances
        public static ArrayList<String> getArmourResistancesLoreSection(UUID uniqueId) {
            ArrayList<String> lore = new ArrayList<>();
            for(ArmourResistanceTypes element : ArmourManager.getArmourResistances(uniqueId)) {
                lore.add(ChatColor.GRAY + element.getDisplay());
            }
            return lore;
        }

        // Get the lore regarding the subcategory of item (e.g. Common Sword, etc.)
        public static String getItemSubcategoryPropertiesLoreLine(ItemSubcategory subcategory) {
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

        // Get the lore regarding item attributes
        public static ArrayList<String> getItemAttributeLoreSection(UUID uniqueId, ItemStack itemStack) {
            ArrayList<String> lore = new ArrayList<>();
            HashMap<ItemStats, Double> statMap;
            if(itemStack==null || itemStack.getType()==Material.AIR) {
                statMap=ItemManager.getStatMap(ItemManager.getItemDefinition(uniqueId), "stats.");
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
                            lore.add(LoreManager.LoreSection.getStatLoreLine(stat, statMap.get(stat), ItemManager.getItemCategory(uniqueId), (int) Math.floor(
                                    ItemManager.getStatMap(ItemManager.getItemDefinition(uniqueId),"stats.").get(ItemStats.MAXIMUM_ADDED_DEX_MODIFIER)
                            )));
                        } else if(statMap.get(stat)!=0) {
                            lore.add(LoreManager.LoreSection.getStatLoreLine(stat, statMap.get(stat), ItemManager.getItemCategory(uniqueId), 0));
                        }
                    }
                }
            }
            return lore;
        }

        // Get the lore regarding ranged item attributes
        public static ArrayList<String> getRangedItemAttributeLoreSection(UUID uniqueId, ItemStack itemStack) {
            ArrayList<String> lore = new ArrayList<>();
            HashMap<RangedItemStats, Double> statMap;

            if(itemStack==null || itemStack.getType()==Material.AIR) {
                statMap=ItemManager.getRangedStatMap(ItemManager.getItemDefinition(uniqueId), "ranged_stats.");
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
                        lore.add(LoreManager.LoreSection.getRangedStatLoreLine(stat, statMap.get(stat)));
                    }
                }
            }
            return lore;
        }
    }
}
