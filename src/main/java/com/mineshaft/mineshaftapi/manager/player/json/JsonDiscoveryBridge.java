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

package com.mineshaft.mineshaftapi.manager.player.json;

import com.mineshaft.mineshaftapi.dependency.world_guard.Town;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class JsonDiscoveryBridge {

    public static JsonDiscoveryManager getInstance(Player player) {return new JsonDiscoveryManager(player, JsonProfileBridge.getCurrentProfile(player));}

    public static void addDiscoveredTown(Player player, Town town) {
        getInstance(player).addDiscoveredTown(town);
    }

    public static boolean hasDiscoveredTown(Player player, Town town) {
        return getInstance(player).hasDiscoveredTown(town);
    }

    public static ArrayList<Town> getDiscoveredTowns(Player player) {
        return getInstance(player).getDiscoveredTowns();
    }

    public static ArrayList<String> getDiscoveredRegions(Player player) {
        return getInstance(player).getDiscoveredRegions();
    }

}
