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
import com.mineshaft.mineshaftapi.manager.item.fields.ItemSubcategory;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemSubcategoryProperty;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.PacketUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ActionManager {

    Long parryWindow = 340L;//320L;
    Long blockingCooldown = 200L;
    Long lightAttackCooldown = 500L;
    Long attackCooldown = 1000L;
    Long heavyAttackCooldown = 2500L;

    private ArrayList<UUID> playersBlocking = new ArrayList<>();
    private HashMap<UUID, ItemSubcategory> powerAttackPrepare = new HashMap<>();
    private HashMap<UUID, CooldownActionType> cooldownActions = new HashMap<>();
    private Cache<UUID, Long> parryTimer = CacheBuilder.newBuilder().expireAfterWrite(parryWindow, TimeUnit.MILLISECONDS).build();
    private Cache<UUID, Long> blockCooldown = CacheBuilder.newBuilder().expireAfterWrite(blockingCooldown, TimeUnit.MILLISECONDS).build();
    private Cache<UUID, Long> powerAttackCooldown = CacheBuilder.newBuilder().expireAfterWrite(attackCooldown, TimeUnit.MILLISECONDS).build();
    private Cache<UUID, Long> powerAttackCooldownLight = CacheBuilder.newBuilder().expireAfterWrite(lightAttackCooldown, TimeUnit.MILLISECONDS).build();
    private Cache<UUID, Long> powerAttackCooldownHeavy = CacheBuilder.newBuilder().expireAfterWrite(heavyAttackCooldown, TimeUnit.MILLISECONDS).build();

    public void addPlayerBlocking(UUID uuid) {
        this.playersBlocking.add(uuid);
        this.parryTimer.put(uuid, System.currentTimeMillis()+parryWindow);
        Logger.logInfo("Adding blocking player: " + uuid);
    }

    public void removePlayerBlocking(UUID uuid) {
        if(playersBlocking.contains(uuid)) {
            this.playersBlocking.remove(uuid);
            addCooldown(Bukkit.getPlayer(uuid),CooldownActionType.BLOCKING);
        }
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

    public void addCooldown(Player player, CooldownActionType type) {
        // send item cooldown animation
        if(MineshaftApi.getInstance().getConfigManager().enableItemCooldownAnimation()) {
            int cooldownTicks = 4; // 1/5 of a second
            PacketUtil.sendCooldown(player, player.getInventory().getItemInMainHand(),cooldownTicks);

            switch (type) {
                case BLOCKING,THROW,SMOKING_PIPE -> cooldownTicks = 4;
                case POWER_ATTACK -> {
                    cooldownActions.put(player.getUniqueId(), type);
                    cooldownTicks = 20;
                    powerAttackCooldown.put(player.getUniqueId(), System.currentTimeMillis()+attackCooldown);
                }
                case POWER_ATTACK_HEAVY -> {
                    cooldownTicks=50;
                    cooldownActions.put(player.getUniqueId(), type);
                    powerAttackCooldownHeavy.put(player.getUniqueId(), System.currentTimeMillis()+heavyAttackCooldown);
                }
                case POWER_ATTACK_LIGHT -> {
                    cooldownTicks=10;
                    cooldownActions.put(player.getUniqueId(), type);
                    powerAttackCooldownLight.put(player.getUniqueId(), System.currentTimeMillis()+lightAttackCooldown);
                }
                default -> blockCooldown.put(player.getUniqueId(), System.currentTimeMillis()+ blockingCooldown);
            }

//            MineshaftApi.getInstance().getServer().getScheduler().runTask(MineshaftApi.getInstance(), () -> {
//                player.setCooldown(player.getInventory().getItemInMainHand().getType(), cooldownTicks);
//            });
        }
    }

    public boolean canBlock(UUID uuid) {
        if(this.blockCooldown.asMap().containsKey(uuid)) return false;
        return true;
    }

    /**
     * POWER ATTACK:
     * */

    public void addPlayerPowerAttack(UUID uuid, ItemSubcategory subcategory) {
        this.powerAttackPrepare.put(uuid, subcategory);
        Logger.logInfo("Adding power attack player: " + uuid);
    }

    public void removePlayerPowerAttack(UUID uuid) {
        if(powerAttackPrepare.containsKey(uuid)) {
            if(powerAttackPrepare.get(uuid).getPropertyList().contains(ItemSubcategoryProperty.LIGHT)) {
                addCooldown(Bukkit.getPlayer(uuid), CooldownActionType.POWER_ATTACK_LIGHT);
            } else if(powerAttackPrepare.get(uuid).getPropertyList().contains(ItemSubcategoryProperty.HEAVY)) {
                addCooldown(Bukkit.getPlayer(uuid), CooldownActionType.POWER_ATTACK_HEAVY);
            } else {
                addCooldown(Bukkit.getPlayer(uuid), CooldownActionType.POWER_ATTACK);
            }
            this.powerAttackPrepare.remove(uuid);
        }
        Logger.logInfo("Removed power attack player: " + uuid);
    }

    public boolean isPlayerPowerAttack(UUID uuid) {
        return this.powerAttackPrepare.containsKey(uuid);
    }

    public CooldownActionType getPlayerPowerAttackType(UUID uuid) {
        if(this.powerAttackPrepare.get(uuid).getPropertyList().contains(ItemSubcategoryProperty.LIGHT)) {
            return CooldownActionType.POWER_ATTACK_LIGHT;
        } else if(this.powerAttackPrepare.get(uuid).getPropertyList().contains(ItemSubcategoryProperty.HEAVY)) {
            return CooldownActionType.POWER_ATTACK_HEAVY;
        } else {
            return CooldownActionType.POWER_ATTACK;
        }
    }

    public boolean canDoPowerAttack(UUID uuid) {
        return !this.powerAttackCooldown.asMap().containsKey(uuid) && !this.powerAttackCooldownLight.asMap().containsKey(uuid) && !this.powerAttackCooldownHeavy.asMap().containsKey(uuid);
    }

}
