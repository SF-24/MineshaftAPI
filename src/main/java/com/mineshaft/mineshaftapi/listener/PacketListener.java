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
import com.mineshaft.mineshaftapi.manager.event.PendingAbilities;
import com.mineshaft.mineshaftapi.manager.item.ClientItemManager;
import com.mineshaft.mineshaftapi.manager.player.PlayerAttackManager;
import com.mineshaft.mineshaftapi.util.Logger;
import io.netty.channel.*;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PacketListener implements Listener {

    public void inject(Player player) {
        ChannelDuplexHandler channelHandler = new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
                // Override client-bound item packets
//                if(packet instanceof ClientboundSetCursorItemPacket && ClientItemManager.isParsable(((ClientboundSetCursorItemPacket) packet).contents().getBukkitStack())) {
//                    try {
//                        ItemStack parsed = ClientItemManager.parseItem(((ClientboundSetCursorItemPacket) packet).contents().getBukkitStack());
//                        if(parsed.getType()!=Material.AIR) {
//                            packet = new ClientboundSetCursorItemPacket(
//                                    ((CraftItemStack) parsed).handle
//                            );
//                        }
//                    } catch (Exception ignored) {
//                        packet = new ClientboundSetCursorItemPacket(
//                            new net.minecraft.world.item.ItemStack(AirItem.byId(0))
//                        );
//                    }
                Object initial = packet;
                if(packet instanceof ClientboundSetPlayerInventoryPacket && ClientItemManager.isParsable(((ClientboundSetPlayerInventoryPacket) packet).contents().getBukkitStack())) {
                    try {
                        ItemStack parsed = ClientItemManager.parseItem(((ClientboundSetPlayerInventoryPacket) packet).contents().getBukkitStack());
                        packet = new ClientboundSetPlayerInventoryPacket(
                                ((ClientboundSetPlayerInventoryPacket) packet).slot(), ((CraftItemStack) parsed).handle
                        );
                    } catch (Exception ignored) {
                        super.write(ctx, initial, promise);

                    }
                } else if(packet instanceof ClientboundContainerSetSlotPacket && ClientItemManager.isParsable(((ClientboundContainerSetSlotPacket) packet).getItem().getBukkitStack())) {
                    try {
                        ItemStack parsed = ClientItemManager.parseItem(((ClientboundContainerSetSlotPacket) packet).getItem().getBukkitStack());
                        packet = new ClientboundContainerSetSlotPacket(
                                ((ClientboundContainerSetSlotPacket) packet).getContainerId(),
                                ((ClientboundContainerSetSlotPacket) packet).getStateId(),
                                ((ClientboundContainerSetSlotPacket) packet).getSlot(),
                                ((CraftItemStack) parsed).handle
                        );
                    } catch (Exception ignored) {
                        super.write(ctx, initial, promise);
                    }
                }
                super.write(ctx, packet, promise);
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                // Observer packets coming in
                if(packet instanceof ServerboundPlayerActionPacket) {
                    if(((ServerboundPlayerActionPacket) packet).getAction().equals(ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM)) {
                        Logger.logDebug("Packet intercepted!");

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

        pipeline.addBefore("packet_handler", "Mineshaft_"+player.getName(), channelHandler);
    }

    public void stop(Player player) {
        Channel channel = ((CraftPlayer)player).getHandle().connection.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("Mineshaft_"+player.getName());
            return null;
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ChannelPipeline pipeline = ((CraftPlayer)e.getPlayer()).getHandle().connection.connection.channel.pipeline();
        if (pipeline.get("Mineshaft_"+e.getPlayer().getName()) != null) {
            pipeline.remove("Mineshaft_"+e.getPlayer().getName()); // or just skip adding if you prefer
        }
//        try {
//            stop(e.getPlayer());
//        } catch (Exception ignored) {}
        inject(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        stop(e.getPlayer());
        MineshaftApi.getInstance().getActionManager().removePlayerBlocking(e.getPlayer().getUniqueId());
    }
}
