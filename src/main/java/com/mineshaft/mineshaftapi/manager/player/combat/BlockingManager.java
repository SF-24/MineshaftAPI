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

package com.mineshaft.mineshaftapi.manager.player.combat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.util.Logger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BlockingManager {

    Long parryWindow = 340L;//320L;
    Long cooldown = 200L;

    private ArrayList<UUID> playersBlocking = new ArrayList<>();
    private Cache<UUID, Long> parryTimer = CacheBuilder.newBuilder().expireAfterWrite(parryWindow, TimeUnit.MILLISECONDS).build();
    private Cache<UUID, Long> blockCooldown = CacheBuilder.newBuilder().expireAfterWrite(cooldown, TimeUnit.MILLISECONDS).build();

    public void addPlayerBlocking(UUID uuid) {
        this.playersBlocking.add(uuid);
        this.parryTimer.put(uuid, System.currentTimeMillis()+parryWindow);
        Logger.logInfo("Adding blocking player: " + uuid);
    }

    public void removePlayerBlocking(UUID uuid) {
        this.playersBlocking.remove(uuid);
        addCooldown(Bukkit.getPlayer(uuid));
        Logger.logInfo("Removed blocking player: " + uuid);
    }

    public boolean isPlayerBlocking(UUID uuid) {
        return this.playersBlocking.contains(uuid);
    }

    public BlockingType getBlockingType(UUID uuid) {
        if(parryTimer.asMap().containsKey(uuid)) {
            return BlockingType.PARRY;
        } else if(playersBlocking.contains(uuid)) {
            return BlockingType.BLOCK;
        }
        return null;
    }

    public void removePlayerParry(UUID uuid) {
        this.parryTimer.put(uuid, System.currentTimeMillis());
    }

    public void addCooldown(Player player) {
        // send item cooldown animation
        if(MineshaftApi.getInstance().getConfigManager().enableItemCooldownAnimation()) {
            Item handItem = ((CraftPlayer) player).getHandle().getItemInHand(InteractionHand.MAIN_HAND).getItem();
            int cooldownTicks = 4; // 1/5 of a second
            ResourceLocation cooldownGroup = BuiltInRegistries.ITEM.getKey(handItem);
            ((CraftPlayer) player).getHandle().connection.send(new ClientboundCooldownPacket(cooldownGroup, (cooldownTicks)));
//            MineshaftApi.getInstance().getServer().getScheduler().runTask(MineshaftApi.getInstance(), () -> {
//                player.setCooldown(player.getInventory().getItemInMainHand().getType(), cooldownTicks);
//            });
        }
        blockCooldown.put(player.getUniqueId(), System.currentTimeMillis()+cooldown);
    }

    public boolean canBlock(UUID uuid) {
        if(this.blockCooldown.asMap().containsKey(uuid)) return false;
        return true;
    }

}
