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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public abstract class BlockClassTickable extends BlockClass{
    public BlockClassTickable(Location location, BlockFace facing, Material material, int customModelData) {
        super(location, facing, material, customModelData);
    }

    public BlockClassTickable(Location location, Material material, int customModelData) {
        super(location, material, customModelData);
    }

    public abstract void tick();

}
