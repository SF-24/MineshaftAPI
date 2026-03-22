/*
 * Copyright (c) 2026. Sebastian Frynas
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
import com.mineshaft.mineshaftapi.manager.item.fields.ItemRarity;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemSubcategory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class ClientItemManager {

    public static String getNameTranslation(ItemStack itemStack) {
        return null;
    }

    public static boolean isParsable(ItemStack itemStack) {
        return itemStack.getType()!=Material.AIR && getNameTranslation(itemStack) != null || (MineshaftApi.getInstance().getConfigManager().getVanillaItemRarity() != ItemRarity.STANDARD && ((itemStack.getItemMeta() == null || itemStack.getItemMeta().getLore() == null) && getMaterialItemRarity(itemStack.getType()) != ItemRarity.STANDARD));
    }

    public static ItemRarity getMaterialItemRarity(Material material) {
        ItemRarity itemRarity = MineshaftApi.getInstance().getConfigManager().getVanillaItemRarityOverrides().get(material);
        if(itemRarity==null) return MineshaftApi.getInstance().getConfigManager().getVanillaItemRarity();
        return itemRarity;
    }

    public static ItemSubcategory getMaterialItemSubcategory(Material material) {
        ItemSubcategory returnVal = MineshaftApi.getInstance().getConfigManager().getVanillaItemSubcategoryOverrides().get(material);
        if(returnVal==null) return ItemSubcategory.DEFAULT;
        return returnVal;
    }

    public static ItemStack parseItem(ItemStack itemStack) {
        if(itemStack==null) return new ItemStack(Material.AIR);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta==null || itemMeta.getLore() == null || itemMeta.getLore().isEmpty()) {
            ItemRarity itemRarity = getMaterialItemRarity(itemStack.getType());
            if(itemRarity!=ItemRarity.STANDARD && itemMeta!=null) {
                itemMeta.setLore(Collections.singletonList(LoreManager.getRarityString(itemRarity, LoreManager.getItemSubcategoryDisplay(itemRarity,getMaterialItemSubcategory(itemStack.getType())))));
            }
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

}
