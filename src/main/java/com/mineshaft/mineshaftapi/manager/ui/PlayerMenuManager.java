/*
 *     This program is a Minecraft plugin developed in Java for the Spigot API.
 *     It adds multiple RPG features intended for Multiplayer gameplay.
 *
 *     Copyright (C) 2024  Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.mineshaft.mineshaftapi.manager.ui;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item_shop.ItemMenu;
import com.mineshaft.mineshaftapi.manager.item_shop.ItemType;
import com.mineshaft.mineshaftapi.manager.item_shop.MenuItem;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class PlayerMenuManager {

    public static void OpenPlayerMenu(Player player, String title, int size, HashMap<Integer, MenuItem> items) {

        Inventory ui = Bukkit.createInventory(null, size, title);

        for(int key : items.keySet()) {
            MenuItem menuItem = items.get(key);
            ItemStack item = MineshaftApi.getInstance().getItemManagerInstance().getItem(menuItem.getItemId());

            NBT.modify(item, nbt -> {
                nbt.setEnum("category", menuItem.getItemType());
            });

            if(menuItem.getItemType().equals(ItemType.PURCHASE)) {
                int price = menuItem.getPriceIfCanBuy();
                String name = menuItem.getItemId();
                if(price>0) {
                    NBT.modify(item, nbt -> {
                        nbt.setInteger("price", price);
                    });
                }
                if(name!=null && !name.equalsIgnoreCase("") && name.equalsIgnoreCase("null") && !name.equalsIgnoreCase("nil")) {
                    NBT.modify(item, nbt -> {
                        nbt.setString("itemName", name);
                    });
                }
            }

            ui.setItem(key, item);
        }

        player.openInventory(ui);

    }

    // open menu for player
    public static void OpenMenu(Player player, ItemMenu itemMenu) {
        OpenPlayerMenu(player, itemMenu.getTitle(), itemMenu.getSize(), itemMenu.getItems());
    }

}
