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

package com.mineshaft.mineshaftapi.manager.item.item_components;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import de.tr7zw.nbtapi.NBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ItemAmmunitionManager {


    public static int getMaximumAmmunitionCapacityInWeapon(UUID uniqueId) {
        return getMaximumAmmunitionCapacityInWeapon(ItemManager.getItemDefinition(uniqueId));
    }

    // Get the maximum ammunition that can be used for an item.
    public static int getMaximumAmmunitionCapacityInWeapon(ConfigurationSection yamlConfiguration) {
        int maxShots = 0;

        // Get the data from yaml
        if(yamlConfiguration.contains("ammunition.shot_count")) {
            maxShots=yamlConfiguration.getInt("ammunition.shot_count");
        } else if(yamlConfiguration.contains("ammunition.shots")) {
            maxShots=yamlConfiguration.getInt("ammunition.shots");
        } else {
            // Otherwise, check the parent yaml file
            String parent = yamlConfiguration.getString("parent");
            if(parent!=null && !parent.equalsIgnoreCase("null")) {
                maxShots= getMaximumAmmunitionCapacityInWeapon(ItemManager.getItemDefinition(parent));
            }
        }
        return maxShots;
    }

    // Get all the ammunition types that can be used for a given weapon
    public static ArrayList<String> getUsedAmmunitionTypesForItem(UUID uniqueId) {
        return getUsedAmmunitionTypesForItem(ItemManager.getItemDefinition(uniqueId));
    }

    // Get all the ammunition types that can be used for a given weapon
    public static ArrayList<String> getUsedAmmunitionTypesForItem(ConfigurationSection yamlConfiguration) {

        ArrayList<String> ammunitionTypes = new ArrayList<>();

        // Whether the item has a parent item
        // TODO: fix this code

        String parent = yamlConfiguration.getString("parent");
        if(parent!=null && !parent.equalsIgnoreCase("null")) {
            List<String> parentAmmunitionTypes = getUsedAmmunitionTypesForItem(ItemManager.getItemDefinition(parent));
            ammunitionTypes.addAll(parentAmmunitionTypes);
        }

        for(String field : yamlConfiguration.getConfigurationSection("ammunition").getKeys(false)) {
            switch(field) {
                case "ammunition_types","ammo_types","ammunition_type","ammo_type" -> ammunitionTypes.addAll(yamlConfiguration.getStringList("ammunition."+field));
            }
        }

        return ammunitionTypes;
    }

    public static String getAmmunitionItemInPlayerInventory(Player player, List<String> ammunitionTypes) {
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

    public static void subtractAmmunitionFromItem(Player player, String ammunitionType) {
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

    public static ItemStack reloadAmmunitionForItem(Player player, ItemStack itemStack) {
        // Check the inventory for ammunition

        // Update the ammunition count
        if(getAmmunitionItemInPlayerInventory(player, getUsedAmmunitionTypesForItem(ItemManager.getItemIdFromItem(itemStack)))!=null) {
            if(getAmmunitionCountInWeapon(itemStack)== getMaximumAmmunitionCapacityInWeapon(ItemManager.getItemIdFromItem(itemStack))) {
                player.getInventory().addItem(MineshaftApi.getInstance().getItemManagerInstance().getItem(getAmmunitionTypeInWeapon(itemStack)));
            }

            // TODO: Add ammunition stash
            String ammunitionType = getAmmunitionItemInPlayerInventory(player, getUsedAmmunitionTypesForItem(ItemManager.getItemIdFromItem(itemStack)));
            subtractAmmunitionFromItem(player, ammunitionType);
            setAmmunitionForItem(itemStack,ammunitionType, getMaximumAmmunitionCapacityInWeapon(ItemManager.getItemIdFromItem(itemStack)));
        } else {
            player.sendActionBar(Component.text("Not enough ammunition", NamedTextColor.RED));
        }
        return itemStack;
    }

    public static void setAmmunitionForItem(ItemStack itemStack, String ammunitionType, int ammunitionCount) {
        ItemMeta meta = itemStack.getItemMeta();
        ArrayList<String> lore = (ArrayList<String>) meta.getLore();

        // Update the lore section. Regenerating the whole lore would be better.
        if(lore!=null) {
            for (int line = 0; line < lore.size(); line++) {
                if (lore.get(line).contains("Ammunition:")) {
                    lore.set(line, LoreManager.LoreSection.getAmmunitionLoreLine(Math.max(ammunitionCount, 0), getMaximumAmmunitionCapacityInWeapon(ItemManager.getItemIdFromItem(itemStack))));
                }
            }
        } else {
            lore = new ArrayList<>();
            lore.add(LoreManager.LoreSection.getAmmunitionLoreLine(Math.max(ammunitionCount, 0), getMaximumAmmunitionCapacityInWeapon((ItemManager.getItemIdFromItem(itemStack)))));
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);

        NBT.modify(itemStack, nbt->{
            nbt.setString("ammunition_type", ammunitionType);
            nbt.setInteger("ammunition",ammunitionCount);
        });
    }

    public static int getAmmunitionCountInWeapon(ItemStack itemStack) {
        AtomicInteger shots = new AtomicInteger();
        NBT.get(itemStack, nbt->{
           shots.set(nbt.getInteger("ammunition"));
        });
        return shots.get();
    }

    public static String getAmmunitionTypeInWeapon(ItemStack itemStack) {
        AtomicReference<String> type = new AtomicReference<>();
        NBT.get(itemStack, nbt->{
            type.set(nbt.getString("ammunition_type"));
        });
        return type.get();
    }

    public static boolean canShootWeapon(ItemStack itemStack) {
        return getMaximumAmmunitionCapacityInWeapon(ItemManager.getUuid(ItemManager.getItemNameFromItem(itemStack)))==0|| getAmmunitionCountInWeapon(itemStack)>0;
    }

    public static ItemStack consumeAmmunition(ItemStack itemStack) {
        if(getAmmunitionCountInWeapon(itemStack)<0) return itemStack;
        int shotsLeft = getAmmunitionCountInWeapon(itemStack)-1;
        setAmmunitionForItem(itemStack, getAmmunitionTypeInWeapon(itemStack),shotsLeft);
        return itemStack;
    }

//    public static int getMaxAmmunition(UUID uniqueId) {
//        return getMaxAmmunition(ItemManager.getItemDefinition(uniqueId));
//    }
//
//    public static int getMaxAmmunition(ConfigurationSection yamlConfiguration) {
//        for(String field : yamlConfiguration.getConfigurationSection("ammunition").getKeys(false)) {
//            switch(field) {
//                case "shot_count","shots" -> {
//                    return yamlConfiguration.getInt("ammunition."+field);
//                }
//            }
//        }
//        return 0;
//    }

}
