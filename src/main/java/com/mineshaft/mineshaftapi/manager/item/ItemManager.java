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

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.VariableTypeEnum;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemCategory;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemFields;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemRarity;
import com.mineshaft.mineshaftapi.text.Logger;
import com.mineshaft.mineshaftapi.text.NumericFormatter;
import com.mineshaft.mineshaftapi.text.TextFormatter;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ItemManager {

    HashMap<UUID, String> items = new HashMap<>();

    public HashMap<UUID, String> getItemList() {
        return items;
    }

    String path = MineshaftApi.getInstance().getItemPath();

    public void initialiseItems() {
        File folder = new File(path);
        if(!folder.exists()) {
            folder.mkdirs();
        }

        if(folder.listFiles()==null || folder.listFiles().length==0) {
            createDemoItem();
        }

        for(File file : Objects.requireNonNull(folder.listFiles())) {
            initialiseItem(file.getName());
        }
    }

    public void initialiseItem(String fileName) {
        File fileYaml = new File(path, fileName);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        String name = fileName.substring(0, fileName.lastIndexOf('.'));

        if(!yamlConfiguration.contains("id")) {
            yamlConfiguration.createSection("id");
            yamlConfiguration.set("id", UUID.randomUUID());
            try {
                yamlConfiguration.save(fileYaml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        items.put(UUID.fromString(yamlConfiguration.getString("id")), name);
        Logger.logInfo("Initialised item '" + name + "' with UUID '" + yamlConfiguration.getString("id") + "'");
    }

    public String getItemName(UUID uuid) {
        return items.get(uuid);
    }

    public UUID getItemIdFromItem(ItemStack item) {
        NBT.get(item, nbt -> {
            String uuid = nbt.getString("uuid");
            return uuid;
        });
        return null;
    }

    public String getItemNameFromItem(ItemStack item) {
        UUID uuid = getItemIdFromItem(item);
        if(uuid != null) {
            return getItemName(uuid);
        }
        return null;
    }

    public ItemStack getItem(String itemName) {
        File fileYaml = new File(path, itemName+".yml");

        // return null if file does not exist
        if(!fileYaml.exists()) return null;

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        // Whether the item has a parent item
        boolean hasParent=false;

        String uuid = yamlConfiguration.getString("id");

        ItemStack item = new ItemStack(Material.BARRIER);

        if(yamlConfiguration.contains("parent")) {
            String parentName = yamlConfiguration.getString("parent");
            if(parentName!=null && !parentName.equalsIgnoreCase("null") && !parentName.equalsIgnoreCase("nil")) {
                item = getItem(parentName);
                hasParent=true;
            }
        }

        if(yamlConfiguration.contains("material")) {
            try {
                item = new ItemStack(Material.valueOf(yamlConfiguration.getString("material")));
            } catch (Exception e) {
                if(!hasParent) {
                    Logger.logError("ERROR! Could not load item '" + itemName + "' invalid material");
                    return null;
                }
                Logger.logWarning("ERROR! Could not load material for item: '" + itemName + "'. Invalid material. Using parent item material instead");
            }
        }

        // ItemMeta variable
        ItemMeta meta = item.getItemMeta();

        // Rarity
        ItemRarity rarity = ItemRarity.STANDARD;

        // Temporarily unused - will be used for stuff like AH sorting, item abilities, etc.
        ItemCategory category = ItemCategory.ITEM_GENERIC;

        // Item display name
        String displayName = "Custom Item";

        // Item stat values
        double defence = 0;
        double speed = 0;

        String statsString = "stats";

        for(String field : yamlConfiguration.getKeys(false)) {
            switch (field) {
                case "rarity":rarity = ItemRarity.valueOf(yamlConfiguration.getString("rarity").toUpperCase(Locale.ROOT));
                    break;
                case "item_category": category = ItemCategory.valueOf(yamlConfiguration.getString("item_category").toUpperCase(Locale.ROOT));
                    break;
                case "custom_model_data": meta.setCustomModelData(yamlConfiguration.getInt("custom_model_data"));
                    break;
                case "name": displayName=yamlConfiguration.getString("name");
                    break;
                // Initialise stats
                case "stats":
                    statsString="stats";
                    break;
                case "attributes":
                    statsString="attributes";
                    break;
                case "modifiers":
                    statsString="modifiers";
                    break;
                default:
            }
        }

        meta.setDisplayName(rarity.getColourCode() + displayName);

        // GENERATE LORE:
        ArrayList<String> lore = new ArrayList<>();

        if(rarity!=ItemRarity.STANDARD) {
            lore.add(rarity.getColourCode() + ChatColor.ITALIC.toString() + rarity.getName() + " Item");
            lore.add("");
        }

        // Load file stats, append to lore and add them to the item

        HashMap<ItemStats, Double> statMap = getStatMap(itemName,statsString);
        System.out.println("stat map: " + statMap);

        EquipmentSlot slot = null;

        switch (category) {

            case WEAPON_MELEE:
                slot=EquipmentSlot.HAND;
                break;
            case WEAPON_RANGED:
                slot=EquipmentSlot.HAND;
                break;
            case ARMOUR_HELMET:
                slot=EquipmentSlot.HEAD;
                break;
            case ARMOUR_CHESTPLATE:
                slot=EquipmentSlot.CHEST;
                break;
            case ARMOUR_LEGGINGS:
                slot=EquipmentSlot.LEGS;
                break;
            case ARMOUR_BOOTS:
                slot=EquipmentSlot.FEET;
                break;
            case TOOL_AXE:
                slot=EquipmentSlot.HAND;
                break;
            case TOOL_PICKAXE:
                slot=EquipmentSlot.HAND;
                break;
            case TOOL_SHOVEL:
                slot=EquipmentSlot.HAND;
                break;
            case TOOL_HOE:
                slot=EquipmentSlot.HAND;
                break;
            case ITEM_CONSUMABLE:
                slot=null;
                break;
            case ITEM_GENERIC:
                slot=null;
                break;
            case OTHER:
                slot=null;
                break;
        }

        for(ItemStats stat : statMap.keySet()) {
            double value = statMap.get(stat);

            lore.add(getStatString(stat, value));

            AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID().toString(), value, AttributeModifier.Operation.ADD_NUMBER);
            if(slot!=null) {
                attributeModifier = new AttributeModifier(UUID.randomUUID(), UUID.randomUUID().toString(), value, AttributeModifier.Operation.ADD_NUMBER, slot);
            }

            switch (stat) {
                case DAMAGE:
                    meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attributeModifier);
                    break;
                case DEFENCE:
                    defence=value;
                    break;
                case SPEED:
                    speed=value;
                    break;
                case HEALTH:
                    meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, attributeModifier);
                    break;
                default:
            }
        }

        meta.setLore(lore);

        item.setItemMeta(meta);

        // Apply NBT tag with item
        NBT.modify(item, nbt -> {
            nbt.setString("uuid", uuid);
            // More are available! Ask your IDE, or see Javadoc for suggestions!
        });

        if(speed!=0) {
            setItemNbtStat(item,ItemStats.SPEED,speed);
        }
        if(defence!=0) {
            setItemNbtStat(item,ItemStats.DEFENCE,defence);
        }

        return item;
    }

    // Generate the default item
    public static void createDemoItem() {
        String path = MineshaftApi.getInstance().getItemPath();
        File fileYaml = new File(path, "example-item" + ".yml");

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        Logger.logInfo("Yaml path for demo item: " + path + " demo-item.yml");

        if (!fileYaml.exists()) {
            try {
                Logger.logInfo("Create file: " + path + " demo-item.yml");

                // Create demo item
                File yamlDir = fileYaml.getParentFile();
                if (!yamlDir.exists()) {
                    yamlDir.mkdir();
                }
                fileYaml.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(ItemFields itemFields : ItemFields.values()) {
                yamlConfiguration.createSection(itemFields.name().toLowerCase(Locale.ROOT));
                if (!itemFields.getVariableType().equals(VariableTypeEnum.LIST)) {
                    yamlConfiguration.set(itemFields.name().toLowerCase(Locale.ROOT), itemFields.getDefaultValue());
                }
            }

            // Create stats section
            yamlConfiguration.createSection("stats.damage");
            yamlConfiguration.set("stats.damage",5);

            // Save demo item
            try {
                yamlConfiguration.save(fileYaml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected HashMap<ItemStats, Double> getStatMap(String name, String statPath) {
        System.out.println("getting statmap");

        HashMap<ItemStats, Double> statMap = new HashMap<>();

        String path = MineshaftApi.getInstance().getItemPath();
        File fileYaml = new File(path, name + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        if(!yamlConfiguration.contains(statPath)) {
            Logger.logError("could not find " + statPath + " in: " + path + "/" + name + ".yml");
            return statMap;
        }

        String yamlPath = statPath + ".";

        for(String key : yamlConfiguration.getConfigurationSection(statPath).getKeys(false)) {

            System.out.println("section: " + key);

            String yamlStatPath = yamlPath + key;

            double value = yamlConfiguration.getDouble(yamlStatPath);
            ItemStats statKey = ItemStats.valueOf(key.toUpperCase(Locale.ROOT));

            if(statKey!=null && !statKey.equals(ItemStats.NULL)) {
                statMap.put(statKey,value);
            }
        }
        return statMap;
    }

    protected static String getStatString(ItemStats stat, Double value) {
        return ChatColor.GRAY + TextFormatter.capitaliseString(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + "+" + NumericFormatter.formatNumberAdvanced(value);
    }

    protected static void setItemNbtStat(ItemStack stack, ItemStats stat, double value) {
        NBT.modify(stack, nbt -> {
            nbt.setDouble("stat."+stat.name().toLowerCase(Locale.ROOT), value);
        });
    }

    public static double getItemNbtStat(ItemStack stack, ItemStats stat) {
        NBT.get(stack, nbt -> {
            return nbt.getDouble("stat."+stat.name().toLowerCase(Locale.ROOT));
        });
        return 0;
    }

    public static HashMap<ItemStats, Double> getItemNbtStats(ItemStack stack) {
        HashMap<ItemStats, Double> statMap = new HashMap<>();

        for(ItemStats stat : ItemStats.values()) {
            double value=getItemNbtStat(stack,stat);
            if(value!=0) statMap.put(stat,value);
        }
        return statMap;
    }

    public static void setItemNbtCategory(ItemStack stack, ItemCategory category) {
        NBT.modify(stack, nbt -> {
            nbt.setEnum("category",category);
        });
    }

    public static ItemCategory getItemNbtCategory(ItemStack stack) {
        NBT.get(stack, nbt -> {
            return nbt.getEnum("category", ItemCategory.class);
        });
        return ItemCategory.ITEM_GENERIC;
    }

}
