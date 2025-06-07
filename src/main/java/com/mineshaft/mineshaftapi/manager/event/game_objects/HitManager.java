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

package com.mineshaft.mineshaftapi.manager.event.game_objects;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.event_subclass.TargeterEvent;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class HitManager {

    // When a block is hit
    public static HitResponse triggerBlockHit(TargeterEvent event, UUID casterId, Location hitLocation, Location previousLocation, boolean hitParticles) {
        HitResponse hitResponse = new HitResponse();
        //PLAY FIZZLE SOUND
        if(hitLocation.getBlock().getBoundingBox().contains(hitLocation.getX(),hitLocation.getY(),hitLocation.getZ())) {
            if(hitParticles) {
                hitLocation.getWorld().playSound(hitLocation, Sound.BLOCK_FIRE_EXTINGUISH, 0.75f, 1.0f);
                hitLocation.getWorld().spawnParticle(Particle.SMOKE, hitLocation, 50, 0, 0, 0, 0);
            }
            hitResponse.isCancelled=true;
            onHitBlock(event.getOnHitBlock(),casterId,hitLocation.getBlock(),null);
        }
        return hitResponse;
    }

    // When a living entity is hit
    public static HitResponse triggerEntityHit(TargeterEvent event, UUID casterId, LivingEntity livingEntityTarget) {
        HitResponse hitResponse = new HitResponse();

        // get onHit stuff
        if(event.getOnHitPlayer()!=null && livingEntityTarget instanceof Player) {
            onHitPlayer(event.getOnHitPlayer(),casterId,livingEntityTarget);
            hitResponse.affectsEntity=true;
        } else if(event.getOnHitEntity()!=null) {
            HitManager.onHitEntity(event.getOnHitEntity(),casterId,livingEntityTarget);
            hitResponse.affectsEntity=true;
        }

        // TODO: test if entity has a shield or similar
        hitResponse.isReflected = false;

        // TEST IF ENTITY IS TARGET
        if (hitResponse.affectsEntity && !hitResponse.isReflected) {
            hitResponse.isCancelled=true;
        }
        return hitResponse;
    }


    /**
     * Trigger hits:
     * */

    public static void onHitBlock(List<Event> blockHitEvent, UUID casterId, Block block, Block previousBlock) {
        for(Event event : blockHitEvent) {
            MineshaftApi.getInstance().getEventManagerInstance().runEvent(event, block.getLocation(),casterId,previousBlock);
        }
    }

    public static void onHitEntity(List<Event> entityHitEvent, UUID casterId, LivingEntity targetEntity) {
        for(Event event : entityHitEvent) {
            MineshaftApi.getInstance().getEventManagerInstance().runEvent(event, targetEntity.getLocation(),casterId,targetEntity);
        }
    }

    public static void onHitPlayer(List<Event> playerHitEvent, UUID casterId, LivingEntity targetPlayer) {
        for(Event event : playerHitEvent) {
            MineshaftApi.getInstance().getEventManagerInstance().runEvent(event, targetPlayer.getLocation(),casterId,targetPlayer);
        }
    }

}
