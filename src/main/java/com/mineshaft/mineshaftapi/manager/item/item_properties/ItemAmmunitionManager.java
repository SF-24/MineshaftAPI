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

package com.mineshaft.mineshaftapi.manager.item.item_properties;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.LoreManager;
import com.mineshaft.mineshaftapi.util.Logger;
import de.tr7zw.changeme.nbtapi.NBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ItemAmmunitionManager {


    public static int getMaximumAmmunitionCount(String name) {

        int maxShots = 0;

        File fileYaml = new File(ItemManager.getPath(), name + ".yml");

        // return null if file does not exist
        if (!fileYaml.exists()) {
            Logger.logError("FILE NOT FOUND ERROR!!!!!!!");
            return 0;
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        if(yamlConfiguration.contains("ammunition.shot_count")) {
            maxShots=yamlConfiguration.getInt("ammunition.shot_count");
        } else if(yamlConfiguration.contains("ammunition.shots")) {
            maxShots=yamlConfiguration.getInt("ammunition.shots");
        } else {
            // get parent data
            String parent = yamlConfiguration.getString("parent");
            if(parent!=null && !parent.equalsIgnoreCase("null")) {
                maxShots=getMaximumAmmunitionCount(parent);
            }
        }
        return maxShots;
    }

    public static ArrayList<String> getAmmunitionTypes(String name) {

        ArrayList<String> ammunitionTypes = new ArrayList<>();

        File fileYaml = new File(ItemManager.getPath(), name + ".yml");

        // return null if file does not exist
        if (!fileYaml.exists()) {
            Logger.logError("FILE NOT FOUND ERROR!!!!!!!");
            return new ArrayList<>();
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        // Whether the item has a parent item
        // TODO: fix this code

        String parent = yamlConfiguration.getString("parent");
        if(parent!=null && !parent.equalsIgnoreCase("null")) {
            List<String> parentAmmunitionTypes = getAmmunitionTypes(parent);
            ammunitionTypes.addAll(parentAmmunitionTypes);
        }

        for(String field : yamlConfiguration.getConfigurationSection("ammunition").getKeys(false)) {
            switch(field) {
                case "ammunition_types","ammo_types","ammunition_type","ammo_type" -> ammunitionTypes.addAll(yamlConfiguration.getStringList("ammunition."+field));
            }
        }

        return ammunitionTypes;
    }

    public static String getAmmunitionInInventory(Player player, List<String> ammunitionTypes) {
        for(int i = 0; i<37; i++) {
            if(player.getInventory().getItem(i)==null || player.getInventory().getItem(i).getType()==Material.AIR || ItemManager.getItemIdFromItem(player.getInventory().getItem(i))==null) {
                continue;
            }
            if(ammunitionTypes.contains(ItemManager.getItemName(ItemManager.getItemIdFromItem(player.getInventory().getItem(i))))) {
                return ItemManager.getItemName(ItemManager.getItemIdFromItem(player.getInventory().getItem(i)));
            }
        }
        return null;
    }

    public static void takeAmmunition(Player player, String ammunitionType) {
        for(int i = 0; i<37; i++) {
            if(player.getInventory().getItem(i)==null ||player.getInventory().getItem(i).getType().equals(Material.AIR)) continue;
            if(ItemManager.getItemNameFromItem(player.getInventory().getItem(i)).equalsIgnoreCase(ammunitionType)) {
                ItemStack item = player.getInventory().getItem(i);
                item.setAmount(item.getAmount()-1);
                player.getInventory().setItem(i,item);
                return;
            }
        }
    }

    public static ItemStack reloadItem(Player player, ItemStack itemStack) {
        // Check the inventory for ammunition

        // Update the ammunition count
        if(getAmmunitionInInventory(player,getAmmunitionTypes(ItemManager.getItemNameFromItem(itemStack)))!=null) {
            if(getAmmunition(itemStack)==getMaximumAmmunitionCount(ItemManager.getItemNameFromItem(itemStack))) {
                player.getInventory().addItem(MineshaftApi.getInstance().getItemManagerInstance().getItem(getAmmunitionType(itemStack)));
            }

            // TODO: Add ammunition stash
            String ammunitionType = getAmmunitionInInventory(player,getAmmunitionTypes(ItemManager.getItemNameFromItem(itemStack)));
            takeAmmunition(player, ammunitionType);
            setAmmunition(itemStack,ammunitionType,getMaximumAmmunitionCount(ItemManager.getItemNameFromItem(itemStack)));
        } else {
            player.sendActionBar(Component.text("Not enough ammunition", NamedTextColor.RED));
        }
        return itemStack;
    }

    public static void setAmmunition(ItemStack itemStack, String ammunitionType, int ammunitionCount) {
        ItemMeta meta = itemStack.getItemMeta();
        ArrayList<String> lore = (ArrayList<String>) meta.getLore();
        if(lore!=null) {
            for (int line = 0; line < lore.size(); line++) {
                if (lore.get(line).contains("Ammunition:")) {
                    lore.set(line, LoreManager.getAmmunitionString(Math.max(ammunitionCount, 0), getMaximumAmmunitionCount(ItemManager.getItemName(ItemManager.getItemIdFromItem(itemStack)))));
                }
            }
        } else {
            lore = new ArrayList<>();
            lore.add(LoreManager.getAmmunitionString(Math.max(ammunitionCount, 0), getMaximumAmmunitionCount(ItemManager.getItemName(ItemManager.getItemIdFromItem(itemStack)))));
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);

        NBT.modify(itemStack, nbt->{
            nbt.setString("ammunition_type", ammunitionType);
            nbt.setInteger("ammunition",ammunitionCount);
        });
    }

    public static int getAmmunition(ItemStack itemStack) {
        AtomicInteger shots = new AtomicInteger();
        NBT.get(itemStack, nbt->{
           shots.set(nbt.getInteger("ammunition"));
        });
        return shots.get();
    }

    public static String getAmmunitionType(ItemStack itemStack) {
        AtomicReference<String> type = new AtomicReference<>();
        NBT.get(itemStack, nbt->{
            type.set(nbt.getString("ammunition_type"));
        });
        return type.get();
    }

    public static boolean canShoot(ItemStack itemStack) {
        return getMaximumAmmunitionCount(ItemManager.getItemNameFromItem(itemStack))==0||getAmmunition(itemStack)>0;
    }

    public static ItemStack consumeAmmunition(ItemStack itemStack) {
        if(getAmmunition(itemStack)<0) return itemStack;
        int shotsLeft = getAmmunition(itemStack)-1;
        setAmmunition(itemStack,getAmmunitionType(itemStack),shotsLeft);
        return itemStack;
    }

    public static int getMaxAmmunition(UUID uniqueId) {
        for(String field : ItemManager.getYamlConfiguration(uniqueId).getConfigurationSection("ammunition").getKeys(false)) {
            switch(field) {
                case "shot_count","shots" -> {
                    return ItemManager.getYamlConfiguration(uniqueId).getInt("ammunition."+field);
                }
            }
        }
        return 0;
    }

    public static @NotNull List<String> getAmmunitionTypes(UUID uniqueId) {
        for(String field : ItemManager.getYamlConfiguration(uniqueId).getConfigurationSection("ammunition").getKeys(false)) {
            switch(field) {
                case "ammunition_types","ammo_types","ammunition_type","ammo_type" -> {
                    return ItemManager.getYamlConfiguration(uniqueId).getStringList("ammunition."+field);
                }
            }
        }
        return Collections.emptyList();
    }


}
