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

package com.mineshaft.mineshaftapi.manager.block.json;

import com.mineshaft.mineshaftapi.util.ItemUtil;
import com.mineshaft.mineshaftapi.util.display.DisplayUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter @Setter
public abstract class BlockClass {

    ItemStack dropItem;
    Material material = Material.BARRIER;
    int customModelData = 0;
    UUID displayId = null;
    boolean useFacing = false;
    BlockFace facing;
    Location location;

    public abstract void click(Player player);

    public BlockClass(Location location, BlockFace facing, Material material, int customModelData, ItemStack item) {
        init(location, facing, material, customModelData, item);
    }

    public BlockClass(Location location, Material material, int customModelData, ItemStack item) {
        init(location, null, material, customModelData, item);
    }

    void init(Location location, BlockFace blockFace, Material material, int customModelData, ItemStack item) {
        this.location=location;
        if(blockFace != null) {
            facing = blockFace;
            useFacing = true;
        } else {
            facing = BlockFace.UP;
        }
        this.material=material;
        this.customModelData=customModelData;
        this.dropItem=item;
        place();
    }

    public void place() {
        Location selfLocation = location.clone();

        if(useFacing) {
            switch (facing) {
                case NORTH -> {
                    selfLocation.setYaw(0);
                    selfLocation.setPitch(0);
                }
                case EAST -> {
                    selfLocation.setYaw(90);
                    selfLocation.setPitch(0);
                }
                case SOUTH -> {
                    selfLocation.setYaw(180);
                    selfLocation.setPitch(0);
                }
                case WEST -> {
                    selfLocation.setYaw(270);
                    selfLocation.setPitch(0);
                }
                case UP -> {
                    selfLocation.setPitch(90);
                    selfLocation.setYaw(0);
                }
                case DOWN -> {
                    selfLocation.setPitch(-90);
                    selfLocation.setYaw(0);
                }
            }
        }
        location.getBlock().setType(Material.BARRIER);
        displayId = DisplayUtil.spawnItemDisplay(location, ItemUtil.getDefaultItem(material, customModelData));
    }

    public void remove() {
        location.getBlock().setType(Material.AIR);
        if(displayId == null) return;

        for(Entity entity : location.getWorld().getNearbyEntities(location,3.0,3.0,3.0)) {
            if(entity.getUniqueId().equals(displayId) && entity instanceof ItemDisplay) {
                entity.remove();
                break;
            }
        }
        location.getWorld().dropItem(location,dropItem);
    }
}
