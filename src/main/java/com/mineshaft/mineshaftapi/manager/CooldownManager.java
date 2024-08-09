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

package com.mineshaft.mineshaftapi.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {

    private HashMap<UUID, Cache<UUID, Long>> itemCacheMap = new HashMap<>();

    // Important: time in milliseconds
    public void addPlayerKey(UUID uuid, UUID itemId, long timeInMilliseconds) {
        Cache<UUID, Long> cooldown;
        if(itemCacheMap.containsKey(uuid)) {
            cooldown=itemCacheMap.get(uuid);
        } else {
            cooldown = CacheBuilder.newBuilder().expireAfterWrite(400L, TimeUnit.MILLISECONDS).build();
        }
        cooldown.put(itemId, System.currentTimeMillis() + timeInMilliseconds);
        itemCacheMap.put(uuid,cooldown);
    }

    public boolean hasCooldown(UUID uuid, UUID itemId) {
        Cache<UUID, Long> cooldown;

        if(itemCacheMap.containsKey(uuid)) {
            cooldown=itemCacheMap.get(uuid);
        } else {
            return false;
        }
        return cooldown.asMap().containsKey(itemId);
    }

}
