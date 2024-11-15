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
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import io.lumine.mythic.bukkit.utils.network.messaging.codec.Message;
import org.bukkit.Warning;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {

    private HashMap<UUID, Cache<UUID, Long>> itemCacheMap = new HashMap<>();
    private HashMap<UUID, Map<UUID, Instant>> itemCooldownMap = new HashMap<>();

    // Important: time in milliseconds
    @Deprecated
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
        Map<UUID, Instant> newCooldown;

        // check cache map for cooldown
        if(itemCacheMap.containsKey(uuid)) {
            cooldown=itemCacheMap.get(uuid);
            return cooldown.asMap().containsKey(itemId);

        // check new cooldown map for cooldown
        } else if(itemCooldownMap.containsKey(uuid)) {
            newCooldown=itemCooldownMap.get(uuid);
            if(newCooldown.containsKey(uuid)) {
                Instant cooldownInstant = newCooldown.get(itemId);
                return cooldownInstant!=null && Instant.now().isBefore(cooldownInstant);
            }
        }
        return false;
    }

    public void addPlayerCooldown(UUID uuid, UUID itemId, Duration duration) {
        Map<UUID, Instant> cooldown;
        if(itemCooldownMap.containsKey(uuid)) {
            cooldown=itemCooldownMap.get(uuid);
        } else {
            cooldown=new HashMap<>();
        }
        cooldown.put(itemId, Instant.now().plus(duration));
        itemCooldownMap.put(uuid,cooldown);
    }

    // Does not work with old cooldown. Completely disregards it
    public Duration getPlayerCooldown() {
        // TODO: add functionality
        return null;
    }

}
