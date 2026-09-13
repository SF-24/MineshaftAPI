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

package com.mineshaft.mineshaftapi.manager.item;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item.configuration_fields.ItemCategory;
import com.mineshaft.mineshaftapi.manager.item.configuration_fields.ItemRarity;
import com.mineshaft.mineshaftapi.manager.item.configuration_fields.ItemSubcategory;
import com.mineshaft.mineshaftapi.manager.item.item_components.ItemAmmunitionManager;
import com.mineshaft.mineshaftapi.manager.item.item_components.LoreManager;
import com.mineshaft.mineshaftapi.manager.item.item_components.armour.ArmourManager;
import com.mineshaft.mineshaftapi.manager.item.item_components.custom_property_classes.ArmourTypeComponent;
import com.mineshaft.mineshaftapi.manager.item.item_components.custom_property_classes.CustomItemComponent;
import com.mineshaft.mineshaftapi.manager.item.item_components.handlers.*;
import com.mineshaft.mineshaftapi.util.Logger;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTList;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

public class ItemBuilder {

    public static ItemStack getItem(String internalName, ConfigurationSection yamlConfiguration) {

        // Whether the item has a parent item
        boolean hasParent = false;

        // Get uuid and run a null uuid check
        // If the uuid is null, something has gone very wrong with the plugin.
        if(yamlConfiguration.getString("id")==null) {
            Logger.logError("Invalid null uuid for item " + internalName);
            return new ItemStack(Material.AIR);
        }
        UUID uuid = UUID.fromString(yamlConfiguration.getString("id"));

        // Create the item.
        ItemStack item = new ItemStack(Material.BARRIER);

        // Rarity
        ItemRarity rarity = ItemManager.getItemRarity(uuid);

        String itemDisplay = "Item";
        String parentItemDisplay = null;

        ItemSubcategory subcategory = ItemManager.getItemSubcategory(uuid);

        if (yamlConfiguration.contains("parent")) {
            String parentName = yamlConfiguration.getString("parent");
            if (parentName != null && !parentName.equalsIgnoreCase("null") && !parentName.equalsIgnoreCase("nil")) {
                item = MineshaftApi.getInstance().getItemManagerInstance().getItem(parentName);
                hasParent = true;
//                File parent = new File(path, itemName + ".yml");
//                if (parent.exists()) {
//                    YamlConfiguration parentYaml = YamlConfiguration.loadConfiguration(parent);
//                    if (parentYaml.getString("subcategory") != null) {
//                        parentItemDisplay = TextFormatter.convertStringToName(parentYaml.getString("subcategory"));
//                        subcategory = getItemSubcategory(parentYaml.getString("subcategory"));
//                    }
//                }
            }
        }

        if (yamlConfiguration.contains("material")) {
            try {
                item = new ItemStack(Material.valueOf(yamlConfiguration.getString("material").toUpperCase()));
            } catch (Exception e) {
                if (!hasParent) {
                    Logger.logError("ERROR! Could not load item '" + internalName + "' invalid material");
                    return null;
                }
                Logger.logWarning("ERROR! Could not load material for item: '" + internalName + "'. Invalid material. Using parent item material instead");
            }
        }

        // ItemMeta variable
        ItemMeta meta = item.getItemMeta();

        // Temporarily unused - will be used for stuff like AH sorting, item abilities, etc.
        ItemCategory category = ItemCategory.ITEM_GENERIC;

        // Item display name
        String displayName = "Custom Item";

        // Item stat values
//        double maximum_dex_modifier = 0;
//        double defence = 0;
//        double speed = 0;
//        double ranged_damage = 0;

        int durability = 0;

        int r = -1;
        int g = -1;
        int b = -1;

        String statsString = "stats";
        String rangedStatsString = "ranged_stats";

        boolean hideAttributes = true;
        boolean hideTooltip = true;

//        final ArmourType armourType;

        List<CustomItemComponent> customItemComponents = new ArrayList<>();
        List<ComponentHandler> itemHandlers = new ArrayList<>();

        List<String> itemProperties = new ArrayList<>();

        // Initial fields.
        for (String field : yamlConfiguration.getKeys(false)) {
            switch (field) {
                case "item_properties":
                    itemProperties=yamlConfiguration.getStringList("item_properties");
                case "item_category":
                    category = ItemCategory.valueOf(yamlConfiguration.getString("item_category").toUpperCase(Locale.ROOT));
                    break;
                case "custom_model_data":
                    meta.setCustomModelData(yamlConfiguration.getInt("custom_model_data"));
                    break;
                case "tooltip_style":
                    meta.setTooltipStyle(NamespacedKey.minecraft(yamlConfiguration.getString("tooltip_style")));
                    break;
                case "item_model":
                    // TODO: FIX
                    meta.setItemModel(NamespacedKey.minecraft(yamlConfiguration.getString("item_model")));
                    break;
                case "name":
                    displayName = yamlConfiguration.getString("name");
                    break;
                // Initialise stats
                case "stats":
                    statsString = "stats";
                    break;
                case "attributes":
                    statsString = "attributes";
                    break;
                case "modifiers":
                    statsString = "modifiers";
                    break;
                case "durability":
                    meta.setMaxStackSize(1);
                    durability = yamlConfiguration.getInt("durability");
                case "stack_size":
                    meta.setMaxStackSize(yamlConfiguration.getInt("stack_size"));
                case "enchantment_glint":
                    meta.setEnchantmentGlintOverride(yamlConfiguration.getBoolean("enchantment_glint"));
                case "hide_attributes":
                    hideAttributes = yamlConfiguration.getBoolean("hide_attributes");
                case "hide_tooltip":
                    hideTooltip = yamlConfiguration.getBoolean("hide_tooltip");
                default:
            }
        }

        if (category == ItemCategory.ARMOUR_HELMET || category == ItemCategory.ARMOUR_BOOTS || category == ItemCategory.ARMOUR_CHESTPLATE || category == ItemCategory.ARMOUR_LEGGINGS) {
            /**
             * Armour types
             * */
            customItemComponents.add(new ArmourTypeComponent(ArmourManager.getArmourType(uuid)));
//            /**
//             * Cold protection
//             * */
//            if(yamlConfiguration.contains("armour.cold_protection")) {
//                customItemComponents.add(new ArmourColdProtectionComponent());
//            }
            /**
             * Armour colour
             * */
            if (yamlConfiguration.contains("armour.colour")) {
                if (yamlConfiguration.contains("armour.colour.g")) {
                    g = yamlConfiguration.getInt("armour.colour.g");
                }
                if (yamlConfiguration.contains("armour.colour.r")) {
                    r = yamlConfiguration.getInt("armour.colour.r");
                }
                if (yamlConfiguration.contains("armour.colour.b")) {
                    b = yamlConfiguration.getInt("armour.colour.b");
                }
            }
        }

        /**
         * The ammunition use component
         * */
        List<String> ammunitionTypes = Collections.emptyList();
        int maxAmmunition = 0;
        if(ItemManager.useAmmunition(uuid)) {
            maxAmmunition = ItemAmmunitionManager.getMaximumAmmunitionCapacityInWeapon(uuid);
            ammunitionTypes = ItemAmmunitionManager.getUsedAmmunitionTypesForItem(uuid);
        }

        /**
         * Get the item lore.
         * */
        ArrayList<String> lore = LoreManager.getItemLoreArrayList(uuid);

        /**
         * Load file stats, append to lore and add them to the item
         * */

        // Get standard and ranged item statistics
        HashMap<ItemStats, Double> statMap = ItemManager.getStatMap(yamlConfiguration, statsString);
        HashMap<RangedItemStats, Double> rangedStatMap = ItemManager.getRangedStatMap(yamlConfiguration, rangedStatsString);

        // The slot where the item is equipped
        EquipmentSlot slot = null;

        switch (category) {

            case WEAPON_MELEE, WEAPON_RANGED, TOOL_AXE, TOOL_PICKAXE, TOOL_SHOVEL, TOOL_HOE:
                slot = EquipmentSlot.HAND;
                break;
            case ARMOUR_HELMET:
                slot = EquipmentSlot.HEAD;
                break;
            case ARMOUR_CHESTPLATE:
                slot = EquipmentSlot.CHEST;
                break;
            case ARMOUR_LEGGINGS:
                slot = EquipmentSlot.LEGS;
                break;
            case ARMOUR_BOOTS:
                slot = EquipmentSlot.FEET;
                break;
            case ITEM_CONSUMABLE:
                slot = null;
                itemHandlers.add(new FoodHandler(yamlConfiguration));
                break;
            case OTHER:
            case AMMUNITION:
            case ITEM_GENERIC:
                slot = null;
                break;
        }
        if(yamlConfiguration.contains("tool")) {
            try {
                itemHandlers.add(new ToolHandler(yamlConfiguration));
            } catch (NullPointerException e) {
                Logger.logError("Error. Could not load tool properties for " + internalName);
            }
        }
        
//        int lowestPriority = 100;
//        int highestPriority= 0;

        /**
         * Item attribute modifiers
         * */
        itemHandlers.add(new AttributeHandler(slot,statMap,yamlConfiguration));

        /**
         * Set the tooltip visibilities.
         * */

        if (hideAttributes) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        if(hideTooltip) {
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        }
        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        /**
         * Apply the lore
         * */
        meta.setLore(lore);

        /**
         * Custom equipment
         */

        if(slot!=null && yamlConfiguration.contains("armour")) {
            itemHandlers.add(new EquippableComponentHandler(slot, yamlConfiguration));
        }

        // IMPORTANT!
        // set item meta. no meta modification after here!!!!!!!
        // Loop though handlers and properties to apply stuff.
        for(ComponentHandler itemPropertyHandler : itemHandlers) {
            itemPropertyHandler.handlePropertyPreMeta(meta);
        }
        item.setItemMeta(meta);

        // Set the name
        item.setData(DataComponentTypes.ITEM_NAME, Component.translatable(displayName).color(rarity.getTextColour()));

        /**
         * NBT Features
         */

        if(meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) item.getItemMeta();
            if(g>=0 || r>=0 || b>=0) {
                leatherArmorMeta.setColor(Color.fromRGB(Math.max(r, 0), Math.max(g, 0),Math.max(b,0)));
            }
            item.setItemMeta(leatherArmorMeta);
        }

