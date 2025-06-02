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

package com.mineshaft.mineshaftapi.manager.item.crafting;

import com.mineshaft.mineshaftapi.MineshaftApi;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.io.File;
import java.util.HashMap;

public class ItemRepairManager {

    public static double getDamage(ItemStack itemStack) {
        Damageable damageableMeta = (Damageable) itemStack.getItemMeta();
        return damageableMeta.getDamage();
    }

    public static double getMaximumDamage(ItemStack itemStack) {
        Damageable damageableMeta = (Damageable) itemStack.getItemMeta();
        return damageableMeta.getMaxDamage();
    }

    public static int getRepairForItem(ItemStack itemStack, Material repairMaterial) {
        return getRepairMaterials(itemStack).get(repairMaterial);
    }

    public static ItemStack getRepairedItem(ItemStack item, Material repairItem, int amount) {
        ItemStack itemStack = item.clone();
        double damage =0;
        if(amount>=getRepairItemNumber(itemStack,repairItem)) {
            damage = getDamage(itemStack) - getRepairItemNumber(itemStack, repairItem) * getRepairForItem(itemStack, repairItem);
        } else {
            damage = getDamage(itemStack) - amount * getRepairForItem(itemStack, repairItem);
        }
        if(damage<0) damage=0;
        Damageable damageableMeta = (Damageable) itemStack.getItemMeta();
        damageableMeta.setDamage((int) Math.floor(damage));
        itemStack.setItemMeta(damageableMeta);
        return itemStack;
    }

    public static int getRepairItemNumber(ItemStack itemStack, Material repairItem) {
        if(!(itemStack.getItemMeta() instanceof Damageable)) return -1;
        double damage = getDamage(itemStack);
        if(damage <= 0) return 0;
        return (int)Math.ceil((damage/(double)getRepairMaterials(itemStack).get(repairItem)));
    }

    public static boolean isRepairItem(ItemStack item, Material repairItem) {
        return getRepairMaterials(item).containsKey(repairItem);
    }

    public static HashMap<Material, Integer> getRepairMaterials(ItemStack item) {
        return getRepairMaterials(MineshaftApi.getInstance().getItemManagerInstance().getItemNameFromItem(item));
    }

    public static HashMap<Material, Integer> getRepairMaterials(String itemName) {
        // Init. file:
        File fileYaml = new File(MineshaftApi.getInstance().getItemPath(), itemName + ".yml");
        // return null if file does not exist
        if (!fileYaml.exists()) return null;
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        // Declare repair item map
        HashMap<Material, Integer> repairItems = new HashMap<>();

        // Get parent repair items
        if (yamlConfiguration.contains("parent")) {
            String parentName = yamlConfiguration.getString("parent");
            if (parentName != null && !parentName.equalsIgnoreCase("null") && !parentName.equalsIgnoreCase("nil")) {
                repairItems.putAll(getRepairMaterials(parentName));
            }
        }

        if(yamlConfiguration.contains("repair-materials")) {
            for(String key : yamlConfiguration.getConfigurationSection("repair-materials").getKeys(false)) {
                int v = yamlConfiguration.getInt("repair-materials." + key);
                repairItems.put(Material.getMaterial(key.toUpperCase()), v);
            }
        }
        return repairItems;
    }


}
