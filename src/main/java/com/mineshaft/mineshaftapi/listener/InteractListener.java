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
import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.EventManager;
import com.mineshaft.mineshaftapi.manager.event.event_subclass.BeamEvent;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.RangedItemStats;
import com.mineshaft.mineshaftapi.manager.player.ActionType;
import de.tr7zw.changeme.nbtapi.NBT;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;

public class InteractListener implements Listener {


    @EventHandler
    void onInteract(PlayerInteractEvent e) {

        //
        // GET CLICK TYPE
        //
        ActionType clickType = ActionType.NULL;

        // detect right and left clicks
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            clickType = ActionType.RIGHT_CLICK;
        } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            clickType = ActionType.LEFT_CLICK;
        }

        ItemStack item = e.getItem();

        // return if the player is not holding an item
        if (item == null) return;

        Player player = e.getPlayer();

        final UUID[] uuid = new UUID[1];

        // CHECK IF ITEM EVENT IS CANCELLED
        if (!(player.isSneaking() || e.getAction().equals(Action.RIGHT_CLICK_AIR) || !clickType.equals(ActionType.RIGHT_CLICK) || (e.getClickedBlock() != null && !BlockManager.isInteractable(e.getClickedBlock().getType())))) {
            return;
        }

        //
        // GET UUID:
        //
        try {
            NBT.get(item, nbt -> {
                String id = nbt.getOrDefault("uuid", "null");
                if (id.equalsIgnoreCase("null")) return;
                uuid[0] = UUID.fromString(id);
            });
        } catch (Exception ignored) {
        }
        UUID uniqueId = uuid[0];

        //
        // GET EVENTS AND TRIGGER HARDCODED EVENTS
        //
        ArrayList<String> events = null;
        if (uniqueId != null) {
            String name = ItemManager.getItemName(uniqueId);

            events = ItemManager.getInteractEventsFromItem(name, clickType);

            if (events == null || events.isEmpty()) {
                // Event call for item with no events;
                Bukkit.getPluginManager().callEvent(new MineshaftUseItemEvent(player,uniqueId,new ArrayList<>(),item,clickType));
                return;
            }

            // TRIGGER DETECTABLE EVENT FOR CHILD PLUGINS
            MineshaftUseItemEvent event = new MineshaftUseItemEvent(player,uniqueId,events,item,clickType);
            Bukkit.getPluginManager().callEvent(event);

            // Check whether the event is cancelled or not. If it is, return
            if(event.isCancelled()) {
               // Event is cancelled, so return
                return;
            }

            // HARDCODED EVENTS:
            boolean executed = com.mineshaft.mineshaftapi.manager.event.EventHandler.handleHardcodedEvents(player,item,events,e);
            if (executed && events.size() == 1) return;
        }

        /*
          HANDLING DYNAMIC EVENTS
          (Not hardcoded)
          */

        boolean cannotFire = (MineshaftApi.getInstance().getCooldownManager().hasCooldown(player.getUniqueId(), uniqueId));

        if (cannotFire) {
            return;
        }

        // Shots per second
        double firingSpeed = ItemManager.getItemNbtRangedStat(item, RangedItemStats.FIRING_SPEED_CUSTOM);
        double firingCooldown = 1 / firingSpeed;

        if (firingSpeed > 0) {
            // Firing cooldown
            // Apply cooldown
            MineshaftApi.getInstance().getCooldownManager().addPlayerCooldown(player.getUniqueId(), uniqueId, Duration.ofMillis((long) (firingCooldown * 1000)));
        } else {
            MineshaftApi.getInstance().getCooldownManager().addPlayerCooldown(player.getUniqueId(), uniqueId, Duration.ofMillis(400L));
        }

        // send item cooldown animation
        if (MineshaftApi.getInstance().getConfigManager().enableItemCooldownAnimation()) {
            int delayInTicks = (int) (firingCooldown / 20);

            Item handItem = ((CraftPlayer) player).getHandle().getItemInHand(InteractionHand.MAIN_HAND).getItem();
            ResourceLocation cooldownGroup = BuiltInRegistries.ITEM.getKey(handItem);
            ((CraftPlayer) player).getHandle().connection.send(new ClientboundCooldownPacket(cooldownGroup, delayInTicks));
        }

        if(events==null)return;
        for (String event : events) {
            if (EventManager.isHardcoded(event)) {
                break;
            }

            // Cancel the event if it should be cancelled
            e.setCancelled(true);
            EventManager eventManager = MineshaftApi.getInstance().getEventManagerInstance();
            Event executableEvent = eventManager.getEvent(event, item);
            if (executableEvent instanceof BeamEvent) {
                String sound = ((BeamEvent) executableEvent).getSound();
                if (sound != null) {
                    player.getLocation().getWorld().playSound(player.getLocation(), sound, 5.0f, 1.0f);
                }
            }
            eventManager.runEvent(executableEvent, player.getLocation(), player.getUniqueId(), player);
        }
    }

}
