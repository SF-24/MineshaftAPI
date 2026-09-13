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
import com.mineshaft.mineshaftapi.manager.VariableTypeEnum;
import com.mineshaft.mineshaftapi.manager.item.cache.CachedItem;
import com.mineshaft.mineshaftapi.manager.item.configuration_fields.*;
import com.mineshaft.mineshaftapi.manager.item.crafting.ItemDeconstructManager;
import com.mineshaft.mineshaftapi.manager.item.crafting.ItemRecipeManager;
import com.mineshaft.mineshaftapi.manager.item.item_components.ItemAmmunitionManager;
import com.mineshaft.mineshaftapi.manager.item.item_components.LoreManager;
import com.mineshaft.mineshaftapi.manager.player.ActionType;
import com.mineshaft.mineshaftapi.util.Logger;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ItemManager {

    private static int getMaximumIterationConstant() {return 1;};

    HashMap<UUID, CachedItem> items = new HashMap<>();
    HashMap<String, ArrayList<UUID>> itemPackages = new HashMap<>();
//    HashMap<UUID, List<String>> cachedEvents = new HashMap<>();

    public boolean isValidUUID(UUID uuid) {
        return items.containsKey(uuid);
    }

    // Get an item list
    public HashMap<UUID, CachedItem> getItemList() {
        return items;
    }

    // Get a list of item names
    public HashMap<UUID, String> getItemNameList() {
        HashMap<UUID, String> returnMap = new HashMap<>();
        for(UUID itemId : items.keySet()) {
            returnMap.put(itemId, items.get(itemId).getName());
        }
        return returnMap;
    }

    // Check if an item is a duplicate
    public boolean itemNameExists(String itemPackage, String item) {
        return getItemNameList().containsValue(item);
    }

    // get the default item saving path
    private static final String defaultPath = MineshaftApi.getItemPath();

    // Cache an item
    public void cacheItem(UUID itemId, String folder, CachedItem item) {
        if (itemPackages.containsKey(folder)) {
            ArrayList<UUID> items = itemPackages.get(folder);
            items.add(itemId);
            itemPackages.put(folder, items);
        } else {
            ArrayList<UUID> items = new ArrayList<>();
            items.add(itemId);
            itemPackages.put(folder,items);
        }
        items.put(itemId,item);
    }

    // Load an item
    public void initialiseItems() {
        items.clear();
        itemPackages.clear();

        File folder = getFolder();

        if (folder.listFiles() == null || Objects.requireNonNull(folder.listFiles()).length == 0) {
            createDemoItem();
        }

        // Iterate
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if(!file.isDirectory()) {
                initialiseItem(null,defaultPath,file.getName());
            } else {
                initialiseFilesInDirectory(defaultPath, file.getName(), 0);
            }
        }
    }

    // Load recipes for items
    public void initialiseItemRecipes() {
        for(UUID uniqueId : items.keySet()) {
            String name = items.get(uniqueId).getName();

            try {
                // Register item crafting recipe, if exists.
                ItemRecipeManager.registerRecipe(name);
                ItemDeconstructManager.registerMeltingRecipes(name);
                Logger.logInfo("Initialised recipe for item: " + name);
            } catch (Exception e) {
                Logger.logWarning("Initialising recipe failed for item: " + name);
            }

        }
    }

    // Initialises files in a given directory
    // iteration is used to avoid an infinite loop
    public void initialiseFilesInDirectory(String path, String dirName, int iteration) {
        path = path + File.separator + dirName;
        File folder = new File(path);

        if(iteration>=getMaximumIterationConstant()) {
            Logger.logWarning("Went into subfolder in directory \""+path+"\" a greater number of times than the maximum number (" + getMaximumIterationConstant() + ") times. Returning to avoid infinite loop.");
            return;
        }

        for(File file : Objects.requireNonNull(folder.listFiles())) {
            if(file.isDirectory()) {
                initialiseFilesInDirectory(path, file.getName(), iteration++);
            } else {
                // Initialise files in the directory
                initialiseItem(file.getParentFile().getName(),path,file.getName());
            }
        }
    }

    public void initialiseItem(String filePackage, String path, String fileName) {
        if(itemNameExists(filePackage,fileName)) {
            Logger.logWarning("Conflicting item names: '" + fileName + "'. This may result in errors due to items containing the same name. This may be fixed in a future release.");
        }

        File fileYaml = new File(path, fileName);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        String name = fileName.substring(0, fileName.lastIndexOf('.'));

        if (!yamlConfiguration.contains("id")) {
            yamlConfiguration.createSection("id");
            yamlConfiguration.set("id", UUID.randomUUID().toString());
            try {
                yamlConfiguration.save(fileYaml);
                Logger.logInfo("Saved file: " + path + "/" + fileName + ".yml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(items.containsKey(UUID.fromString(Objects.requireNonNull(yamlConfiguration.getString("id"))))) {
            Logger.logError("Duplicate id detected for items: " + fileName + " and " + items.get(UUID.fromString(yamlConfiguration.getString("id"))));
            Logger.logError("Aborting loading item: '" + name + ".yml' in package: '" + filePackage + "'");
            return;
        }

        // Cache item:
        cacheItem(UUID.fromString(Objects.requireNonNull(yamlConfiguration.getString("id"))),filePackage,new CachedItem(name, filePackage, yamlConfiguration));

        Logger.logInfo("Initialised item ' " + filePackage + ":" + name + "', " /* + "UUID '" + yamlConfiguration.getString("id") + "'"*/);
    }

    public static String getItemName(UUID uuid) {
        return MineshaftApi.getInstance().getItemManagerInstance().items.get(uuid).getName();
    }

    public static UUID getItemIdFromItem(ItemStack item) {
        final UUID[] uuid = {null};

        try {
            NBT.get(item, nbt -> {
                uuid[0] = UUID.fromString(nbt.getString("uuid"));
            });
        } catch (IllegalArgumentException ignored) {} // Ignore the stack trace
        return uuid[0];
    }

    public static String getItemNameFromItem(ItemStack item) {
        UUID uuid = getItemIdFromItem(item);
        if (uuid != null) {
            return getItemName(uuid);
        }
        return null;
    }

    public ItemStack getItem(String name) {
        return getItem(getUuid(name));
    }

    public ItemStack getItem(UUID uuid) {
        return ItemBuilder.getItem(
                items.get(uuid).getName(),
                getItemDefinition(uuid));
    }

    // Generate the default item
    public static void createDemoItem() {
        MineshaftApi.getInstance();
        String path = MineshaftApi.getItemPath();
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

            for (ItemFields itemFields : ItemFields.values()) {
                yamlConfiguration.createSection(itemFields.name().toLowerCase(Locale.ROOT));
                if (!itemFields.getVariableType().equals(VariableTypeEnum.LIST)) {
                    yamlConfiguration.set(itemFields.name().toLowerCase(Locale.ROOT), itemFields.getDefaultValue());
                }
            }

            // Create stats section
            yamlConfiguration.createSection("stats.damage");
            yamlConfiguration.set("stats.damage", 5);

            // Save demo item
            try {
                yamlConfiguration.save(fileYaml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static HashMap<ItemStats, Double> getStatMap(ConfigurationSection yamlConfiguration, String statPath) {
        //System.out.println("getting statmap");

        HashMap<ItemStats, Double> statMap = new HashMap<>();

        if (!yamlConfiguration.contains(statPath)) {
//            Logger.logError("could not find " + statPath + " in: " + path + "/" + name + ".yml");
            return statMap;
        }

        String yamlPath = statPath + ".";

        for (String key : yamlConfiguration.getConfigurationSection(statPath).getKeys(false)) {

            //System.out.println("section: " + key);

            String yamlStatPath = yamlPath + key;

            if(!yamlConfiguration.contains(yamlStatPath)) {
                // The value cannot be found. Continue.
                continue;
            }

            double value = yamlConfiguration.getDouble(yamlStatPath);
            ItemStats statKey = ItemStats.valueOf(key.toUpperCase(Locale.ROOT));

            if (statKey != null && !statKey.equals(ItemStats.NULL)) {
                statMap.put(statKey, value);
            }
        }
        return statMap;
    }

    public static HashMap<ItemStats, Double> getItemStatMap(ItemStack itemStack) {
        //System.out.println("getting statmap");

        HashMap<ItemStats, Double> statMap = new HashMap<>();

        for(ItemStats itemStats : ItemStats.values()) {
            if(getItemNbtStat(itemStack,itemStats)>0) {
                statMap.put(itemStats,getItemNbtStat(itemStack,itemStats));
            }
        }
        return statMap;
    }

    public static HashMap<RangedItemStats, Double> getRangedItemStatMap(ItemStack itemStack) {
        //System.out.println("getting statmap");

        HashMap<RangedItemStats, Double> statMap = new HashMap<>();

        for(RangedItemStats itemStats : RangedItemStats.values()) {
            if(getItemNbtRangedStat(itemStack,itemStats)>0) {
                statMap.put(itemStats,getItemNbtRangedStat(itemStack,itemStats));
            }
        }
        return statMap;
    }

    protected HashMap<WeaponStats, Double> getWeaponStatMap(String name, String statPath) {
        return getWeaponStatMap(getItemDefinition(name),statPath);
    }

    protected HashMap<WeaponStats, Double> getWeaponStatMap(ConfigurationSection yamlConfiguration, String statPath) {
        //System.out.println("getting statmap");

        HashMap<WeaponStats, Double> statMap = new HashMap<>();

        if (!yamlConfiguration.contains(statPath)) {
//            Logger.logError("could not find " + statPath + " in: " + path + "/" + name + ".yml");
            return statMap;
        }

        String yamlPath = statPath + ".";

        for (String key : yamlConfiguration.getConfigurationSection(statPath).getKeys(false)) {

            //System.out.println("section: " + key);

            String yamlStatPath = yamlPath + key;

            double value = yamlConfiguration.getDouble(yamlStatPath);
            WeaponStats statKey = WeaponStats.valueOf(key.toUpperCase(Locale.ROOT));

            if (!statKey.equals(WeaponStats.NULL)) {
                statMap.put(statKey, value);
            }
        }
        return statMap;
    }

    public static HashMap<RangedItemStats, Double> getRangedStatMap(ConfigurationSection yamlConfiguration, String rangedStatPath) {
        HashMap<RangedItemStats, Double> statMap = new HashMap<>();

        if (yamlConfiguration==null || !yamlConfiguration.contains(rangedStatPath)) {
            return statMap;
        }

        String yamlPath = rangedStatPath + ".";

        for (String key : yamlConfiguration.getConfigurationSection(rangedStatPath).getKeys(false)) {
            String yamlStatPath = yamlPath + key;

            double value = yamlConfiguration.getDouble(yamlStatPath);
            RangedItemStats statKey = RangedItemStats.valueOf(key.toUpperCase(Locale.ROOT));

            // Null check - likely not needed
            if (statKey != null && !statKey.equals(RangedItemStats.NULL)) {
                statMap.put(statKey, value);
            }
        }
        return statMap;
    }

    public static void setCustomItemAttribute(ItemStack stack, ItemStats stat, double value) {
        NBT.modify(stack, nbt -> {
            nbt.setDouble("stat." + stat.name().toLowerCase(Locale.ROOT), value);
        });
    }

    public static void setCustomItemAttribute(ItemStack stack, RangedItemStats stat, double value) {
        NBT.modify(stack, nbt -> {
            nbt.setDouble("ranged_stat." + stat.getName().toLowerCase(Locale.ROOT), value);
        });
    }

    public static double getMaximumDexterityModifier(ItemStack stack) {
       if(stack==null || stack.getType()==Material.AIR || stack.getAmount()==0 || stack.isEmpty()) {
           return -9999;
       }
        final double[] value = {0};
        de.tr7zw.nbtapi.NBT.get(stack, nbt -> {
            if (!nbt.hasNBTData()) {
                value[0] = -8888;
            } else {
                value[0] = nbt.getDouble("stat." + ItemStats.MAXIMUM_ADDED_DEX_MODIFIER.name().toLowerCase(Locale.ROOT));
            }
        });
        return value[0];
    }

    public static double getItemNbtStat(ItemStack stack, ItemStats stat) {
        if(stack==null) {return 0;}
        final double[] value = {0};
        try {
            NBT.get(stack, nbt -> {
                if (!nbt.hasNBTData()) {
                    value[0] = 0;
                } else {
                    value[0] = nbt.getDouble("stat." + stat.name().toLowerCase(Locale.ROOT));
                }
            });
        } catch (NullPointerException e) {
            return 0;
        }
        return value[0];
    }

    public static double getItemNbtRangedStat(ItemStack stack, RangedItemStats stat) {
        final double[] value = {0};
        NBT.get(stack, nbt -> {
            value[0] = nbt.getDouble("ranged_stat." + stat.name().toLowerCase(Locale.ROOT));
        });
        return value[0];
    }

    // Get item category from an item
    public static ItemCategory getItemCategory(ItemStack item) {
        if(item==null) return ItemCategory.ITEM_GENERIC;
        // If not null
        AtomicReference<ItemSubcategory> returnValue = new AtomicReference<>();
        NBT.get(item, nbt->{
            return getItemCategory(UUID.fromString(nbt.getString("uuid")));
        });
        return ItemCategory.ITEM_GENERIC;
    }

    public static ItemCategory getItemCategory(UUID uuid) {
        return getItemCategory(MineshaftApi.getInstance().getItemManagerInstance().getItemDefinition(uuid));
    }

    public static ItemCategory getItemCategory(ConfigurationSection yamlConfiguration) {
        if(yamlConfiguration.contains("item_category")&&yamlConfiguration.getString("item_category")!=null) {
            return ItemCategory.valueOf(yamlConfiguration.getString("item_category").toUpperCase(Locale.ROOT));
        }
        return ItemCategory.ITEM_GENERIC;
    }

    public static ItemRarity getItemRarity(UUID uniqueId) {
        return getItemRarity(ItemManager.getItemDefinition(uniqueId));
    }

    // Get the rarity of an item.
    public static ItemRarity getItemRarity(ConfigurationSection yamlConfiguration) {
        if(yamlConfiguration==null) {
            Logger.logError("The given UUID is null for this item. Returning");
            return MineshaftApi.getInstance().getConfigManager().getDefaultCustomItemRarity();
        }

        if (yamlConfiguration.contains("rarity")) {
            return ItemRarity.valueOf(yamlConfiguration.getString("rarity").toUpperCase(Locale.ROOT));
        }
        return ItemRarity.STANDARD;

    }

    // Get item subcategory from an item
    public static ItemSubcategory getItemSubcategory(ItemStack item) {
        if(item==null) return ItemSubcategory.DEFAULT;
        // If not null
        AtomicReference<ItemSubcategory> returnValue = new AtomicReference<>();
        AtomicReference<ItemSubcategory> returnValueOld = new AtomicReference<>();
        AtomicReference<ItemSubcategory> returnValueOld2 = new AtomicReference<>();
        NBT.get(item, nbt->{
            returnValueOld.set(getItemSubcategory(nbt.getString("sub_category")));
            returnValueOld2.set(getItemSubcategory(nbt.getString("subCategory")));
            returnValue.set(getItemSubcategory(nbt.getString("subcategory")));
        });

        if(returnValue.get()==null) {
            if(returnValueOld.get()==null) {
                return returnValueOld2.get();
            } else {
                return returnValueOld.get();
            }
        } else {
            return returnValue.get();
        }
    }

    public static ItemSubcategory getItemSubcategory(UUID uniqueId) {
        return getItemSubcategory(ItemManager.getItemDefinition(uniqueId));
    }

    public static ItemSubcategory getItemSubcategory(ConfigurationSection yamlConfiguration) {
        if(yamlConfiguration==null) {
            Logger.logError("Found null item YAML!");
            return ItemSubcategory.DEFAULT;
        }
        if(yamlConfiguration.contains("subcategory")) {
            return getItemSubcategory(yamlConfiguration.getString("subcategory"));
        } else {
            if (yamlConfiguration.contains("parent")) {
                String parentName = yamlConfiguration.getString("parent");
                if (parentName != null && !parentName.equalsIgnoreCase("null") && !parentName.equalsIgnoreCase("nil")) {
                    ConfigurationSection parentYamlConfiguration = ItemManager.getItemDefinition(getUuid(parentName));
                    if (parentYamlConfiguration!=null) {
                        if (parentYamlConfiguration.getString("subcategory") != null) {
                            return getItemSubcategory(parentYamlConfiguration.getString("subcategory"));
                        }
                    } else {
                        return ItemSubcategory.DEFAULT;
                    }
                }
            }

        }
        return ItemSubcategory.DEFAULT;
    }

    public static @NotNull String getItemSubcategoryOverride(ConfigurationSection yamlConfiguration) {
        if(yamlConfiguration.contains("subcategory_override")&&yamlConfiguration.getString("subcategory_override")!=null&&!yamlConfiguration.getStringList("subcategory_override").isEmpty()) {
            return (yamlConfiguration.getString("subcategory_override")).toLowerCase();
        } else if (yamlConfiguration.contains("parent")) {
            String parentName = yamlConfiguration.getString("parent");
            if (parentName != null && !parentName.equalsIgnoreCase("null") && !parentName.equalsIgnoreCase("nil")) {
                ConfigurationSection parentYamlConfiguration = ItemManager.getItemDefinition(parentName);
                if (parentYamlConfiguration!=null) {
                    if (parentYamlConfiguration.contains("subcategory") && parentYamlConfiguration.getString("subcategory_override")!=null && !parentYamlConfiguration.getString("subcategory_override").isEmpty()) {
                        return (parentYamlConfiguration.getString("subcategory_override")).toLowerCase();
                    }
                }
            }
        }
        return getItemSubcategory(yamlConfiguration).name().toLowerCase();
    }

    public static ConfigurationSection getParentYamlConfiguration(UUID uniqueId) {
        return getParentYamlConfiguration(ItemManager.getItemDefinition(uniqueId));
    }

    public static ConfigurationSection getParentYamlConfiguration(ConfigurationSection yamlConfiguration) {
        if (yamlConfiguration.contains("parent")) {
            if (yamlConfiguration.getString("parent") != null && !(yamlConfiguration.getString("parent").equalsIgnoreCase("null")) && !(yamlConfiguration.getString("parent").equalsIgnoreCase("nil"))) {
                return getItemDefinition(yamlConfiguration.getString("parent"));
            }
        }
        return null;
    }

    public static @Nullable String getParentName(ConfigurationSection yamlConfiguration) {
        if (yamlConfiguration.contains("parent")) {
            if (yamlConfiguration.getString("parent") != null && !(yamlConfiguration.getString("parent").equalsIgnoreCase("null")) && !(yamlConfiguration.getString("parent").equalsIgnoreCase("nil"))) {
                return (yamlConfiguration.getString("parent"));
            }
        }
        return null;
    }

    public static UUID getUuid(String name) {
        for(UUID uuid : MineshaftApi.getInstance().getItemManagerInstance().getItemNameList().keySet()) {
            if(MineshaftApi.getInstance().getItemManagerInstance().getItemNameList().get(uuid).equals(name)) {
                return uuid;
            }
        }
        return null;
    }

//    @Deprecated
//    public static YamlConfiguration getYamlConfiguration(String fileName) {
//        // TODO: Get path
//        if(fileName==null) {
//            Logger.logError("Found null filename when loading item!");
//            return null;
//        }
////        if(!fileName.contains(".yml") || !fileName.contains(".yaml")) fileName += ".yml";
//        return getYamlConfiguration(MineshaftApi.getItemPath(), fileName);
//    }
//
//    @Deprecated
//    public static YamlConfiguration getYamlConfiguration(String path, String itemName) {
//        if(itemName==null || itemName.isEmpty() || itemName.contains("null")) return null;
//        if(path.isBlank()) {
//            Logger.logWarning("Found empty path for file " + itemName + ".yml, using default path" );
//            path=MineshaftApi.getItemPath();
//        }
//        File fileYaml = new File(path, itemName+".yml");
//        if(!fileYaml.exists()) {
//            Logger.logError("Tried to load non-existent file: " + path + "/" + fileYaml.getName());
//            return null;
//        }
//        return YamlConfiguration.loadConfiguration(fileYaml);
//    }

    public static List<String> getItemPropertiesAsString(ItemStack item) {
        if(item==null) return Collections.emptyList();
        NBT.get(item, nbt -> {
            return nbt.getStringList("item_properties");
        });
        return Collections.emptyList();
    }

    public static List<ItemProperties> getItemProperties(ItemStack item) {
        if(item==null) return Collections.emptyList();
        List<ItemProperties> list = new ArrayList<>();
        for(String p : getItemPropertiesAsString(item)) {
            list.add(getItemProperty(p));
        }
        return list;
    }

    public static ItemProperties getItemProperty(String s) {
        for(ItemProperties p : ItemProperties.values()) {
            if(p.name().equalsIgnoreCase(s)) {
                return p;
            }
        }
        return null;
    }

    public static HashMap<ItemStats, Double> getItemNbtStats(ItemStack stack) {
        HashMap<ItemStats, Double> statMap = new HashMap<>();

        for (ItemStats stat : ItemStats.values()) {
            double value = getItemNbtStat(stack, stat);
            if (value != 0) statMap.put(stat, value);
        }
        return statMap;
    }

    public static void setItemNbtCategory(ItemStack stack, ItemCategory category) {
        NBT.modify(stack, nbt -> {
            nbt.setEnum("category", category);
        });
    }

    public static ItemCategory getItemNbtCategory(ItemStack stack) {
        if(stack==null||stack.getAmount()<1) return ItemCategory.ITEM_GENERIC;
        try {
            NBT.get(stack, nbt -> {
                return nbt.getEnum("category", ItemCategory.class);
            });
        } catch (NullPointerException e) {
            return ItemCategory.ITEM_GENERIC;
        }
        return ItemCategory.ITEM_GENERIC;
    }

    public static ArrayList<String> getInteractEventsFromItem(UUID uniqueId, ActionType actionType) {
        return getInteractEventsFromItem(getItemDefinition(uniqueId),actionType);
    }

    public static ArrayList<String> getInteractEventsFromItem(ConfigurationSection yamlConfiguration, ActionType actionType) {

        ArrayList<String> interactEvents = new ArrayList<>();

        // Whether the item has a parent item
        // TODO: fix this code

        if(yamlConfiguration.contains("parent")) {
            String parent = yamlConfiguration.getString("parent");
            if (parent != null && !parent.equalsIgnoreCase("null") && !parent.equalsIgnoreCase("nil")) {
                List<String> parentEvents = getInteractEventsFromItem(getUuid(parent), actionType);
                interactEvents.addAll(parentEvents);
            }
        }

        String clickPath = "action.";

        if(yamlConfiguration.contains(clickPath + actionType.getClickPath())) {
            interactEvents.addAll(yamlConfiguration.getStringList(clickPath + actionType.getClickPath()));
        }

        return interactEvents;
    }

    public static File getFolder() {
        File folder = new File(defaultPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    public static ConfigurationSection getItemDefinition(UUID uuid) {
        return MineshaftApi.getInstance().getItemManagerInstance().getItemList().get(uuid).getYamlSetup();
    }

    public static ConfigurationSection getItemDefinition(String name) {
        return getItemDefinition(getUuid(name));
    }

    public static @NotNull YamlConfiguration getYamlConfiguration(File fileYaml) {
        return YamlConfiguration.loadConfiguration(fileYaml);
    }

    public static ItemSubcategory getItemSubcategory(String subcategory) {
        ItemSubcategory returnValue = null;
        for(ItemSubcategory subcategoryItem : ItemSubcategory.values()) {
            if(subcategoryItem.name().toLowerCase().equalsIgnoreCase(subcategory)) {
                returnValue=subcategoryItem;
            }
        }
        return returnValue;
    }

    public static boolean useAmmunition(UUID uniqueId) {
        return useAmmunition(ItemManager.getItemDefinition(uniqueId));
    }

    public static boolean useAmmunition(ConfigurationSection yamlConfiguration) {
        if(ItemManager.getItemCategory(yamlConfiguration) == ItemCategory.WEAPON_RANGED) {
            return(yamlConfiguration.contains("ammunition"))&& ItemAmmunitionManager.getMaximumAmmunitionCapacityInWeapon(yamlConfiguration)>0;
        }
        return false;
    }

    public static void updateItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        // TODO: Update the properties from the equipped sockets
        item.setLore(LoreManager.getItemLoreArrayList(getItemIdFromItem(item),item));
        item.setItemMeta(meta);
    }

}
