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

import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.util.Logger;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        for(int i = 0; i<36; i++) {
            if(ammunitionTypes.contains(ItemManager.getItemName(ItemManager.getItemIdFromItem(player.getInventory().getItem(i))))) {
                return ItemManager.getItemName(ItemManager.getItemIdFromItem(player.getInventory().getItem(i)));
            }
        }
        return null;
    }

    public static void takeAmmunition(Player player, String ammunitionType) {
        for(int i = 0; i<36; i++) {
            if(player.getInventory().getItem(i)==null ||player.getInventory().getItem(i).getType().equals(Material.AIR)) continue;
            if(ItemManager.getItemNameFromItem(player.getInventory().getItem(i)).equalsIgnoreCase(ammunitionType)) {
                ItemStack item = player.getInventory().getItem(i);
                item.setAmount(item.getAmount()-1);
                player.getInventory().setItem(i,item);
                return;
            }
        }
    }

    public static void reloadItem(Player player, ItemStack itemStack) {
        // Check the inventory for ammunition

        // Update the ammunition count
        if(getAmmunitionInInventory(player,getAmmunitionTypes(ItemManager.getItemNameFromItem(itemStack)))!=null) {
            String ammunitionType = getAmmunitionInInventory(player,getAmmunitionTypes(ItemManager.getItemNameFromItem(itemStack)));
            takeAmmunition(player, ammunitionType);
            setAmmunition(itemStack,ammunitionType,getMaximumAmmunitionCount(ItemManager.getItemNameFromItem(itemStack)));
        }
    }

    public static void setAmmunition(ItemStack itemStack, String ammunitionType, int ammunitionCount) {
        ItemMeta meta = itemStack.getItemMeta();
        ArrayList<String> lore = (ArrayList<String>) meta.getLore();
        for(int line = 0; line<lore.size(); line++) {
            if(lore.get(line).contains("Ammunition:")) {
                lore.remove(line);
            }
        }
        lore.add(ItemManager.getAmmunitionString(ammunitionCount,getMaximumAmmunitionCount(ItemManager.getItemName(ItemManager.getItemIdFromItem(itemStack)))));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);

        NBT.modify(itemStack, nbt->{
            nbt.setString("ammunition_type", ammunitionType);
            nbt.setInteger("ammunition",ammunitionCount);
        });
    }

    public static int getAmmunition(ItemStack itemStack) {
        NBT.get(itemStack, nbt->{
            return nbt.getInteger("ammunition");
        });
        return 0;
    }

    public static String getAmmunitionType(ItemStack itemStack) {
        NBT.get(itemStack, nbt->{
            return nbt.getString("ammunition_type");
        });
        return null;
    }

    public static boolean canShoot(ItemStack itemStack) {
        return getMaximumAmmunitionCount(ItemManager.getItemNameFromItem(itemStack))==0||getAmmunition(itemStack)>0;
    }

    public static void shoot(ItemStack itemStack) {
        int shotsLeft = getAmmunition(itemStack)-1;
        setAmmunition(itemStack,getAmmunitionType(itemStack),shotsLeft);
    }


}
