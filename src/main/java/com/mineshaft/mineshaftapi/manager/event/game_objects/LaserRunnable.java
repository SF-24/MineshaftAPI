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
import com.mineshaft.mineshaftapi.manager.entity.display.DisplayManager;
import com.mineshaft.mineshaftapi.manager.entity.display.DisplayType;
import com.mineshaft.mineshaftapi.manager.event.event_subclass.BeamEvent;
import com.mineshaft.mineshaftapi.manager.event.fields.TriggerType;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.*;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.UUID;

public class LaserRunnable extends BukkitRunnable {

    Location location;
    Vector dir;

    DisplayType displayType = DisplayType.ITEM;
    TriggerType target = TriggerType.ENTITY;

    boolean flipped = false;
    double dist = 0;
    int flyDistance = 40;
    int speed = 4;
    BeamEvent event = new BeamEvent();
    int particle = 10;
    Display projectile = null;
    int speedCount = 4;

    boolean hasProjectile;
    boolean hideParticles;

    UUID casterId = null;

    double t = 0;

    Location loc;
    private Object stack;

    public LaserRunnable(BeamEvent event, Location loc) {
        if(loc.getWorld()==null) return;
        this.location=loc;
        this.loc=location;
        this.event = event;
        dir=location.getDirection().normalize();
        speed = event.getSpeed();
        target=event.getTarget();
        this.flyDistance=event.getFlyDistance();
        this.speedCount=this.speed;
        this.start();
    }

    public void setProjectile(DisplayType displayType, Object stack) {
        hasProjectile = true;
        hideParticles = true;
        this.stack=stack;
        this.displayType=displayType;
        this.projectile = DisplayManager.generateDisplay(displayType,stack,loc);
    }

    public void setCaster(UUID uuid) {
        this.casterId=uuid;
    }

    public void start() {
        runTaskTimer(MineshaftApi.getInstance(),0,1);
    }

    @Override
    public void run() {

        if(t==0) {
            loc=location;
        }

        if(this.loc==null) {
            Logger.logError("Location (this.loc) is null in class LaserRunnable");
            this.cancel();
        }

        if(speed<1||speedCount<1) {
            this.speedCount=1;
        } else if(speed>40) {
            this.speedCount=40;
        }

        for(int i = 0; i<speedCount; i++) {
            t += 0.25;
            dist=t;

            double x = dir.getX() * dist;
            double y = dir.getY() * dist /*+ 1.5*/;
            double z = dir.getZ() * dist;

            loc.add(x,y,z);

            if(hasProjectile&&(projectile != null)) {
                projectile.remove();
                projectile=DisplayManager.generateDisplay(displayType,stack,loc);
            }

            if (/*!hideParticles &&*/ !loc.getBlock().getType().equals(Material.AIR) && !event.getIgnoredBlocks().contains(loc.getBlock().getType())) {
                HitResponse hitResponse = HitManager.triggerBlockHit(event,casterId,loc.clone(),loc.clone().subtract(x,y,z),hideParticles);
                if(hitResponse.isCancelled) {
                    this.cancel();
                }
            } else {

                LivingEntity livingEntityTarget = null;
                boolean foundEntity = false;

                double distance = 10;
                // IF SPELL HITS ENTITY
                for (Entity entityIteration : loc.getWorld().getNearbyEntities(loc, 1.75, 2.0, 1.75)) { //getChunk().getEntities()) {

                    BoundingBox boundingBox = entityIteration.getBoundingBox();
                    boundingBox.expand(0.1,0.1,0.1,0.1,0.1,0.1);
                    if((!flipped && !entityIteration.getUniqueId().equals(casterId)) && boundingBox.contains(loc.getX(),loc.getY(),loc.getZ())) {
                        if (entityIteration instanceof LivingEntity && !(entityIteration instanceof ItemFrame)) {
                            foundEntity = true;
                            // entity detected
                            if (distance>entityIteration.getLocation().distance(loc)) {
                                distance = entityIteration.getLocation().distance(loc);
                                livingEntityTarget=(LivingEntity)entityIteration;
                            }
                        }
                    }
                }

                if(foundEntity && livingEntityTarget != null) {
                    // IF ENTITY IS NOT CASTER OR WAS CASTER BUT SPELL BOUNCED OFF SHIELD
                    if (t>0.5) {
                        HitResponse hitResponse = HitManager.triggerEntityHit(event, casterId, livingEntityTarget);
                        if(hitResponse.isCancelled) {
                            this.cancel();
                        }
                    }
                }
            }


            //PARTICLE

            if(event.getParticleCount()>-1) {
                if (event.getParticleType().equals(Particle.DUST)) {
                    loc.getWorld().spawnParticle(Particle.DUST, loc, 0, 0.2, 0, 0, 5, new Particle.DustOptions(event.getColour(), event.getSize()), true);
                    loc.getWorld().spawnParticle(Particle.DUST, loc, 0, 0.2, 0, 0, 5, new Particle.DustOptions(event.getColour(), event.getSize()), true);
                } else {
                    loc.getWorld().spawnParticle(event.getParticleType(), loc, event.getParticleCount(), 0, 0 ,0, 0);
                }
            } else {
                Bukkit.getServer().getOnlinePlayers().iterator().next().sendMessage("no particles");
            }

            loc.subtract(x, y, z);

            if (t >= flyDistance || t>=100) {
                this.cancel();
            }
        }
    }
}
