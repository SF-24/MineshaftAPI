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

package com.mineshaft.mineshaftapi.manager.event;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.events.MineshaftUseItemEvent;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.item_properties.ItemAmmunitionManager;
import com.mineshaft.mineshaftapi.manager.item.item_properties.ItemSheatheManager;
import com.mineshaft.mineshaftapi.manager.player.ActionType;
import com.mineshaft.mineshaftapi.util.ItemUtil;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class EventHandler {

    public static void runEvents(Player player, ItemStack item, ArrayList<String> events, Cancellable e, EquipmentSlot slot, ActionType actionType) {
        boolean exec = handleHardcodedEvents(player,item,events,e,slot,actionType);
        if(exec&&events.size()==1) return;

        // Run events
        callMineshaftUseItemEvent(player,ItemManager.getItemIdFromItem(item),item,events,actionType);


    }

    public static boolean handleHardcodedEvents(Player player, ItemStack item, ArrayList<String> events, Cancellable e, EquipmentSlot slot, ActionType actionType) {
        // HARDCODED EVENTS:

        boolean cannotFire = (MineshaftApi.getInstance().getCooldownManager().hasCooldown(player.getUniqueId(), ItemManager.getItemIdFromItem(item)));
        if (cannotFire||!ItemAmmunitionManager.canShoot(item)) {
            return false;
        }


        for(String event : events) {
            switch (event) {
                case "toggle_sheathe" ->{
                    if(ItemUtil.isSheathed(item)) {
                        ItemSheatheManager.unSheatheWeapon(item);
                    } else {
                        ItemSheatheManager.sheatheWeapon(item);
                    }

                    if(slot==(EquipmentSlot.OFF_HAND)) {
                        player.getInventory().setItemInOffHand(item);
                    }  else {
                        player.getInventory().setItemInMainHand(item);
                    }
                }
                case "parry"->{
                    // Player is blocking:
                    if (MineshaftApi.getInstance().getActionManager().canBlock(player.getUniqueId())) {
                        if (!MineshaftApi.getInstance().getActionManager().isPlayerBlocking(player.getUniqueId())) {
                            // Start blocking
                            MineshaftApi.getInstance().getActionManager().addPlayerBlocking(player.getUniqueId());
                        }
                    } else {
                        // COOLDOWN!
                        e.setCancelled(true);
                    }
                    return true;
                }
                case "power_attack"->{
                    // Power attack
                    if (MineshaftApi.getInstance().getActionManager().canDoPowerAttack(player.getUniqueId())) {
                        if (!MineshaftApi.getInstance().getActionManager().isPlayerPowerAttack(player.getUniqueId())) {
                            // Start preparing power attack
                            MineshaftApi.getInstance().getActionManager().addPlayerPowerAttack(player.getUniqueId(), ItemManager.getItemSubcategory(item));
                        }
                    } else {
                        // COOLDOWN!
                        e.setCancelled(true);
                    }
                    return true;
                }
                case "wand"->{
                    e.setCancelled(true);
                    return true;
                }
                case "reload"->{
                    e.setCancelled(true);
                    player.playSound(player.getLocation(), "minecraft:block.stone_button.click_on", SoundCategory.PLAYERS, 1.0f, 1.0f);
                    if (slot == (EquipmentSlot.OFF_HAND)) {
                        player.getInventory().setItemInOffHand(ItemAmmunitionManager.reloadItem(player, item));
                    } else {
                        player.getInventory().setItemInMainHand(ItemAmmunitionManager.reloadItem(player, item));
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public static void handleSecondaryHardcodedEvents(Player player, ItemStack item, ArrayList<String> events, PlayerInteractEvent e) {
        if(events.contains("use_ammo") || events.contains("use_ammunition")) {
            e.setCancelled(true);
            if(e.getHand()==EquipmentSlot.OFF_HAND) {
                player.getInventory().setItemInOffHand(ItemAmmunitionManager.consumeAmmunition(item));
            } else {
                player.getInventory().setItemInMainHand(ItemAmmunitionManager.consumeAmmunition(item));
            }
        }
    }

    public static void handleEvents(Player player, ItemStack item, ArrayList<String> events, Cancellable e) {
        for (String event : events) {
            if (EventManager.isHardcoded(event)) {
                continue;
            }
            // Cancel the event if it should be cancelled
            e.setCancelled(true);
            EventManager eventManager = MineshaftApi.getInstance().getEventManagerInstance();
            Event executableEvent = eventManager.getEvent(event, item);

            if (executableEvent == null) {
                Logger.logWarning("Detected null executable event for event " + event);
            } else {
                String sound = executableEvent.getSound();
                if (sound != null) {
                    player.getLocation().getWorld().playSound(player.getLocation(), sound, 5.0f, 1.0f);
                }
                eventManager.runEvent(executableEvent, player.getLocation(), player.getUniqueId(), player);
            }
        }
    }

    public static MineshaftUseItemEvent callMineshaftUseItemEvent(Player player, UUID uniqueId, ItemStack item, ArrayList<String> events, ActionType action) {
        MineshaftUseItemEvent event = new MineshaftUseItemEvent(player,uniqueId,events,item,action);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
