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
import com.mineshaft.mineshaftapi.ToolRuleExtended;
import com.mineshaft.mineshaftapi.manager.ActionType;
import com.mineshaft.mineshaftapi.manager.ArmourType;
import com.mineshaft.mineshaftapi.manager.VariableTypeEnum;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemCategory;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemFields;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemRarity;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.NumericFormatter;
import com.mineshaft.mineshaftapi.util.TextFormatter;
import de.tr7zw.nbtapi.NBT;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
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
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ItemManager {

    HashMap<UUID, String> items = new HashMap<>();
    HashMap<UUID, List<String>> cachedEvents = new HashMap<>();

    public HashMap<UUID, String> getItemList() {
        return items;
    }

    String path = MineshaftApi.getInstance().getItemPath();

    public void initialiseItems() {
        items = new HashMap<>();

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
        items.put(UUID.fromString(Objects.requireNonNull(yamlConfiguration.getString("id"))), name);
        Logger.logInfo("Initialised item '" + name + "' with UUID '" + yamlConfiguration.getString("id") + "'");
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

    public String getItemNameFromItem(ItemStack item) {
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
        if (!fileYaml.exists()) return null;

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        // Whether the item has a parent item
        boolean hasParent = false;

        String uuid = yamlConfiguration.getString("id");

        ItemStack item = new ItemStack(Material.BARRIER);


        // Rarity
        ItemRarity rarity=ItemRarity.STANDARD;

        String itemDisplay = "Item";
        String parentItemDisplay = null;

        if (yamlConfiguration.contains("parent")) {
            String parentName = yamlConfiguration.getString("parent");
            if (parentName != null && !parentName.equalsIgnoreCase("null") && !parentName.equalsIgnoreCase("nil")) {
                item = getItem(parentName);
                hasParent = true;
                File parent = new File(path, itemName + ".yml");
                if(parent.exists()) {
                    YamlConfiguration parentYaml = YamlConfiguration.loadConfiguration(parent);
                    if( parentYaml.getString("subcategory")!=null) {
                        parentItemDisplay = TextFormatter.convertStringToName(parentYaml.getString("subcategory"));
                    }
                }
            }
        }
        final String[] rareCategory = new String[1];



        NBT.modify(item, nbt -> {
            rareCategory[0] = nbt.getOrNull("rarity", String.class);
        });
        if(rareCategory[0]!=null) {
            rarity = ItemRarity.valueOf(rareCategory[0].toUpperCase(Locale.ROOT));
        }

        if (yamlConfiguration.contains("material")) {
            try {
                item = new ItemStack(Material.valueOf(yamlConfiguration.getString("material")));
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
        String rangedStatsString = "rangedstats";

        boolean hideAttributes = true;

        String subcategory = null;

        final ArmourType armourType;

        for (String field : yamlConfiguration.getKeys(false)) {
            switch (field) {
                case "rarity":
                    rarity = ItemRarity.valueOf(yamlConfiguration.getString("rarity").toUpperCase(Locale.ROOT));
                    break;
                case "item_category":
                    category = ItemCategory.valueOf(yamlConfiguration.getString("item_category").toUpperCase(Locale.ROOT));
                    break;
                case "custom_model_data":
                    meta.setCustomModelData(yamlConfiguration.getInt("custom_model_data"));
                    break;
                case "item_model":
                    meta.setItemModel(NamespacedKey.minecraft("item_model"));
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
                    subcategory = yamlConfiguration.getString("subcategory");
                default:
            }
        }

        if(category==ItemCategory.ARMOUR_HELMET || category==ItemCategory.ARMOUR_BOOTS  || category==ItemCategory.ARMOUR_CHESTPLATE || category==ItemCategory.ARMOUR_LEGGINGS) {
            if(yamlConfiguration.contains("armour.type")) {
                armourType=ArmourType.valueOf(yamlConfiguration.getString("armour.type"));
            } else if(yamlConfiguration.contains("armor.type")) {
                armourType=ArmourType.valueOf(yamlConfiguration.getString("armor.type"));
            } else {
                armourType=ArmourType.NONE;
            }
            if(yamlConfiguration.contains("armour.colour")) {
                if(yamlConfiguration.contains("armour.colour.g")) {
                    g = yamlConfiguration.getInt("armour.colour.g");
                }
                if(yamlConfiguration.contains("armour.colour.r")) {
                    r = yamlConfiguration.getInt("armour.colour.r");
                }
                if(yamlConfiguration.contains("armour.colour.b")) {
                    b = yamlConfiguration.getInt("armour.colour.b");
                }
            }
        } else {
            armourType=ArmourType.NONE;
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

            if (subcategory != null && !subcategory.equalsIgnoreCase("")) {
                itemDisplay = TextFormatter.convertStringToName(subcategory);
            } else if(parentItemDisplay!=null) {
                itemDisplay=parentItemDisplay;
            }

            if(MineshaftApi.getInstance().getConfigManager().useItalicItemRarity()) {
                lore.add(rarity.getColourCode() + ChatColor.ITALIC.toString() + rarity.getName() + " " + itemDisplay);
            } else {
                lore.add(rarity.getColourCode() + rarity.getName() + " " + itemDisplay);
            }

            if(!armourType.equals(ArmourType.NONE)) {
                lore.add(ChatColor.GRAY + armourType.getName());
            }

            lore.add("");
        }

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

        if(hasStats) {
            lore.add("");
        }

        // get ranged stat strings
        Logger.logInfo(rangedStatMap.toString());
        for(int i = lowestPriority; i<=highestPriority; i++) {
            for(RangedItemStats stat : rangedStatMap.keySet()) {
                if (i == stat.getPriority() && statMap.get(stat)!=null) {
                    lore.add(getRangedStatString(stat, statMap.get(stat)));
                }
            }
        }

        // Hide attributes
        if (hideAttributes) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }

        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        meta.setLore(lore);

        item.setItemMeta(meta);

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

        // Apply NBT tag with item
        NBT.modify(item, nbt -> {
            nbt.setString("uuid", uuid);
            if(!armourType.equals(ArmourType.NONE)) {
                nbt.setString("ArmourType",armourType.name().toLowerCase());
            }
            // More are available! Ask your IDE, or see Javadoc for suggestions!
        });


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

        ItemRarity finalRarity = rarity;
        NBT.modify(item, nbt -> {
            nbt.setString("rarity", finalRarity.toString());
        });

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
            Logger.logError("could not find " + statPath + " in: " + path + "/" + name + ".yml");
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
            Logger.logError("could not find " + statPath + " in: " + path + "/" + name + ".yml");
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
            Logger.logError("could not find " + rangedStatPath + " in: " + path + "/" + name + ".yml");
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
                return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value) + ChatColor.DARK_GREEN + "+ DEX" + ChatColor.WHITE + "(" + arg + ")";
            } else if(arg<0) {
                return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value) + ChatColor.DARK_GREEN + "+ DEX";
            }
        }
        return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + "+" + NumericFormatter.formatNumberAdvanced(value);
    }

    protected static String getRangedStatString(RangedItemStats stat, Double value) {
        if(value==null||value<0) value=0d;
        return ChatColor.GRAY + TextFormatter.convertStringToName(stat.name().toLowerCase(Locale.ROOT)) + ": " + stat.getColour() + NumericFormatter.formatNumberAdvanced(value);
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

    // TODO: returns null - bug
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
}
