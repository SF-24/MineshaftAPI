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

package com.mineshaft.mineshaftapi.manager.item.item_slots;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.util.Logger;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class SlotManager {

    // Map format: [Slot Name | Slot Type]
    // The slot type is declared by the plugin user, there are no hardcoded values.
    public static HashMap<String, String> getSocketTypes(UUID uniqueId) {

        // Gets the YAML configurations, used to read the file
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(
                new File(MineshaftApi.getInstance().getItemPath(), ItemManager.getItemName(uniqueId))
        );

        // If the section does not exist, return
        if (!yamlConfiguration.contains("slots")) {
            return new HashMap<>();
        }

        HashMap<String, String> slotMap = new HashMap<>();

        // Convert the YAML configuration to a map
        for(String slotName : yamlConfiguration.getConfigurationSection("slots").getKeys(false)) {
            if(!yamlConfiguration.contains("slots."+slotName+".type")) {
                Logger.logWarning("Invalid slot declaration for slot: " + slotName + " in item " + ItemManager.getItemName(uniqueId));
                continue;
            }
            slotMap.put(slotName, yamlConfiguration.getString("slots."+slotName+".type"));
        }

        return slotMap;
    }

    // Map Format: [Name | Item]
    // Gets the items saved to the sockets
    public static HashMap<String, ItemStack> getSocketItems(ItemStack baseItem) {
        HashMap<String, ItemStack> slotMap = new HashMap<>();

        // Load the NBT
        NBT.get(baseItem, nbt->{
            // Gets the list of slot names of the given item and loop through them
            for(String slotName : getSocketTypes(ItemManager.getItemIdFromItem(baseItem)).keySet()) {
                // If the slot has not been filled (and therefore not defined), move on.
                if(nbt.getItemStack("slot_"+slotName)==null || nbt.getItemStack("slot_"+slotName).getType()== Material.AIR) {
                    continue;
                }
                // Otherwise, get the slot value and add it to the map
                slotMap.put(slotName,nbt.getItemStack("slot_"+slotName));
            }
        });
        return slotMap;
    }

    // Sets the slot of a given item to the specified slot.
    // IMPORTANT: This does no check whether the slot exists
    public static void setSlot(ItemStack baseItem, String slotName, ItemStack slotItem) {
        NBT.modify(baseItem, nbt->{
           nbt.setItemStack("slot_"+slotName, slotItem);
        });
    }

    // Gets the item from a given slot.
    // Returns null or an empty AIR item if the slot cannot be read, does not exist or is empty.
    // IMPORTANT: This does not check whether the slot exists
    public static ItemStack getSlot(ItemStack baseItem, String slotName) {
        NBT.get(baseItem,nbt->{
            if(nbt.getItemStack("slot_"+slotName)==null || nbt.getItemStack("slot_"+slotName).getType()== Material.AIR) {
                return new ItemStack(Material.AIR);
            }

            return nbt.getItemStack("slot_"+slotName);
        });
        return new ItemStack(Material.AIR);
    }
}
