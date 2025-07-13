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

package com.mineshaft.mineshaftapi.listener;

import com.mineshaft.mineshaftapi.manager.item.crafting.ItemRepairManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class UIListener implements Listener {

    @EventHandler
    void onClick(InventoryClickEvent e) {

//        assert e.getCurrentItem()!=null;
//        assert e.getCurrentItem().getItemMeta()!=null;

        // Generic ui check
        String title = ChatColor.translateAlternateColorCodes('&', e.getView().getTitle());

        // Was an if statement

//        {
//            NBT.get(e.getCurrentItem(), nbt -> {
//                switch (nbt.getString("clickAction")) {
//                    case "close":
//                    case "closeView":
//                        e.setCancelled(true);
//                        e.getWhoClicked().closeInventory();
//                        break;
//                    case "cancel":
//                    case "immutable":
//                        e.setCancelled(true);
//                        break;
//                    default:
//                        break;
//                }
//            });
//        }{
////            NBT.get(e.getCurrentItem(), nbt -> {
////                switch (nbt.getString("clickAction")) {
////                    case "close":
////                    case "closeView":
////                        e.setCancelled(true);
////                        e.getWhoClicked().closeInventory();
////                        break;
////                    case "cancel":
////                    case "immutable":
////                        e.setCancelled(true);
////                        break;
////                    default:
////                        break;
////                }
////            });
////

        if (ChatColor.translateAlternateColorCodes('&', title).equalsIgnoreCase(ChatColor.BLACK + "Menu")) {
            e.setCancelled(true);


        } else if (ChatColor.translateAlternateColorCodes('&', title).equalsIgnoreCase(ChatColor.BLACK + "Item Repair")) {
            e.getWhoClicked().sendMessage("Type: " + e.getSlot() + ", " + e.getCursor().getType());
            e.getWhoClicked().sendMessage("Type: " + e.getSlot() + ", " + e.getCurrentItem().getType());
            if (e.getCurrentItem().getType().equals(Material.PEONY) && e.getCurrentItem().getItemMeta().getCustomModelData() == 1) {
                e.setCancelled(true);
            }

            if (e.getSlot() == 16) {

                // If placing item in slot 16
                if (!e.getCursor().getType().equals(Material.AIR)||e.getCurrentItem().getType().equals(Material.AIR)) {
                    // placed item in slot
                    e.setCancelled(true);
                } else {

                    // Otherwise:

                    e.getInventory().setItem(10, null);
                    ItemStack repair = e.getInventory().getItem(13);
                    assert repair != null;
                    repair.setAmount(repair.getAmount() - 1);

                    // update repair item
                    e.getInventory().setItem(13, repair);
                    return;
                }
            }

            if (e.getSlot() != 16) {
                ItemStack item;
                ItemStack repair;
                if (e.getSlot() == 10) {
                    item = e.getCursor();
                } else {
                    item = e.getInventory().getItem(10);
                }
                if (e.getSlot() == 13) {
                    repair = e.getCursor();
                } else {
                    repair = e.getInventory().getItem(13);
                }

                    if (ItemRepairManager.isRepairItem(item, repair.getType())) {
                    e.getInventory().setItem(16, ItemRepairManager.getRepairedItem(item, repair.getType(), 1));

                }
            }
        }
    }
}
