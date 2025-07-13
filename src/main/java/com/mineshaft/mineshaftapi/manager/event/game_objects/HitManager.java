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
import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import com.mineshaft.mineshaftapi.util.Logger;
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
    public static HitResponse triggerBlockHit(TargeterEvent event, UUID casterId, Location hitLocation, Location previousLocation, boolean hideParticles) {
//        Logger.logDebug("Block hit " + hitLocation);

        HitResponse hitResponse = new HitResponse();
        //PLAY FIZZLE SOUND
        if(hitLocation.getBlock().getBoundingBox().contains(hitLocation.getX(),hitLocation.getY(),hitLocation.getZ())) {
            if(!hideParticles) {

                // Pitch, then volume
                hitLocation.getWorld().playSound(hitLocation, Sound.BLOCK_FIRE_EXTINGUISH, 1.0F /*volume, was 0.75*/, 1.0f);
                hitLocation.getWorld().spawnParticle(Particle.SMOKE, hitLocation, 50, 0, 0, 0, 0);
//                Logger.logDebug("Block hit particles");
            }
            hitResponse.isCancelled=true;
            //onHitBlock(event.getOnHitBlock(),casterId,hitLocation.getBlock(),null);
        }
        return hitResponse;
    }

    // When a living entity is hit
    public static HitResponse triggerEntityHit(TargeterEvent event, UUID casterId, LivingEntity livingEntityTarget) {
        HitResponse hitResponse = new HitResponse();

        // get onHit stuff
        if(event.getOnHitPlayer()!=null && !event.getOnHitPlayer().isEmpty() && livingEntityTarget instanceof Player) {
            onHitPlayer(event.getOnHitPlayer(),casterId,livingEntityTarget);
            hitResponse.affectsEntity=true;
        }
        if(event.getOnHitEntity()!=null) {
            Logger.logDebug("Entity hit " + event.getOnHitEntity());
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
            if (!event.getEventType().equals(EventType.BEAM)) {
                MineshaftApi.getInstance().getEventManagerInstance().runEvent(event, block.getLocation(), casterId, previousBlock);
            }
        }
    }

    public static void onHitEntity(List<Event> entityHitEvent, UUID casterId, LivingEntity targetEntity) {
//        Logger.logDebug("entity hit onhitmanager");
        Logger.logDebug("triggering on hit");
        for(Event event : entityHitEvent) {
            if(event==null) {
                Logger.logDebug("Found null event nested in a beam event!");
                return;
            }
            // Null!
            if (!event.getEventType().equals(EventType.BEAM)) {

                Logger.logDebug("Event: " + event.getClass().getName());
                MineshaftApi.getInstance().getEventManagerInstance().runEvent(event, targetEntity.getLocation(), casterId, targetEntity);
            } else {
                Logger.logDebug("nested beam event");
            }
        }
    }

    public static void onHitPlayer(List<Event> playerHitEvent, UUID casterId, LivingEntity targetPlayer) {
        for(Event event : playerHitEvent) {
            if (!event.getEventType().equals(EventType.BEAM)) {
                MineshaftApi.getInstance().getEventManagerInstance().runEvent(event, targetPlayer.getLocation(), casterId, targetPlayer);
            } else {
                Logger.logDebug("nested beam event");
            }
        }
    }

}
