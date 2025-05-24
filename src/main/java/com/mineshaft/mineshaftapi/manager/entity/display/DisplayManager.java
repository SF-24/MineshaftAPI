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

package com.mineshaft.mineshaftapi.manager.entity.display;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;

public class DisplayManager {
    public static Display generateDisplay(DisplayType displayType, Object stack, Location loc) {
        Display projectile = null;
        if(displayType.equals(DisplayType.ITEM)) {
            projectile = loc.getWorld().spawn(loc, ItemDisplay.class);
            ItemDisplay itemProjectile = (ItemDisplay) projectile;
            itemProjectile.setItemStack((ItemStack) stack);
        } else if(displayType.equals(DisplayType.BLOCK)) {
            projectile = loc.getWorld().spawn(loc, BlockDisplay.class);
            BlockDisplay blockProjectile = (BlockDisplay) projectile;
            blockProjectile.setBlock((BlockData) stack);
        } else if(displayType.equals(DisplayType.TEXT)) {
            projectile = loc.getWorld().spawn(loc, TextDisplay.class);
        }
        return projectile;
    }
}
