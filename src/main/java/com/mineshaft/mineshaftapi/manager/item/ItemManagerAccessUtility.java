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
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.QuickFunction;
import jdk.jfr.Description;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemManagerAccessUtility {

    @Description("Page numbers starting with 1." +
            "To avoid pagination set pageSize to 0 or -1. " +
            "Set page size to -2 to use default value (45)")
    public static ArrayList<ItemStack> getItemsEnMasse(String folder, int page, int pageSize) {
        if(page>getItemsEnMasseFull(folder,pageSize).size()) {
            Logger.logError("Attempted to access non-existent page. Error in:\n" +
                    "GetItemCommand:104");
            return null;
        }

        return getItemsEnMasseFull(folder,pageSize).get(page-1);
    }

    public static ArrayList<ArrayList<ItemStack>> getItemsEnMasseFull(String folder, int pageSize) {
        if(pageSize==-2) pageSize=45;

        ArrayList<ArrayList<ItemStack>> itemList = new ArrayList<>();

        ArrayList<ItemStack> pageList = new ArrayList<>();
        for(String name : MineshaftApi.getInstance().getItemManagerInstance().getItemList().values()) {
            pageList.add(MineshaftApi.getInstance().getItemManagerInstance().getItem(name));
            if(pageSize>0 && pageList.size()>=pageSize) {
                itemList.add(pageList);
                pageList.clear();
            }
        }
        return itemList;
    }

    public static Inventory getItemUI(String folder, int page) {
        Inventory itemInventory = Bukkit.createInventory(null, 54, ChatColor.BLACK + "Item View UI");

        ItemStack emptyItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta emptyItemMeta = emptyItem.getItemMeta();
        emptyItemMeta = QuickFunction.setLocalisedName(emptyItemMeta,"immutable");
        emptyItemMeta.setDisplayName("");
        emptyItem.setItemMeta(emptyItemMeta);

        for(int i = 45; i<54; i++) {
            itemInventory.setItem(i,emptyItem);
        }

        ArrayList<ArrayList<ItemStack>> itemList = ItemManagerAccessUtility.getItemsEnMasseFull(folder,-2);

        if(itemList.size()>=2) {
            if(page!=itemList.size()) {
                ItemStack nextItem = new ItemStack(Material.ARROW);
                ItemMeta nextMeta = nextItem.getItemMeta();
                nextMeta.setDisplayName("Next Page");
                nextMeta=QuickFunction.setLocalisedName(nextMeta,"next"+page);
                nextItem.setItemMeta(nextMeta);
                itemInventory.setItem(53, nextItem);
            }
            if(page!=1) {
                ItemStack backItem = new ItemStack(Material.ARROW);
                ItemMeta backMeta = backItem.getItemMeta();
                backMeta.setDisplayName("Previous Page");
                backMeta=QuickFunction.setLocalisedName(backMeta,"back"+page);
                backItem.setItemMeta(backMeta);
                itemInventory.setItem(45,backItem);
            }
        }
        return itemInventory;
    }

    public static void sendItemListUi(Player player, String folder, int page) {
        player.openInventory(getItemUI(folder,page));
    }
}
