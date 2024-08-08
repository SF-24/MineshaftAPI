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

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.ActionType;
import com.mineshaft.mineshaftapi.manager.event.EventManager;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class InteractListener implements Listener {

    @EventHandler
    void onInteract(PlayerInteractEvent e) {

        ActionType clickType = ActionType.NULL;

        // detect right and left clicks
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            clickType=ActionType.RIGHT_CLICK;
        } else if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            clickType=ActionType.LEFT_CLICK;
        }

        ItemStack item = e.getItem();

        // return if the player is not holding an item
        if(item==null) return;

        Player player = e.getPlayer();

        final UUID[] uuid = new UUID[1];

        NBT.get(item, nbt -> {
            String id = nbt.getOrDefault("uuid","null");
            if(id.equalsIgnoreCase("null")) return;
            uuid[0] = UUID.fromString(id);
        });
        UUID uniqueId = uuid[0];

        if(uniqueId !=null) {
            String name = ItemManager.getItemName(uniqueId);

            ArrayList<String> events = ItemManager.getInteractEventsFromItem(name, clickType);

            if(events==null) return;

            e.setCancelled(true);

            for(String event : events) {
                EventManager eventManager = MineshaftApi.getInstance().getEventManagerInstance();
                eventManager.runEvent(eventManager.getEvent(event), player.getLocation(), player.getUniqueId());
            }
        }
    }
}
