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
import com.mineshaft.mineshaftapi.manager.block.BlockManager;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class EventHandler {

    public static boolean handleHardcodedEvents(Player player, ItemStack item, ArrayList<String> events, PlayerInteractEvent e) {
        // HARDCODED EVENTS:

        // detect right and left clicks
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return false;
        }

        // On parry
        if (events.contains("parry")) {
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
        } else if (events.contains("power_attack")) {
            // Player is parrying:
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

        // Power attack
        return false;
    }
}