        if (durability > 0) {
            Damageable damageableMeta = (Damageable) item.getItemMeta();
            damageableMeta.setMaxDamage(durability);
            item.setItemMeta(damageableMeta);
        }

        // final variant meta above here. No more after this.

        // Apply NBT tag with item
        customItemComponents.add(ArmourManager.getArmourResistanceComponent(uuid));

        int finalMaxAmmunition = maxAmmunition;
        List<String> finalAmmunitionTypes = ammunitionTypes;
        NBT.modify(item, nbt -> {
            nbt.setString("uuid", uuid.toString());

            if(finalMaxAmmunition>0) {
                nbt.setInteger("ammunition", finalMaxAmmunition);
                if(finalAmmunitionTypes.contains("ammunition_power_cell")) {
                    nbt.setString("ammunition_type", "ammunition_power_cell");
                } else {
                    nbt.setString("ammunition_type", finalAmmunitionTypes.get(0));
                }
            }

            // Extendable weapon mechanics
            if(yamlConfiguration.contains("extendable")) {
                nbt.setBoolean("is_sheathed",true);
                nbt.setInteger("custom_model_data", Objects.requireNonNullElse(yamlConfiguration.getInt("custom_model_data"),0));

                for(String field : yamlConfiguration.getConfigurationSection("extendable").getKeys(false)) {
                    switch (field) {
                        case "custom_model_data" -> nbt.setInteger("open_custom_model_data", yamlConfiguration.getInt("extendable.custom_model_data"));
                        // TODO: Add more customisation
                    }
                }
            }

        });

