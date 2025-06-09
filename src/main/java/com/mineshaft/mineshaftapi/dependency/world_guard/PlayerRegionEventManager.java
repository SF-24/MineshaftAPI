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

package com.mineshaft.mineshaftapi.dependency.world_guard;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.events.MineshaftTownDiscoveryEvent;
import com.mineshaft.mineshaftapi.manager.player.json.JsonDiscoveryBridge;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerRegionEventManager {

    public static void onEnterRegion(Player player, ProtectedRegion region) {
        if(MineshaftApi.getInstance().getRegionManager().isRegionInTown(region) && !JsonDiscoveryBridge.hasDiscoveredTown(player, MineshaftApi.getInstance().getRegionManager().getTown(region))) {
            MineshaftTownDiscoveryEvent townDiscoveryEvent = new MineshaftTownDiscoveryEvent(player, MineshaftApi.getInstance().getRegionManager().getTown(region));
            Bukkit.getPluginManager().callEvent(townDiscoveryEvent);

            // If the event is not cancelled, discover it for the player
            if(!townDiscoveryEvent.isCancelled()) {
                JsonDiscoveryBridge.addDiscoveredTown(player, townDiscoveryEvent.getTown());
            }
        }
    }

    public static void testRegions(Player player) {
        ApplicableRegionSet applicableRegions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getLocation().getWorld())).getApplicableRegions(BlockVector3.at((int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ()), RegionQuery.QueryOption.NONE);
        for (ProtectedRegion region : applicableRegions.getRegions()) {
            onEnterRegion(player,region);
        }
    }

}
