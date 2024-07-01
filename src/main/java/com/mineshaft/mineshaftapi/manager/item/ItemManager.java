package com.mineshaft.mineshaftapi.manager.item;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.VariableTypeEnum;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemCategory;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemFields;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemRarity;
import com.mineshaft.mineshaftapi.text.Logger;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ItemManager {

    HashMap<UUID, String> items = new HashMap<>();

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
        String name = "Custom Item";

        for(String field : yamlConfiguration.getKeys(false)) {
            switch (field) {
                case "rarity":rarity = ItemRarity.valueOf(yamlConfiguration.getString("rarity"));
                case "item_category": category = ItemCategory.valueOf(ItemFields.item_category.name().toLowerCase(Locale.ROOT));
                case "custom_model_data": meta.setCustomModelData(yamlConfiguration.getInt("custom_model_data"));
                case "name": name=yamlConfiguration.getString("name");
                default:
            }
        }

        meta.setDisplayName(rarity.getColourCode() + name);

        // GENERATE LORE:
        ArrayList<String> lore = new ArrayList<>();

        if(rarity!=ItemRarity.STANDARD) {
            lore.add(rarity.getColourCode() + rarity.getName() + " Item");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        // Apply NBT tag with item
        NBT.modify(item, nbt -> {
            nbt.setString("uuid", uuid);
            // More are available! Ask your IDE, or see Javadoc for suggestions!
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

            for(ItemFields itemFields : ItemFields.values()) {
                yamlConfiguration.createSection(itemFields.name().toLowerCase(Locale.ROOT));
                if (!itemFields.getVariableType().equals(VariableTypeEnum.LIST)) {
                    yamlConfiguration.set(itemFields.name().toLowerCase(Locale.ROOT), itemFields.getDefaultValue());
                }
            }

            // Save demo item
            try {
                yamlConfiguration.save(fileYaml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