        /**
         * Custom Item Attributes
         * */

        // Parse custom stats.
        for(RangedItemStats stat : rangedStatMap.keySet()) {
            ItemManager.setCustomItemAttribute(item, stat, rangedStatMap.get(stat));
        }

        /**
         * External consumable handler
         * */
        if(yamlConfiguration.contains("consumable")) {
            itemHandlers.add(new ConsumableHandler(yamlConfiguration));
        }

        /**
         * Item subcategory and rarity
         * TODO: Fix applying twice
         * */
        // Set rarity tag
        NBT.modify(item, nbt -> {
            nbt.setString("rarity", rarity.toString());
        });

        final String rareString = rarity.name().toLowerCase();

        String finalSubcategory = subcategory.name().toLowerCase();
        List<String> finalItemProperties1 = itemProperties;
        NBT.modify(item, nbt -> {
            nbt.setString("rarity", rareString);
            nbt.setString("subcategory", finalSubcategory);

            // create the item property list
            ReadWriteNBTList<String> propertyList = nbt.getStringList("item_properties");
            propertyList.addAll(finalItemProperties1);
        });

        /**
         * Custom hardcoded properties
         * */
        itemHandlers.add(new CustomHardcodedInteractEventHandler(uuid));

        /**
         * Loop through a list of post-meta handlers
         * */
        for(ComponentHandler itemPropertyHandler : itemHandlers) {
            itemPropertyHandler.handlePropertyPostMeta(item);
        }

        /**
         * Apply the components from the list
         * */
        for(CustomItemComponent customItemComponent : customItemComponents) {
            if(customItemComponent!=null) {
                customItemComponent.applyPropertyToItemPostItemMeta(item);
            }
        }
        return item;
    }


}
