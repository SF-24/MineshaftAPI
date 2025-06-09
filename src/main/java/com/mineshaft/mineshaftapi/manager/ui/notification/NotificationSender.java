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

package com.mineshaft.mineshaftapi.manager.ui.notification;

import com.mineshaft.mineshaftapi.dependency.world_guard.Town;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minecraft.advancements.*;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.*;

public class NotificationSender {

    public static void sendTitle(Player player, String title, String subtitle, double fadeIn, double duration, double fadeOut) {
        player.showTitle(Title.title(Component.text(title),Component.text(subtitle), Title.Times.times(Duration.ofMillis((long) (fadeIn*1000)),Duration.ofMillis((long) (duration*1000)),Duration.ofMillis((long) (fadeOut*1000)))));
    }

    public static void sendTownDiscoveryTitle(Player player, Town town) {
        sendDiscoveryTitle(player,"Town", town.getName());
    }

    public static void sendDiscoveryTitle(Player player, String category, String name) {
        sendTitle(player,"§f§lCODEX UPDATED", "§8" + category + ": §3" + name, 1,2.5,1);
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.75F);
    }

    public static void sendAdvancement(Player player, String id, Advancement advancement, boolean add) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

        List<AdvancementHolder> advancements = new ArrayList<>();
        Set<ResourceLocation> removedAdvancements = new HashSet<>();
        Map<ResourceLocation, AdvancementProgress> progress = new HashMap<>();

        ResourceLocation resourceLocation = ResourceLocation.parse(id);

        //Populate Lists
        if(add) {
            advancements.add(new AdvancementHolder(resourceLocation,advancement));
            progress.put(resourceLocation, null);
        } else {
            removedAdvancements.add(resourceLocation);
        }

        //Create Packet
        ClientboundUpdateAdvancementsPacket packet = new ClientboundUpdateAdvancementsPacket(false, advancements, removedAdvancements, progress);
    }

    public static void sendToastPopup(Player player, String key, Advancement advancement) {
        sendAdvancement(player, key, advancement,true);
        sendAdvancement(player, key, advancement,false);
    }

    public static Advancement buildAdvancement(String key, ItemStack displayItem, String title, String description, ResourceLocation background, AdvancementType type, boolean showToast) {
        net.minecraft.world.item.ItemStack itemStack = net.minecraft.world.item.ItemStack.fromBukkitCopy(displayItem);
        DisplayInfo displayInfo = new DisplayInfo(itemStack,
                net.minecraft.network.chat.Component.literal(title),
                net.minecraft.network.chat.Component.literal(description),
                Optional.of(background),
                type, showToast,
                false,
                false);

        final HashMap<String, Criterion<?>> criteria = new HashMap<>();

        Advancement advancement = new Advancement(Optional.empty(), Optional.of(displayInfo),AdvancementRewards.EMPTY,criteria,AdvancementRequirements.EMPTY,false);
        return advancement;
    }

}
