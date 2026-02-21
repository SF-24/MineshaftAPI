/*
 * Copyright (c) 2026. Sebastian Frynas
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

package com.mineshaft.mineshaftapi.util.save_data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter @Setter
public class SaveLocation {

    private final String worldName;
    double x;
    double y;
    double z;
    float pitch;
    float yaw;

    SaveLocation(double x, double y, double z, float pitch, float yaw, String worldName) {
        this.x=x;
        this.y=y;
        this.z=z;
        this.pitch=pitch;
        this.yaw=yaw;
        this.worldName=worldName;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(worldName),x,y,z,pitch,yaw);
    }

    public static SaveLocation fromLocation(Location location) {
        return new SaveLocation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw(), location.getWorld().getName());
    }
}
