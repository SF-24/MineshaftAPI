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

package com.mineshaft.mineshaftapi.listener;

import com.mineshaft.mineshaftapi.util.QuickFunction;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UIListener implements Listener {

    @EventHandler
    void onClick(InventoryClickEvent e) {

        String title = ChatColor.translateAlternateColorCodes('&', e.getView().getTitle());

        String menu = ChatColor.BLACK + "Menu";

        List<String> blockList = List.of("immutable","back","next","close","reload","button");
        assert e.getCurrentItem().getItemMeta()!=null;
        if(e.getCurrentItem().hasItemMeta() && QuickFunction.hasLocalisedName(e.getCurrentItem().getItemMeta()) && blockList.contains(QuickFunction.getLocalisedMane(e.getCurrentItem().getItemMeta()))) {
            e.setCancelled(true);
        }

        if (title.equalsIgnoreCase(ChatColor.BLACK + "Menu")) {
            e.setCancelled(true);
        }
    }

}
