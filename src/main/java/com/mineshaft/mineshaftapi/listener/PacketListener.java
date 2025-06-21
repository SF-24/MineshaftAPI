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
import com.mineshaft.mineshaftapi.events.MineshaftTownDiscoveryEvent;
import com.mineshaft.mineshaftapi.manager.event.PendingAbilities;
import com.mineshaft.mineshaftapi.manager.event.event_subclass.EventLoader;
import com.mineshaft.mineshaftapi.manager.player.PlayerAttackManager;
import com.mineshaft.mineshaftapi.manager.player.combat.CooldownActionType;
import com.mineshaft.mineshaftapi.util.Logger;
import com.sk89q.worldguard.bukkit.event.debug.LoggingEntityDamageByEntityEvent;
import io.netty.channel.*;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PacketListener implements Listener {

    public void inject(Player player) {
        ChannelDuplexHandler channelHandler = new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                super.write(ctx, msg, promise);
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                // Observer packets coming in
                if(packet instanceof ServerboundPlayerActionPacket) {
                    if(((ServerboundPlayerActionPacket) packet).getAction().equals(ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM)) {
                        MineshaftApi.getInstance().getActionManager().removePlayerBlocking(player.getUniqueId());
                        if(MineshaftApi.getInstance().getActionManager().isPlayerPowerAttack(player.getUniqueId())) {
                            // Execute a power attack
                            PendingAbilities pendingAbilities = MineshaftApi.getInstance().getPendingAbilities(player.getUniqueId());
                            switch (MineshaftApi.getInstance().getActionManager().getPlayerPowerAttackType(player.getUniqueId())) {
                                case POWER_ATTACK_LIGHT -> pendingAbilities.addStrongAttackAbility(1.75,1.75,"entity.dragon_fireball.explode",true);
                                case POWER_ATTACK -> pendingAbilities.addStrongAttackAbility(2.25,2.25,"entity.dragon_fireball.explode",true);
                                case POWER_ATTACK_HEAVY -> pendingAbilities.addStrongAttackAbility(3.0,3.0,"entity.dragon_fireball.explode",true);
                                default -> Logger.logWarning("Cannot identify power attack type for player");
                            }

                            Bukkit.getScheduler().runTaskLaterAsynchronously(MineshaftApi.getInstance(),()->{
                                MineshaftApi.getInstance().removePendingAility(player.getUniqueId(), PendingAbilities.PendingAbilityType.STRONG_ATTACK);
                            },5);

                            Logger.logDebug(pendingAbilities.getPendingAbilities().toString());

                            MineshaftApi.getInstance().setPendingAbilities(player.getUniqueId(), pendingAbilities);
                            MineshaftApi.getInstance().getActionManager().removePlayerPowerAttack(player.getUniqueId());

                            Bukkit.getScheduler().runTaskLater(MineshaftApi.getInstance(), ()-> PlayerAttackManager.makeAttack(player),1/99);
                        }
                    }
                }

                super.channelRead(ctx, packet);
            }
        };

        // Create pipeline (middleman between client and server packets)
        ChannelPipeline pipeline = ((CraftPlayer)player).getHandle().connection.connection.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelHandler);
    }

    public void stop(Player player) {
        Channel channel = ((CraftPlayer)player).getHandle().connection.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        inject(e.getPlayer());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        stop(e.getPlayer());
        MineshaftApi.getInstance().getActionManager().removePlayerBlocking(e.getPlayer().getUniqueId());
    }


}
