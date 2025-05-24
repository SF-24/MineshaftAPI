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
import com.mineshaft.mineshaftapi.manager.event.fields.LocalEvent;
import com.mineshaft.mineshaftapi.manager.event.fields.TriggerType;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
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

            ArrayList<Material> ignoredBlocks = new ArrayList<>();
            ignoredBlocks.add(Material.VINE);
            ignoredBlocks.add(Material.TALL_GRASS);
            ignoredBlocks.add(Material.SHORT_GRASS);
            ignoredBlocks.add(Material.ROSE_BUSH);
            ignoredBlocks.add(Material.LILAC);
            ignoredBlocks.add(Material.PEONY);
            ignoredBlocks.add(Material.OXEYE_DAISY);
            ignoredBlocks.add(Material.POPPY);
            ignoredBlocks.add(Material.DANDELION);
            ignoredBlocks.add(Material.BLUE_ORCHID);
            ignoredBlocks.add(Material.ALLIUM);
            ignoredBlocks.add(Material.AZURE_BLUET);
            ignoredBlocks.add(Material.ORANGE_TULIP);
            ignoredBlocks.add(Material.RED_TULIP);
            ignoredBlocks.add(Material.PINK_TULIP);
            ignoredBlocks.add(Material.WHITE_TULIP);
            ignoredBlocks.add(Material.CORNFLOWER);
            ignoredBlocks.add(Material.LILY_OF_THE_VALLEY);
            ignoredBlocks.add(Material.TORCHFLOWER);
            ignoredBlocks.add(Material.WITHER_ROSE);
            ignoredBlocks.add(Material.PINK_PETALS);
            ignoredBlocks.add(Material.SUNFLOWER);
            ignoredBlocks.add(Material.PITCHER_PLANT);
            ignoredBlocks.add(Material.ACACIA_SAPLING);
            ignoredBlocks.add(Material.BAMBOO_SAPLING);
            ignoredBlocks.add(Material.OAK_SAPLING);
            ignoredBlocks.add(Material.CHERRY_SAPLING);
            ignoredBlocks.add(Material.JUNGLE_SAPLING);
            ignoredBlocks.add(Material.SPRUCE_SAPLING);
            ignoredBlocks.add(Material.DARK_OAK_SAPLING);
            ignoredBlocks.add(Material.BIRCH_SAPLING);
            ignoredBlocks.add(Material.MANGROVE_PROPAGULE);
            ignoredBlocks.add(Material.FERN);
            ignoredBlocks.add(Material.LARGE_FERN);
            ignoredBlocks.add(Material.CAVE_VINES);
            ignoredBlocks.add(Material.TWISTING_VINES);
            ignoredBlocks.add(Material.WEEPING_VINES);
            ignoredBlocks.add(Material.DEAD_BUSH);
            ignoredBlocks.add(Material.SWEET_BERRY_BUSH);
            ignoredBlocks.add(Material.GLOW_BERRIES);
            ignoredBlocks.add(Material.BIG_DRIPLEAF);
            ignoredBlocks.add(Material.SMALL_DRIPLEAF);
            ignoredBlocks.add(Material.BIG_DRIPLEAF_STEM);
            ignoredBlocks.add(Material.BLACK_CARPET);
/*            ignoredBlocks.add(Material.YELLOW_CARPET);
            ignoredBlocks.add(Material.PURPLE_CARPET);
            ignoredBlocks.add(Material.LIGHT_GRAY_CARPET);
            ignoredBlocks.add(Material.RED_CARPET);
            ignoredBlocks.add(Material.WHITE_CARPET);
            ignoredBlocks.add(Material.ORANGE_CARPET);
            ignoredBlocks.add(Material.PINK_CARPET);
            ignoredBlocks.add(Material.MAGENTA_CARPET);
            ignoredBlocks.add(Material.LIGHT_BLUE_CARPET);
            ignoredBlocks.add(Material.MOSS_CARPET);
            ignoredBlocks.add(Material.LIME_CARPET);
            ignoredBlocks.add(Material.GREEN_CARPET);
            ignoredBlocks.add(Material.BROWN_CARPET);
            ignoredBlocks.add(Material.GRAY_CARPET);
            ignoredBlocks.add(Material.BLUE_CARPET);
            ignoredBlocks.add(Material.CYAN_CARPET);*/
            ignoredBlocks.add(Material.TORCH);
            ignoredBlocks.add(Material.WALL_TORCH);
            ignoredBlocks.add(Material.REDSTONE_TORCH);
            ignoredBlocks.add(Material.REDSTONE_WALL_TORCH);
            ignoredBlocks.add(Material.SOUL_TORCH);
            ignoredBlocks.add(Material.SOUL_WALL_TORCH);

            // flip on hit barrier
            if(loc.getBlock().getType().equals(Material.BARRIER)) {
                flipped = true;
            } else if (!hideParticles && !loc.getBlock().getType().equals(Material.AIR) && !ignoredBlocks.contains(loc.getBlock().getType())) {
                //PLAY FIZZLE SOUND
                if(loc.getBlock().getBoundingBox().contains(loc.getX(),loc.getY(),loc.getZ())) {

                    loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 0.75f, 1.0f);
                    loc.getWorld().spawnParticle(Particle.SMOKE, loc, 50, 0, 0, 0, 0);
                    this.cancel();
                    break;
                }
                // if hits barrier block or air
            } else {

                LivingEntity e = null;
                boolean foundEntity = false;

                double distance = 10;
                // IF SPELL HITS ENTITY
                for (Entity en : loc.getWorld().getNearbyEntities(loc, 1.75, 2.0, 1.75)) { //getChunk().getEntities()) {

                    BoundingBox boundingBox = en.getBoundingBox();
                    boundingBox.expand(0.1,0.1,0.1,0.1,0.1,0.1);
                    if((!flipped && !en.getUniqueId().equals(casterId)) && boundingBox.contains(loc.getX(),loc.getY(),loc.getZ())) {
                        if (en instanceof LivingEntity && !(en instanceof ItemFrame)) {
                            foundEntity = true;
                            // entity detected
                            if (distance>en.getLocation().distance(loc)) {
                                distance = en.getLocation().distance(loc);
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
