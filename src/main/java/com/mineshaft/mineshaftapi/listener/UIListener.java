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

import com.mineshaft.mineshaftapi.manager.item.ItemManagerAccessUtility;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.QuickFunction;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class UIListener implements Listener {

    @EventHandler
    void onClick(InventoryClickEvent e) {


        assert e.getCurrentItem()!=null;
        assert e.getCurrentItem().getItemMeta()!=null;

        // Generic ui check
        String title = ChatColor.translateAlternateColorCodes('&', e.getView().getTitle());
        List<String> blockList = List.of("immutable","back","next","close","reload","button");
        if(e.getCurrentItem().hasItemMeta() && QuickFunction.hasLocalisedName(e.getCurrentItem().getItemMeta()) && blockList.contains(QuickFunction.getLocalisedMane(e.getCurrentItem().getItemMeta()))) {
            e.setCancelled(true);
        } else if(e.getCurrentItem().hasItemMeta() && QuickFunction.hasLocalisedName(e.getCurrentItem().getItemMeta()) &&
                (QuickFunction.getLocalisedMane(e.getCurrentItem().getItemMeta()).contains("back")||QuickFunction.getLocalisedMane(e.getCurrentItem().getItemMeta()).contains("next"))) {
            String locName = QuickFunction.getLocalisedMane(e.getCurrentItem().getItemMeta());

            // UI Menu back and next button check
            // TODO: add folder support
            if (ChatColor.translateAlternateColorCodes('&',title).equalsIgnoreCase( ChatColor.BLACK + "Item View UI")) {
                try {
                    int page = Integer.parseInt(locName.substring(3));
                    ItemManagerAccessUtility.sendItemListUi((Player) e.getWhoClicked(),"",page);
                } catch (NumberFormatException err) {
                    Logger.logError("Error in UIListener:57. Attempted to read number, but got string");
                    err.printStackTrace();
                }
            }
        } else {
            NBT.get(e.getCurrentItem(), nbt -> {
                switch (nbt.getString("clickAction")) {
                    case "close":
                    case "closeView":
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        break;
                    case "cancel":
                    case "immutable":
                        e.setCancelled(true);
                        break;
                    default:
                        break;
                }
            });
        }

        if (ChatColor.translateAlternateColorCodes('&',title).equalsIgnoreCase(ChatColor.BLACK + "Menu")) {
            e.setCancelled(true);
        }
    }

}
