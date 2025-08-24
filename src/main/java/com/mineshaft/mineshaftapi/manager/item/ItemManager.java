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
import com.mineshaft.mineshaftapi.manager.item.crafting.ItemDeconstructManager;
import com.mineshaft.mineshaftapi.manager.item.crafting.ItemRecipeManager;
import com.mineshaft.mineshaftapi.manager.item.fields.*;
import com.mineshaft.mineshaftapi.manager.player.ActionType;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.ToolRuleExtended;
import com.mineshaft.mineshaftapi.util.formatter.NumericFormatter;
import com.mineshaft.mineshaftapi.util.formatter.TextFormatter;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBTList;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import it.unimi.dsi.fastutil.chars.Char2CharArrayMap;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ItemManager {

    HashMap<UUID, String> items = new HashMap<>();
    HashMap<UUID, String> itemPaths = new HashMap<>();
    HashMap<UUID, List<String>> cachedEvents = new HashMap<>();

    public boolean isValidUUID(UUID uuid) {
        return items.containsKey(uuid);
    }

    public HashMap<UUID, String> getItemList() {
        return items;
    }

    public HashMap<UUID, String> getItemPathList() {
        return itemPaths;
    }

    String path = MineshaftApi.getInstance().getItemPath();


    public void initialiseItems() {
        items.clear();
        itemPaths.clear();

        File folder = getFolder();

        if (folder.listFiles() == null || Objects.requireNonNull(folder.listFiles()).length == 0) {
            createDemoItem();
        }

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if(!file.isDirectory()) {
                initialiseItem(file.getName());
            } else {
                initialiseFilesInDirectory(getPath(), file.getName(), 0);
            }
        }
    }

    // Initialises files in a given directory
    // iteration is used to avoid an infinite loop
    public void initialiseFilesInDirectory(String path, String dirName, int iteration) {
        File folder = new File(path + File.separator + dirName);

        if(iteration>99) {
            Logger.logWarning("Went into subfolder in directory \""+path+"\\"+dirName+"\" more than 99 times. Returning to avoid infinite loop.");
            return;
        }

        for(File file : Objects.requireNonNull(folder.listFiles())) {
            if(file.isDirectory()) {
                initialiseFilesInDirectory(path+File.separator+dirName, file.getName(), iteration++);
            } else {
                initialiseItem(file.getName());
            }
        }
    }

    public void initialiseItem(String fileName) {
        if(items.containsValue(fileName)) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(items.containsKey(UUID.fromString(Objects.requireNonNull(yamlConfiguration.getString("id"))))) {
            Logger.logError("Duplicate id detected for items: " + fileName + " and " + items.get(UUID.fromString(yamlConfiguration.getString("id"))));
        }

        try {
            // Register item crafting recipe, if exists.
            ItemRecipeManager.registerRecipe(name);
            ItemDeconstructManager.registerMeltingRecipes(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        items.put(UUID.fromString(Objects.requireNonNull(yamlConfiguration.getString("id"))), name);
        itemPaths.put(UUID.fromString(yamlConfiguration.getString("id")), path);
        Logger.logInfo("Initialised item '" + name + "' with UUID '" + yamlConfiguration.getString("id") + "'");
    }

    public static String getItemPath(UUID uuid) {
        return MineshaftApi.getInstance().getItemManagerInstance().itemPaths.get(uuid);
    }

    public static String getItemName(UUID uuid) {
        return MineshaftApi.getInstance().getItemManagerInstance().items.get(uuid);
    }

    public static UUID getItemIdFromItem(ItemStack item) {
        final UUID[] uuid = {null};

        NBT.get(item, nbt -> {
            uuid[0] = UUID.fromString(nbt.getString("uuid"));
        });
        return uuid[0];
    }

    public static String getItemNameFromItem(ItemStack item) {
        UUID uuid = getItemIdFromItem(item);
        if (uuid != null) {
            return getItemName(uuid);
        }
        return null;
    }


    @SuppressWarnings({"deprecation","removal"})
    public ItemStack getItem(String itemName) {
        File fileYaml = new File(path, itemName + ".yml");

        // return null if file does not exist
        if (!fileYaml.exists()) {
            Logger.logError("Attempted to load null item: " + itemName + ".yml");
            return null;
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        // Whether the item has a parent item
        boolean hasParent = false;

        String uuid = yamlConfiguration.getString("id");

        ItemStack item = new ItemStack(Material.BARRIER);


        // Rarity
        ItemRarity rarity = ItemRarity.STANDARD;

        String itemDisplay = "Item";
        String parentItemDisplay = null;
        ItemSubcategory subcategory = ItemSubcategory.DEFAULT;

        if (yamlConfiguration.contains("parent")) {
            String parentName = yamlConfiguration.getString("parent");
            if (parentName != null && !parentName.equalsIgnoreCase("null") && !parentName.equalsIgnoreCase("nil")) {
                item = getItem(parentName);
                hasParent = true;
                File parent = new File(path, itemName + ".yml");
                if (parent.exists()) {
                    YamlConfiguration parentYaml = YamlConfiguration.loadConfiguration(parent);
                    if (parentYaml.getString("subcategory") != null) {
                        parentItemDisplay = TextFormatter.convertStringToName(parentYaml.getString("subcategory"));
                        subcategory = getItemSubcategory(parentYaml.getString("subcategory"));
                    }
                }
            }
        }


        if (yamlConfiguration.contains("material")) {
            try {
                item = new ItemStack(Material.valueOf(yamlConfiguration.getString("material").toUpperCase()));
            } catch (Exception e) {
                if (!hasParent) {
                    Logger.logError("ERROR! Could not load item '" + itemName + "' invalid material");
                    return null;
                }
                Logger.logWarning("ERROR! Could not load material for item: '" + itemName + "'. Invalid material. Using parent item material instead");
            }
        }

        // ItemMeta variable
        ItemMeta meta = item.getItemMeta();

        // Temporarily unused - will be used for stuff like AH sorting, item abilities, etc.
        ItemCategory category = ItemCategory.ITEM_GENERIC;


        // Item display name
        String displayName = "Custom Item";

        // Item stat values
        double maximum_dex_modifier = 0;
        double defence = 0;
        double speed = 0;
        double ranged_damage = 0;

        int durability = 0;

        int r = -1;
        int g = -1;
        int b = -1;

        String statsString = "stats";
        String rangedStatsString = "ranged_stats";

        boolean hideAttributes = true;

        final ArmourType armourType;

        List<String> itemProperties = new ArrayList<>();

        for (String field : yamlConfiguration.getKeys(false)) {
            switch (field) {
                case "rarity":
                    rarity = ItemRarity.valueOf(yamlConfiguration.getString("rarity").toUpperCase(Locale.ROOT));
                    break;
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
                case "subcategory":
                    subcategory = getItemSubcategory(yamlConfiguration.getString("subcategory"));
                default:
            }
        }

        boolean coldProtect = false;

        if (category == ItemCategory.ARMOUR_HELMET || category == ItemCategory.ARMOUR_BOOTS || category == ItemCategory.ARMOUR_CHESTPLATE || category == ItemCategory.ARMOUR_LEGGINGS) {
            if (yamlConfiguration.contains("armour.type")) {
                armourType = ArmourType.valueOf(yamlConfiguration.getString("armour.type"));
            } else if (yamlConfiguration.contains("armor.type")) {
                armourType = ArmourType.valueOf(yamlConfiguration.getString("armor.type"));
            } else {
                armourType = ArmourType.NONE;
            }
            if(yamlConfiguration.contains("armour.cold_protection")) {
                coldProtect=yamlConfiguration.getBoolean("armour.cold_protection");
            }
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
        } else {
            armourType = ArmourType.NONE;
        }

        List<String> ammunitionTypes = Collections.emptyList();
        int maxAmmunition = 0;

        if(category == ItemCategory.WEAPON_RANGED) {
            if(yamlConfiguration.contains("ammunition")) {

                for(String field : yamlConfiguration.getConfigurationSection("ammunition").getKeys(false)) {
                    switch(field) {
                        case "shot_count","shots" -> {
                            maxAmmunition = yamlConfiguration.getInt("ammunition."+field);
                        }
                        case "ammunition_types","ammo_types","ammunition_type","ammo_type" -> {
                            ammunitionTypes = yamlConfiguration.getStringList("ammunition."+field);
                        }
                    }
                }
            }
        }

        meta.setDisplayName(rarity.getColourCode() + displayName);

        // GENERATE LORE:
        ArrayList<String> lore = new ArrayList<>();

        if (rarity != ItemRarity.STANDARD) {

            if (category.equals(ItemCategory.WEAPON_MELEE) || category.equals(ItemCategory.WEAPON_RANGED)) {
                itemDisplay = "Weapon";
            } else if (category.equals(ItemCategory.ARMOUR_CHESTPLATE)) {
                itemDisplay = "Chestplate";
            } else if (category.equals(ItemCategory.ARMOUR_LEGGINGS)) {
                itemDisplay = "Leggings";
            } else if (category.equals(ItemCategory.ARMOUR_BOOTS)) {
                itemDisplay = "Boots";
            } else if (category.equals(ItemCategory.ARMOUR_HELMET)) {
                itemDisplay = "Helmet";
            } else if (category.equals(ItemCategory.TOOL_AXE)) {
                itemDisplay = "Axe";
            } else if (category.equals(ItemCategory.TOOL_PICKAXE)) {
                itemDisplay = "Pickaxe";
            } else if (category.equals(ItemCategory.TOOL_SHOVEL)) {
                itemDisplay = "Shovel";
            } else if (category.equals(ItemCategory.TOOL_HOE)) {
                itemDisplay = "Hoe";
            } else if (category.equals(ItemCategory.ITEM_CONSUMABLE)) {
                itemDisplay = "Consumable";
            }

            if (subcategory != null && !subcategory.equals(ItemSubcategory.DEFAULT)) {
                itemDisplay = TextFormatter.convertStringToName(subcategory.name().toLowerCase());
            } else if (parentItemDisplay != null) {
                itemDisplay = parentItemDisplay;
            }

            if (MineshaftApi.getInstance().getConfigManager().useItalicItemRarity()) {
                String italic = ChatColor.ITALIC.toString();
                lore.add(rarity.getSecondaryColourCode() + italic + rarity.getName() + " " + itemDisplay);
            } else {
                lore.add(rarity.getSecondaryColourCode() + rarity.getName() + " " + itemDisplay);
            }
            // Set rarity
        }
        if (!armourType.equals(ArmourType.NONE)) {
            lore.add(ChatColor.GRAY + armourType.getName());
            if(coldProtect) {
                lore.add(ChatColor.GRAY + "Frost Protection");
            }
        } else if(!rarity.equals(ItemRarity.STANDARD)) {
            if(coldProtect) {
                lore.add(ChatColor.GRAY + "Frost Protection");
            }

            // Item properties
        }
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
            lore.add(properties);
        }
        if(!rarity.equals(ItemRarity.STANDARD) || !armourType.equals(ArmourType.NONE) || coldProtect || !subcategory.getPropertyList().isEmpty()) {
            lore.add("");
        }


//        else {
//            if(coldProtect) {
//                lore.add(ChatColor.GRAY + "Frost Protection");
//                lore.add("");
//            }
//        }



        // Load file stats, append to lore and add them to the item

        // Get standard and ranged item statistics
        HashMap<ItemStats, Double> statMap = getStatMap(itemName, statsString);
        // TODO: Add in 1.21.5 when update comes out
        //HashMap<WeaponStats, Double> weaponStatMap = getWeaponStatMap(itemName, statsString);
        HashMap<RangedItemStats, Double> rangedStatMap = getRangedStatMap(itemName, rangedStatsString);

        EquipmentSlot slot = null;

        switch (category) {

            case WEAPON_MELEE:
                slot = EquipmentSlot.HAND;
                break;
            case WEAPON_RANGED:
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
            case TOOL_AXE:
            case TOOL_PICKAXE:
            case TOOL_SHOVEL:
            case TOOL_HOE:
                slot = EquipmentSlot.HAND;
                break;
            case ITEM_CONSUMABLE:
                slot = null;

                FoodComponent component = new ItemStack(Material.APPLE).getItemMeta().getFood();

                String path = "food.";

                for (String field : yamlConfiguration.getConfigurationSection("food").getKeys(false)) {
                    switch (field) {
                        case "saturation":
                            component.setSaturation((float) yamlConfiguration.getDouble(path + "saturation"));
                        case "nutrition":
                            component.setNutrition(yamlConfiguration.getInt(path + "nutrition"));
                        case "always_edible":
                            component.setCanAlwaysEat(yamlConfiguration.getBoolean(path + "always_edible"));
//                        case "eat_seconds":
//                            component.setEatSeconds((float) yamlConfiguration.getDouble(path + "eat_seconds"));
                    }
                }

                meta.setFood(component);
                break;
            case OTHER:
            case AMMUNITION:
            case ITEM_GENERIC:
                slot = null;
                break;
        }

        if(yamlConfiguration.contains("tool")) {
            try {
                ToolComponent toolComponent = meta.getTool();
                for (String field : yamlConfiguration.getConfigurationSection("tool").getKeys(false)) {
                    switch (field) {
                        case "damage_per_block":
                            toolComponent.setDamagePerBlock(yamlConfiguration.getInt("damage_per_block"));
                        case "mining_speed":
                            toolComponent.setDefaultMiningSpeed((float) yamlConfiguration.getDouble("mining_speed"));
                        case "block_rules":
                            for(ToolComponent.ToolRule rule : toolComponent.getRules()) {
                                toolComponent.removeRule(rule);
                            }

                            for (String key : yamlConfiguration.getConfigurationSection("block_list").getKeys(false)) {
                                String tempPath = "tool." + field + "." + "block_list." + key;
                                // Each block rule
                                List<ToolComponent.ToolRule> rules = List.of();
                                for(String f : yamlConfiguration.getConfigurationSection(tempPath).getKeys(false)) {
                                    ToolComponent.ToolRule toolRule = new ToolRuleExtended();

                                    // Get block rule parameters
                                    Tag<Tag> tag = Bukkit.getTag("minecraft",NamespacedKey.fromString(f.toUpperCase()),Tag.class);
                                    @NotNull Set<Tag> mat = tag.getValues();
                                    List<Material> materials = Collections.EMPTY_LIST;
                                    mat.stream().map(t -> Material.valueOf(String.valueOf(t))).forEach(materials::add);
                                    toolRule.setBlocks(materials);

                                    if(yamlConfiguration.contains(tempPath + "." + f + ".blocks")) {

                                    }
                                    if(yamlConfiguration.contains(tempPath + "." + f + ".correct_for_drops")) {
                                        toolRule.setCorrectForDrops(yamlConfiguration.getBoolean((tempPath + "." + f + ".correct_for_drops")));
                                    }
                                    if(yamlConfiguration.contains(tempPath + "." + f + ".mining_speed")) {
                                        toolRule.setSpeed((float) yamlConfiguration.getDouble(tempPath + "." + f + ".mining_speed"));
                                    }
                                    rules.add(toolRule);
                                }
                                toolComponent.setRules(rules);
                            }
                        case "tool_type":

                    }
                    meta.setTool(toolComponent);
                }
            } catch (NullPointerException e) {
                Logger.logError("Error. Could not load tool properties for " + itemName);
            }
        }


        int lowestPriority = 100;
        int highestPriority= 0;

        for (ItemStats stat : statMap.keySet()) {
            double value = statMap.get(stat);

            // If an item has an attack speed modifier, the attack speed is 4 + the modifier.

            if (stat.equals(ItemStats.ATTACK_SPEED)) {
                value = -1 * (4 - value);
            }


            AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID().toString(), value, AttributeModifier.Operation.ADD_NUMBER);
            if (slot != null) {
                attributeModifier = new AttributeModifier(UUID.randomUUID(), UUID.randomUUID().toString(), value, AttributeModifier.Operation.ADD_NUMBER, slot);
            }
            if(stat.equals(ItemStats.DAMAGE)) {
                attributeModifier = new AttributeModifier(UUID.randomUUID().toString(), value-1, AttributeModifier.Operation.ADD_NUMBER);
                if (slot != null) {
                    attributeModifier = new AttributeModifier(UUID.randomUUID(), UUID.randomUUID().toString(), value-1, AttributeModifier.Operation.ADD_NUMBER, slot);
                }
            }

            if(stat.getPriority()<lowestPriority) lowestPriority=stat.getPriority();
            if(stat.getPriority()>highestPriority) highestPriority=stat.getPriority();

            switch (stat) {
                case DAMAGE:
                    meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, attributeModifier);
                    break;
                case MAXIMUM_ADDED_DEX_MODIFIER:
                    // For use with MineshaftRpg only
                    // Does nothing on its own
                    maximum_dex_modifier = value;
                    break;
                case ARMOUR:
                    meta.addAttributeModifier(Attribute.ARMOR, attributeModifier);
                    break;
                case ARMOUR_CLASS:
                    defence = value;
                    break;
                case SPEED:
                    speed = value;
                    break;
                case RANGED_DAMAGE:
                    ranged_damage = value;
                    break;
                case HEALTH:
                    meta.addAttributeModifier(Attribute.MAX_HEALTH, attributeModifier);
                    break;
                case ATTACK_REACH:
                    meta.addAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE, attributeModifier);
                    break;
                case MINING_REACH:
                    meta.addAttributeModifier(Attribute.BLOCK_INTERACTION_RANGE, attributeModifier);
                    break;
                case REACH:
                    meta.addAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE, attributeModifier);
                    meta.addAttributeModifier(Attribute.BLOCK_INTERACTION_RANGE, attributeModifier);
                    break;
                case ATTACK_SPEED:
                    meta.addAttributeModifier(Attribute.ATTACK_SPEED, attributeModifier);
                    break;
                case ATTACK_KNOCKBACK:
                    meta.addAttributeModifier(Attribute.ATTACK_KNOCKBACK, attributeModifier);
                    break;
                case SNEAKING_SPEED:
                    meta.addAttributeModifier(Attribute.SNEAKING_SPEED, attributeModifier);
                    break;
                case MINING_SPEED:
                    meta.addAttributeModifier(Attribute.BLOCK_BREAK_SPEED, attributeModifier);
                    break;
                default:
            }
        }

        boolean hasStats = false;

        if(lowestPriority<0) lowestPriority=0;

        for(int i = lowestPriority; i<=highestPriority; i++) {
            for(ItemStats stat : statMap.keySet()) {
                if (i == stat.getPriority()) {
                    hasStats = true;
                    if (stat.equals(ItemStats.ARMOUR_CLASS) && statMap.get(stat)!=0) {
                        lore.add(getStatString(stat, statMap.get(stat), category, (int) maximum_dex_modifier));

                    } else if(statMap.get(stat)!=0) {
                        lore.add(getStatString(stat, statMap.get(stat), category, 0));
                    }
                }
            }
        }

        if(!rangedStatMap.isEmpty()) {
            lore.add("");
        }

        // get ranged stat strings
