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

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.events.MineshaftUseItemEvent;
import com.mineshaft.mineshaftapi.manager.block.BlockManager;
import com.mineshaft.mineshaftapi.manager.block.json.JsonBlockCacheBridge;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.RangedItemStats;
import com.mineshaft.mineshaftapi.manager.item.item_properties.ItemAmmunitionManager;
import com.mineshaft.mineshaftapi.manager.player.ActionType;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.PacketUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;

public class InteractListener implements Listener {

    @EventHandler
    void onInteract(PlayerInteractEvent e) {

        // return if the player is not holding an item
        if (e.getItem() == null) return;

        // Declare variables
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        ActionType clickType;

        // Detect the item type
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            clickType = ActionType.RIGHT_CLICK;
        } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            clickType = ActionType.LEFT_CLICK;
        } else {
            clickType = ActionType.NULL;
        }

        // Remove a custom block if it is clicked
        if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if(JsonBlockCacheBridge.getBlock(e.getClickedBlock().getLocation())!=null) {
                JsonBlockCacheBridge.removeBlock(e.getClickedBlock().getLocation());
            }
        }

        // CHECK IF ITEM EVENT IS CANCELLED
        if (
            (
                !player.isSneaking() &&  clickType.equals(ActionType.RIGHT_CLICK) && e.getClickedBlock() != null && BlockManager.isInteractable(e.getClickedBlock().getType())
            )
        ) {
            return;
        }

        //
        // GET UUID:
        //

        UUID uniqueId = ItemManager.getItemIdFromItem(item);
        if (uniqueId == null) {
            return;
        }

        //
        // GET EVENTS AND TRIGGER HARDCODED EVENTS
        //
        ArrayList<String> events = ItemManager.getInteractEventsFromItem(ItemManager.getItemNameFromItem(item), clickType);

        // TRIGGER DETECTABLE EVENT FOR CHILD PLUGINS
        MineshaftUseItemEvent event = com.mineshaft.mineshaftapi.manager.event.EventHandler.callMineshaftUseItemEvent(player, uniqueId, item, events, clickType);

        // Null check for the events
        if (events == null || events.isEmpty()) {
            // Event call for item with no events;
            return;
        }

        // Check whether the event is cancelled or not. If it is, return
        if(event.isCancelled()) {
           // Event is cancelled, so return
            return;
        }

        // Hardcoded events:
        boolean executed = com.mineshaft.mineshaftapi.manager.event.EventHandler.handleHardcodedEvents(player,item,events,e,e.getHand(),clickType);
        if (executed && events.size() == 1) return;

        // IMPORTANT: Firing speed mechanics ONLY APPLY to NON-HARDCODED RIGHT-CLICK or LEFT-CLICK EVENTS if a COOLDOWN is specified.

        /*
          HANDLING DYNAMIC EVENTS
          (Not hardcoded)
        */

        boolean cannotFire = (MineshaftApi.getInstance().getCooldownManager().hasCooldown(player.getUniqueId(), uniqueId));

        if (cannotFire||!ItemAmmunitionManager.canShoot(item)) {
            return;
        }

        if(events.isEmpty())return;

        // Shots per second
        double firingSpeed = 0;
        double firingCooldown = 0;

        try {
            firingSpeed = ItemManager.getItemNbtRangedStat(item, RangedItemStats.FIRING_SPEED);
            Logger.logInfo("Firing speed: " + firingSpeed);
            if(firingSpeed!=0) {
                firingCooldown = 1 / firingSpeed;
            }
        } catch (NullPointerException ignored) {}

        if (firingSpeed > 0) {
            // Firing cooldown
            // Apply cooldown
            MineshaftApi.getInstance().getCooldownManager().addPlayerCooldown(player.getUniqueId(), uniqueId, Duration.ofMillis((long) (firingCooldown * 1000)));
        } else {
            firingCooldown=0.4d;
            MineshaftApi.getInstance().getCooldownManager().addPlayerCooldown(player.getUniqueId(), uniqueId, Duration.ofMillis(400L));
        }

        // send item cooldown animation
        if (MineshaftApi.getInstance().getConfigManager().enableItemCooldownAnimation()) {
            int delayInTicks = (int) (firingCooldown * 20);
            PacketUtil.sendCooldown(player, item, delayInTicks);
        }

        // Handle custom event
        com.mineshaft.mineshaftapi.manager.event.EventHandler.handleEvents(player, item, events, e);

        // Handle ammunition consumption
        com.mineshaft.mineshaftapi.manager.event.EventHandler.handleSecondaryHardcodedEvents(player,item,events,e);
    }

    @EventHandler
    void onOffHandSwap(PlayerSwapHandItemsEvent e) {
        if(e.getMainHandItem().getType().equals(Material.AIR)) return;

        Player player = e.getPlayer();
        UUID uniqueId = ItemManager.getItemIdFromItem(e.getMainHandItem());

        if(uniqueId==null) return;

        // Get the events

        String name = ItemManager.getItemName(uniqueId);
        ArrayList<String> events = ItemManager.getInteractEventsFromItem(name, ActionType.OFFHAND_SWAP);

        // Do not cancel if there are no specified events to execute.
        if(events==null||events.isEmpty()) return;

        // Otherwise, cancel and execute the events.
        e.setCancelled(true);
        com.mineshaft.mineshaftapi.manager.event.EventHandler.runEvents(player,e.getMainHandItem(),events,e, EquipmentSlot.HAND,ActionType.OFFHAND_SWAP);
    }
}
