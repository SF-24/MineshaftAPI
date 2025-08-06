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

package com.mineshaft.mineshaftapi.manager.block.json;

import org.bukkit.Location;

import java.util.ArrayList;

public class JsonBlockCacheBridge {

    public static JsonBlockCacheManager getInstance(String world) {return new JsonBlockCacheManager(world);}

    public static ArrayList<BlockClass> getBlockCache(String world) {
        return getInstance(world).getBlockCache();
    }

    public static void cacheBlock(BlockClass blockClass) {
        getInstance(blockClass.getLocation().getWorld().getName()).cacheBlock(blockClass);
    }

    public static void removeBlock(Location location) {
        getInstance(location.getWorld().getName()).removeBlock(location);
    }

    public static BlockClass getBlock(Location location) {
        return getInstance(location.getWorld().getName()).getBlock(location);
    }

}
