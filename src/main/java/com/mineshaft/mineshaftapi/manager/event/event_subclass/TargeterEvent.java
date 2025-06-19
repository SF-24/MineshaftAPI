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

package com.mineshaft.mineshaftapi.manager.event.event_subclass;

import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class TargeterEvent extends Event {
    ArrayList<Event> onHitEntity = new ArrayList<>();
    ArrayList<Event> onHitBlock = new ArrayList<>();
    ArrayList<Event> onHitPlayer = new ArrayList<>();

    public TargeterEvent(EventType type) {
        super(type);
    }


    public void removeOnHitEntity(Event event) {
        onHitEntity.remove(event);
    }

    public void removeOnHitPlayer(Event event) {
        onHitPlayer.remove(event);
    }

    public void removeOnHitBlock(Event event) {
        onHitBlock.remove(event);
    }

    public void setOnHitEntity(Event event) {
        onHitEntity.add(event);
    }

    public void setOnHitPlayer(Event event) {
        onHitPlayer.add(event);
    }

    public void setOnHitBlock(Event event) {
        onHitBlock.add(event);
    }

    public List<Event> getOnHitBlock() {
        return new ArrayList<>(onHitBlock);
    }

    public List<Event> getOnHitEntity() {
        return new ArrayList<>(onHitEntity);
    }

    public List<Event> getOnHitPlayer() {
        return new ArrayList<>(onHitPlayer);
    }


    public ArrayList<Material> getIgnoredBlocks() {
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

}
