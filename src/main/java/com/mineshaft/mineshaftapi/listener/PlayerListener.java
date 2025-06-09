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
import com.mineshaft.mineshaftapi.dependency.world_guard.PlayerRegionEventManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import com.mineshaft.mineshaftapi.manager.ui.SidebarManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // Make a new json player manager
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player, JsonProfileBridge.getCurrentProfile(player));

        if(MineshaftApi.getInstance().getConfigManager().getSidebarEnabled()) {
            SidebarManager.displayScoreboard(player);
        }

        PlayerRegionEventManager.testRegions(player);

//        String randomKey = UUID.randomUUID().toString();
//        NotificationSender.sendToastPopup(player, randomKey,NotificationSender.buildAdvancement(randomKey, new ItemStack(Material.DEBUG_STICK), "Welcome", "", null, AdvancementType.TASK, true));
    }

    @EventHandler(ignoreCancelled = true)
    public void PlayerTeleportEvent(PlayerTeleportEvent e) {
        PlayerRegionEventManager.testRegions(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        if (e.getTo().distanceSquared(e.getFrom()) > 0) {
            PlayerRegionEventManager.testRegions(e.getPlayer());
        }
    }
}
