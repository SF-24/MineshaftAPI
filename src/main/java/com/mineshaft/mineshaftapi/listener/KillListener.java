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

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class KillListener implements Listener {

    /**
     * Guide:
     * <a href="https://www.youtube.com/watch?v=6LSScMdk0gU">...</a>
     */

    @EventHandler
    void deathEvent(EntityDeathEvent e) {
        if(e.getEntity() instanceof Player) {
            // TODO:
        }
    }

    void spawnCorpse(EntityDeathEvent e) {
        // Spawn corpse entity
        Player player = (Player) e.getEntity();

        // Get player data
        CraftPlayer craftPlayer=(CraftPlayer)player.getPlayer();
        Property texture = (Property) craftPlayer.getProfile().getProperties().get("texture").toArray()[0];
        UUID uuid;
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
        gameProfile.getProperties().put("textures",new Property(texture.value(), texture.signature()));

        CraftServer craftServer = (CraftServer)Bukkit.getServer();
        MinecraftServer server = craftServer.getServer().getConnection().getServer();
        ServerLevel level = craftPlayer.getHandle().serverLevel();

        // Create corpse entity
        ServerPlayer corpse = new ServerPlayer(server,level, gameProfile, null);
        corpse.setPos(player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ());

        // set bed and sleep pose
        Location bed = player.getLocation().add(1,0,0);
        corpse.startSleeping(new BlockPos((int) bed.getX(), (int) bed.getY(), (int) bed.getZ()));

        // hide name tag
        // TODO:
    }
}
