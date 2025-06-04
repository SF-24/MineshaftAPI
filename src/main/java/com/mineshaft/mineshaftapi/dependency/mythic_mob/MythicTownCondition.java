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

package com.mineshaft.mineshaftapi.dependency.mythic_mob;

import com.mineshaft.mineshaftapi.MineshaftApi;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.ILocationCondition;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MythicTownCondition implements ILocationCondition {

    public MythicTownCondition(MythicLineConfig config) {
        // Ignore. Can be used for config if needed.
    }

    @Override
    public boolean check(AbstractLocation abstractLocation) {
        return isInTown(abstractLocation);
    }

    // Method to check if a location is in a town with a MythicMobs location function
    public boolean isInTown(AbstractLocation loc) {
        return MineshaftApi.getInstance().getRegionManager().isInTown(new Location(Bukkit.getWorld(loc.getWorld().getName()),loc.getX(),loc.getY(),loc.getZ()));
    }

}
