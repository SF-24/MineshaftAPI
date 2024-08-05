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

package com.mineshaft.mineshaftapi.manager.game_objects;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.event.event_subclass.BeamEvent;
import com.mineshaft.mineshaftapi.manager.event.fields.LocalEvent;
import com.mineshaft.mineshaftapi.manager.event.fields.TriggerType;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.logging.Level;

public class LaserRunnable extends BukkitRunnable {

    Location location;
    Vector dir;

    TriggerType target = TriggerType.ENTITY;

    boolean flipped = false;
    double dist = 0;
    int flyDistance = 40;
    int speed = 4;
    BeamEvent event = new BeamEvent();
    int particle = 10;
    int speedCount = 4;

    UUID casterId = null;

    double t = 0;

    Location loc;

    public LaserRunnable(BeamEvent event, Location loc) {
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

            // flip on hit barrier
            if(loc.getBlock().getType().equals(Material.BARRIER)) {
                flipped = true;
            }

            // IF SPELL HITS BLOCK (not air)
            if (!loc.getBlock().getType().equals(Material.AIR)) {
                //PLAY FIZZLE SOUND
                //loc.getWorld().spawnParticle(Particle.FLAME, loc,0,0.2,0,0,0.1);
                loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 10.0f, 1.0f);
                this.cancel();

                // if hits barrier block or air
            } else/* if(!loc.getBlock().getType().equals(Material.BARRIER))*/ {
                //

                LivingEntity e = null;
                boolean foundEntity = false;

                // IF SPELL HITS ENTITY
                for (Entity en : loc.getWorld().getNearbyEntities(loc, 1.75, 2.0, 1.75)) { //getChunk().getEntities()) {
                    float distance = 10;

                    if ((!flipped && !en.getUniqueId().equals(casterId)) && en.getLocation().distance(loc) < 1.75) {
                        if (en instanceof LivingEntity && !(en instanceof ItemFrame)) {
                            foundEntity = true;
                            // entity detected
                            if (distance>en.getLocation().distance(loc)) {
                                e=(LivingEntity)en;
                            }
                        }
                    }
                }

                if(foundEntity && e != null) {
                    // IF ENTITY IS NOT CASTER OR WAS CASTER BUT SPELL BOUNCED OFF SHIELD
                    if (t>0.5) {
                        boolean affectsEntity = false;
                        LocalEvent localEvent = null;
                        LocalEvent playerEvent = null;
                        Object param = null;
                        Object playerParam = null;

                        // get onHit stuff
                        if(event.getOnHitEntity()!=null) {
                            localEvent=event.getOnHitEntity().get(0);
                            param=event.getOnHitEntityObject(LocalEvent.DAMAGE);
                            playerEvent=event.getOnHitEntity().get(0);
                            playerParam=event.getOnHitEntityObject(LocalEvent.DAMAGE);
                            affectsEntity=true;
                        }
                        if(event.getOnHitPlayer()!=null && e instanceof Player) {
                            playerEvent=event.getOnHitPlayer().get(0);
                            playerParam=event.getOnHitPlayerObject(LocalEvent.DAMAGE);
                            affectsEntity=true;
                        }

                        // test if entity has a shield or similar
                        boolean isReflected = false;

                        // TEST IF ENTITY IS TARGET
                        if (affectsEntity && !isReflected) {

                            this.cancel();

                            Object switchParam = param;
                            LocalEvent switchEvent = localEvent;
                            if(e instanceof Player) {
                                switchParam=playerParam;
                                switchEvent=playerEvent;
                            }

                            switch (switchEvent) {
                                case DAMAGE:
                                    double damage = 5;
                                    if(switchParam instanceof Double ) {
                                        damage = (double) switchParam;
                                    } else if(switchParam instanceof Integer) {
                                        damage = (int) switchParam;
                                    }

                                    if(Bukkit.getPlayer(casterId)!=null) {
                                        e.damage(damage, Bukkit.getPlayer(casterId));
                                    } else {
                                        e.damage(damage);
                                    }
                                    break;
                                case EXPLODE:
                                    break;
                                case SET_BLOCK:
                                    break;
                            }
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
