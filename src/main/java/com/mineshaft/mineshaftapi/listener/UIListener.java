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

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class UIListener implements Listener {

    @EventHandler
    void onClick(InventoryClickEvent e) {


        assert e.getCurrentItem()!=null;
        assert e.getCurrentItem().getItemMeta()!=null;

        // Generic ui check
        String title = ChatColor.translateAlternateColorCodes('&', e.getView().getTitle());

        // Was an if statement
        {
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
