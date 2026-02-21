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

package com.mineshaft.mineshaftapi.manager.location;

import com.mineshaft.mineshaftapi.util.save_data.SaveLocation;
import org.bukkit.Location;

import java.util.Map;

public class WarpManager {

    public static void addWarp(String name, Location location) {
        WarpJsonManager warpJsonManager = new WarpJsonManager();
        warpJsonManager.addWarp(name,location);
    }

    public static void removeWarp(String name) {
        WarpJsonManager warpJsonManager = new WarpJsonManager();
        warpJsonManager.removeWarp(name);
    }

    public static Map<String, SaveLocation> getWarps() {
        return new WarpJsonManager().getWarps();
    }

    public static Location getWarp(String name) {
        if(getWarps().containsKey(name)) {
            return getWarps().get(name).getLocation();
        }
        return null;
    }

}
