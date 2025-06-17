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

package com.mineshaft.mineshaftapi.manager.player;

import com.mineshaft.mineshaftapi.MineshaftApi;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayerAttackManager {

    public static ArrayList<Material> getIgnoredBlocks() {
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
        ignoredBlocks.add(Material.TORCH);
        ignoredBlocks.add(Material.WALL_TORCH);
        ignoredBlocks.add(Material.REDSTONE_TORCH);
        ignoredBlocks.add(Material.REDSTONE_WALL_TORCH);
        ignoredBlocks.add(Material.SOUL_TORCH);
        ignoredBlocks.add(Material.SOUL_WALL_TORCH);
        return ignoredBlocks;
    }

    public static void makeAttack(Player player) {
        player.swingMainHand();

        Bukkit.getScheduler().runTask(MineshaftApi.getInstance(), ()->{
            Entity target = getTargetEntity(player);
            if(target!=null) {
                player.attack(target);
            }
        });
    }

    private static Entity getTargetEntity(LivingEntity entity) {
        // Ray-cast
        @NotNull Location loc = entity.getLocation().add(0,1.0,0);
        if(entity.isSneaking()) {
            loc.add(0,-0.5,0);
        }
        for(double i = 0; i<=getAttackReach(entity)+0.5; i+=0.25) {
            // Iterate-able location
            loc.add(new Vector(entity.getLocation().getDirection().normalize().getX()*0.25,entity.getLocation().getDirection().normalize().getY()*0.25,entity.getLocation().getDirection().normalize().getZ()*0.25));

            if(!entity.getWorld().getBlockAt(loc).getType().equals(Material.AIR) || !getIgnoredBlocks().contains(entity.getWorld().getBlockAt(loc).getType())) {
                if(loc.getBlock().getBoundingBox().contains(loc.getX(),loc.getY(),loc.getZ())) {
                    return null;
                }
            }

            // Check for entities
            for(Entity potentialTarget : entity.getWorld().getNearbyEntities(loc,2.5,2.5,2.5)) {
                if(!entity.getUniqueId().equals(potentialTarget.getUniqueId())) {
                    BoundingBox boundingBox = potentialTarget.getBoundingBox();
                    boundingBox.expand(0.1, 0.1, 0.1, 0.1, 0.1, 0.1);

                    if (boundingBox.contains(loc.toVector())) {
                        return potentialTarget;
                    }
                }
            }
        }
        return null;
    }

    private static double getAttackReach(LivingEntity entity) {
//        if(entity.getAttribute(Attribute.ENTITY_INTERACTION_RANGE)!=null) return entity.getAttribute(Attribute.ENTITY_INTERACTION_RANGE).getValue();
        return 3.0;
    }
}
