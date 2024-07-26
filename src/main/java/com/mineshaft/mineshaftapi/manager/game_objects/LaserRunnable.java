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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class LaserRunnable extends BukkitRunnable {

    Location loc;
    Vector dir;

    TriggerType target = TriggerType.ENTITY;

    boolean flipped = false;
    double dist = 0;
    int flyDistance = 0;
    int speed = 4;
    BeamEvent event = null;
    int particle = 10;

    int t = 0;

    private Location tempLocation;

    public LaserRunnable(BeamEvent event, Location loc) {
        this.event = event;
        this.loc=loc.add(event.getOffset());
        dir=loc.getDirection().normalize();
        speed = event.getSpeed();
        target=event.getTarget();
    }

    public void start() {
        runTaskTimer(MineshaftApi.getInstance(),0,1);
    }

    @Override
    public void run() {
        int speedCount = (int) (speed/20);

        if(t==0) {
            tempLocation=loc;
        }

        for(int i = 0; i<speedCount; i++) {

            t += 0.25;

            // now unused code
            // kept for now
            if(!flipped) {
                dist +=0.25;
            } else {
                dist-=0.25;
            }

            dist=t;

            double x = dir.getX() * dist;
            double y = dir.getY() * dist + 1.5;
            double z = dir.getZ() * dist;

            loc.add(x,y,z);

            if(loc.getBlock().getType().equals(Material.BARRIER)) {
                flipped = true;
            }

            // IF SPELL HITS BLOCK
            if (!loc.getBlock().getType().equals(Material.AIR)) {
                //PLAY FIZZLE SOUND
                loc.getWorld().spawnParticle(Particle.FLAME,tempLocation,0,0.2,0,0,5);
                loc.getWorld().playSound(tempLocation, Sound.BLOCK_FIRE_EXTINGUISH, 10.0f, 1.0f);

                // if hits barrier block
            } else if(!loc.getBlock().getType().equals(Material.BARRIER)) {

                Entity e = null;
                HashMap<UUID, Double> entities = new HashMap<>();
                boolean foundEntity = false;

                // IF SPELL HITS ENTITY
                for (Entity en : loc.getChunk().getEntities()) {
                    if (en.getLocation().distance(loc) < 1.75) {
                        if (en instanceof LivingEntity) {
                            foundEntity = true;
                            entities.put(en.getUniqueId(), en.getLocation().distance(loc));
                        }
                    }
                }

                // if found entity
                if(foundEntity) {
                    double distance = 10;
                    UUID id = null;
                    for (UUID uuid : entities.keySet()) {
                        if (entities.get(uuid) < distance) {
                            distance = entities.get(uuid);
                            id = uuid;
                        }
                    }


                    for (Entity en : loc.getChunk().getEntities()) {
                        if (en.getUniqueId().equals(id)) {
                            e = en;
                        }
                    }
                }

                if(foundEntity && e instanceof LivingEntity) {

                    // IF ENTITY IS NOT CASTER OR WAS CASTER BUT SPELL BOUNCED OF SHIELD
                    if (t>9) {
                        boolean affectsEntity = false;
                        LocalEvent localEvent = null;

                        if(event.getOnHitEntity().iterator().hasNext()) {
                            affectsEntity=true;
                            localEvent=event.getOnHitEntity().iterator().next();
                        } else if(event.getOnHitPlayer()!=null&&e instanceof Player) {
                            affectsEntity=true;
                            localEvent=event.getOnHitPlayer().iterator().next();                        }

                        // test if entity has a shield or similar
                        boolean isReflected = false;

                        // TEST IF ENTITY IS TARGET
                        if (affectsEntity && !isReflected) {

                            this.cancel();

                            switch (localEvent) {
                                case DAMAGE:
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
                    loc.getWorld().spawnParticle(event.getParticleType(), tempLocation, event.getParticleCount());
                }
            }

            loc.subtract(x, y, z);

            if (t >= flyDistance) {
                this.cancel();
            }
        }
    }
}