//        Logger.logInfo(rangedStatMap.toString());
        for(int i = lowestPriority; i<=highestPriority; i++) {
            for(RangedItemStats stat : rangedStatMap.keySet()) {
                if (i == stat.getPriority() && statMap.get(stat)!=null) {
                    lore.add(getRangedStatString(stat, statMap.get(stat)));
                }
            }
        }

        if(!rangedStatMap.isEmpty()) {
            lore.add("");
        }

        if(maxAmmunition>0) {
            lore.add(getAmmunitionString(maxAmmunition,maxAmmunition));
        }

        // Hide attributes
        if (hideAttributes) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        meta.setLore(lore);

        /**
         * Custom equipment
         */

        if(slot!=null && yamlConfiguration.contains("armour")) {
            EquippableComponent equippableComponent = meta.getEquippable();
            if(equippableComponent==null) {equippableComponent=new ItemStack(Material.IRON_CHESTPLATE).getItemMeta().getEquippable();}

            equippableComponent.setSlot(slot);
            for(String key : yamlConfiguration.getConfigurationSection("armour").getKeys(false)) {
                String path = "armour."+key;
                switch (key) {
                    case "equip_sound":
                        equippableComponent.setEquipSound(Sound.valueOf(yamlConfiguration.getString(path)));
                        break;
                    case "model":
                        equippableComponent.setModel(NamespacedKey.minecraft(yamlConfiguration.getString(path)));
                        break;
                    case "damage_on_hurt":
                        equippableComponent.setDamageOnHurt(yamlConfiguration.getBoolean(path));
                        break;
                    default:
                        break;
                }
            }
            meta.setEquippable(equippableComponent);
        }

        // IMPORTANT!
        // set item meta. no meta modification after here!!!!!!!
        item.setItemMeta(meta);

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
        final boolean finalColdProtect = coldProtect;
        ItemSubcategory finalSubcategory1 = subcategory;
        int finalMaxAmmunition = maxAmmunition;
        List<String> finalAmmunitionTypes = ammunitionTypes;
        NBT.modify(item, nbt -> {
            nbt.setString("uuid", uuid);
            if(!armourType.equals(ArmourType.NONE)) {
                nbt.setString("ArmourType",armourType.name().toLowerCase());
            }
            // More are available! Ask your IDE, or see Javadoc for suggestions!
            nbt.setBoolean("ColdProtection", finalColdProtect);

            if(finalMaxAmmunition>0) {
                nbt.setInteger("ammunition", finalMaxAmmunition);
                if(finalAmmunitionTypes.contains("ammunition_power_cell")) {
                    nbt.setString("ammunition_type", "ammunition_power_cell");
                } else {
                    nbt.setString("ammunition_type", finalAmmunitionTypes.get(0));
                }
            }
        });

        // Custom stats.
        if (speed != 0) {
            setItemNbtStat(item, ItemStats.SPEED, speed);
        }
        if (defence != 0) {
            setItemNbtStat(item, ItemStats.ARMOUR_CLASS, defence);
        }
        if (maximum_dex_modifier != 0) {
            setItemNbtStat(item, ItemStats.MAXIMUM_ADDED_DEX_MODIFIER, maximum_dex_modifier);
        }
        if(ranged_damage!=0) {
            setItemNbtStat(item, ItemStats.RANGED_DAMAGE, ranged_damage);
        }

        for(RangedItemStats stat : rangedStatMap.keySet()) {
            setItemNbtRangedStat(item, stat, rangedStatMap.get(stat));
        }

        // Set rarity tag
        ItemRarity finalRarity = rarity;
        NBT.modify(item, nbt -> {
            nbt.setString("rarity", finalRarity.toString());
        });

        if(yamlConfiguration.contains("consumable")) {
            String path = "consumable.";
            ArrayList<ConsumeEffect> effects = new ArrayList<>();
            HashMap<PotionEffect, Float> potionEffects = new HashMap<>();
            ItemUseAnimation animation = ItemUseAnimation.EAT;
            float eatSeconds = 1.0f;
            boolean consumeParticles = true;
            for (String key : yamlConfiguration.getConfigurationSection("consumable").getKeys(false)) {
                switch (key) {
                    case "consume_seconds":
                        eatSeconds = (float) yamlConfiguration.getDouble(path + "eat_seconds");
                        break;
                    case "animation":
                        animation = ItemUseAnimation.valueOf(yamlConfiguration.getString(path + "animation"));
                        break;
                    case "has_consume_particles":
                        consumeParticles = yamlConfiguration.getBoolean("has_consume_particles");
                        break;
                    case "consume_sound":
                        // TODO: Add consume sound
                        break;
                    case "potion_effects":
                        for (String effectName : yamlConfiguration.getConfigurationSection(path + "potion_effects").getKeys(false)) {
                            if(effectName.equalsIgnoreCase("clear")) {
                                effects.add(ConsumeEffect.clearAllStatusEffects());
                            }
                            String tempPath = path + "potion_effects." + effectName + ".";
                            PotionEffectType potionEffectType = PotionEffectType.getByName(effectName.toUpperCase());
                            int duration = 20 * 60;
                            int amplifier = 0;
                            float effectProbability = 1.0f;
                            boolean ambient = false;
                            boolean particles = false;
                            boolean icon = true;
                            for (String parameter : yamlConfiguration.getConfigurationSection(path + "potion_effects." + effectName).getKeys(false)) {
                                switch (parameter) {
                                    case "probability":
                                        effectProbability = (float) yamlConfiguration.getDouble(tempPath + "probability");
                                        break;
                                    case "duration":
                                        duration = yamlConfiguration.getInt(tempPath + "duration");
                                    case "amplifier":
                                        amplifier = yamlConfiguration.getInt(tempPath + "amplifier");
                                    case "ambient":
                                        ambient = yamlConfiguration.getBoolean(tempPath + "ambient");
                                    case "particles":
                                        particles = yamlConfiguration.getBoolean(tempPath + "particles");
                                    case "icon":
                                        icon = yamlConfiguration.getBoolean(tempPath + "icon");
                                }
                            }
                            potionEffects.put(new PotionEffect(potionEffectType,duration,amplifier,ambient,particles,icon),effectProbability);
                        }
                        for(PotionEffect eff : potionEffects.keySet()) {
                            effects.add(ConsumeEffect.applyStatusEffects(Collections.singletonList(eff),potionEffects.get(eff)));
                        }

                    default:
                        break;
                }
            }

            // Add consumable
            Consumable consumable = Consumable.consumable().consumeSeconds(eatSeconds).hasConsumeParticles(consumeParticles).animation(animation).build();
            consumable.consumeEffects().addAll(effects);
            item.setData(DataComponentTypes.CONSUMABLE, consumable);
        }


        // Other properties
        final String rareString = rarity.name().toLowerCase();

        String finalSubcategory = subcategory.name().toLowerCase();
        List<String> finalItemProperties1 = itemProperties;
        NBT.modify(item, nbt -> {
            nbt.setString("rarity", rareString);
            nbt.setString("subCategory", finalSubcategory);

            // create the item property list
            ReadWriteNBTList<String> propertyList = nbt.getStringList("item_properties");
            propertyList.addAll(finalItemProperties1);
        });

        /**
         * Custom hardcoded properties
         * */

        if(getInteractEventsFromItem(itemName,ActionType.RIGHT_CLICK).contains("parry")) {
            Consumable consumable = Consumable.consumable().consumeSeconds(72000).hasConsumeParticles(false).animation(ItemUseAnimation.BLOCK).build();
            item.setData(DataComponentTypes.CONSUMABLE, consumable);
        } else if(getInteractEventsFromItem(itemName,ActionType.RIGHT_CLICK).contains("power_attack")) {
            Consumable consumable = Consumable.consumable().consumeSeconds(72000).hasConsumeParticles(false).animation(ItemUseAnimation.SPEAR).build();
            item.setData(DataComponentTypes.CONSUMABLE, consumable);
        } else if(getInteractEventsFromItem(itemName, ActionType.RIGHT_CLICK).contains("smoke_pipe") || getInteractEventsFromItem(itemName, ActionType.RIGHT_CLICK).contains("instrument")) {
            Consumable consumable = Consumable.consumable().consumeSeconds(72000).hasConsumeParticles(false).animation(ItemUseAnimation.TOOT_HORN).build();
            item.setData(DataComponentTypes.CONSUMABLE, consumable);
        } else if(getInteractEventsFromItem(itemName, ActionType.RIGHT_CLICK).contains("throw")) {
            Consumable consumable = Consumable.consumable().consumeSeconds(72000).hasConsumeParticles(false).animation(ItemUseAnimation.SPEAR).build();
            item.setData(DataComponentTypes.CONSUMABLE, consumable);
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

    protected HashMap<ItemStats, Double> getStatMap(String name, String statPath) {
        //System.out.println("getting statmap");

        HashMap<ItemStats, Double> statMap = new HashMap<>();

        String path = getPath();
        File fileYaml = getFileYaml(name);
        @NotNull YamlConfiguration yamlConfiguration = getYamlConfiguration(fileYaml);

        if (!yamlConfiguration.contains(statPath)) {
//            Logger.logError("could not find " + statPath + " in: " + path + "/" + name + ".yml");
            return statMap;
        }

        String yamlPath = statPath + ".";

        for (String key : yamlConfiguration.getConfigurationSection(statPath).getKeys(false)) {

            //System.out.println("section: " + key);

            String yamlStatPath = yamlPath + key;

            double value = yamlConfiguration.getDouble(yamlStatPath);
            ItemStats statKey = ItemStats.valueOf(key.toUpperCase(Locale.ROOT));

            if (statKey != null && !statKey.equals(ItemStats.NULL)) {
                statMap.put(statKey, value);
            }
        }
        return statMap;
    }

    protected HashMap<WeaponStats, Double> getWeaponStatMap(String name, String statPath) {
        //System.out.println("getting statmap");

        HashMap<WeaponStats, Double> statMap = new HashMap<>();

        String path = getPath();
        File fileYaml = getFileYaml(name);
        @NotNull YamlConfiguration yamlConfiguration = getYamlConfiguration(fileYaml);

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


    protected HashMap<RangedItemStats, Double> getRangedStatMap(String name, String rangedStatPath) {
        HashMap<RangedItemStats, Double> statMap = new HashMap<>();

        String path = getPath();
        File fileYaml = getFileYaml(name);
        @NotNull YamlConfiguration yamlConfiguration = getYamlConfiguration(fileYaml);

        if (!yamlConfiguration.contains(rangedStatPath)) {
//            Logger.logError("could not find " + rangedStatPath + " in: " + path + "/" + name + ".yml");
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

    protected static String getStatString(ItemStats stat, Double value, ItemCategory category, int arg) {
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

    protected static String getRangedStatString(RangedItemStats stat, Double value) {
        if(value==null||value<0) value=0d;
        return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value);
    }

    public static String getAmmunitionString(int ammunition, int maxAmmunition) {
        return (ChatColor.WHITE + "Ammunition: " + ChatColor.GREEN + ammunition + ChatColor.DARK_GRAY + "/" + maxAmmunition);
    }

    protected static void setItemNbtStat(ItemStack stack, ItemStats stat, double value) {
        NBT.modify(stack, nbt -> {
            nbt.setDouble("stat." + stat.name().toLowerCase(Locale.ROOT), value);
        });
    }

    protected static void setItemNbtRangedStat(ItemStack stack, RangedItemStats stat, double value) {
        NBT.modify(stack, nbt -> {
            nbt.setDouble("ranged_stat." + stat.name().toLowerCase(Locale.ROOT), value);
        });
    }

    public static double getMaximumDexterityModifier(ItemStack stack) {
       if(stack==null) {
           return -9999;
       }
        final double[] value = {0};
        NBT.get(stack, nbt -> {
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
        File fileYaml = new File(getItemPath(uuid), getItemName(uuid) + ".yml");

        // return null if file does not exist
        if (!fileYaml.exists()) return null;

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        if(yamlConfiguration.contains("item_category")&&yamlConfiguration.getString("item_category")!=null) {
            return ItemCategory.valueOf(yamlConfiguration.getString("item_category").toUpperCase(Locale.ROOT));
        }
        return ItemCategory.ITEM_GENERIC;
    }


    // Get item subcategory from an item
    public static ItemSubcategory getItemSubcategory(ItemStack item) {
        if(item==null) return ItemSubcategory.DEFAULT;
        // If not null
        AtomicReference<ItemSubcategory> returnValue = new AtomicReference<>();
        AtomicReference<ItemSubcategory> returnValueOld = new AtomicReference<>();
        NBT.get(item, nbt->{
            returnValueOld.set(getItemSubcategory(nbt.getString("sub_category")));
            returnValue.set(getItemSubcategory(nbt.getString("subCategory")));
        });

        if(returnValue.get()==null) {
            return returnValueOld.get();
        } else {
            return returnValue.get();
        }
    }

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

    public static ArrayList<String> getInteractEventsFromItem(String name, ActionType actionType) {

        ArrayList<String> interactEvents = new ArrayList<>();

        File fileYaml = new File(getPath(), name + ".yml");

        // return null if file does not exist
        if (!fileYaml.exists()) {
            Logger.logError("FILE NOT FOUND ERROR!!!!!!!");
            return null;
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        // Whether the item has a parent item
        // TODO: fix this code

        String parent = yamlConfiguration.getString("parent");
        if(parent!=null && !parent.equalsIgnoreCase("null")) {
            List<String> parentEvents = getInteractEventsFromItem(parent, actionType);
            interactEvents.addAll(parentEvents);
        }

        String clickPath = "action.";

        if(yamlConfiguration.contains(clickPath + actionType.getClickPath())) {
            interactEvents.addAll(yamlConfiguration.getStringList(clickPath + actionType.getClickPath()));
        }

        return interactEvents;
    }

    public static String getPath() {
        return MineshaftApi.getInstance().getItemPath();
    };

    public static File getFolder() {
        File folder = new File(getPath());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    public static File getFileYaml(String name) {
        return new File(getPath(), name + ".yml");
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
}
