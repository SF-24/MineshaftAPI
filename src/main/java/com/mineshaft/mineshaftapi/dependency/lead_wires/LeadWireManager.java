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

package com.mineshaft.mineshaftapi.dependency.lead_wires;

import com.mineshaft.mineshaftapi.util.LocationUtil;
import me.saharnooby.plugins.leadwires.LeadWires;
import me.saharnooby.plugins.leadwires.api.LeadWiresAPI;
import me.saharnooby.plugins.leadwires.wire.Wire;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class LeadWireManager {

    public static LeadWiresAPI getApi() {
        return LeadWires.getApi();
    }

    public static Wire getWire(UUID uuid) {
        return getApi().getWires().get(uuid);
    }

    public boolean wireHasEndAtLocation(UUID uuid, Location loc) {
        return getWire(uuid).getWorld().equals(loc.getWorld().getName()) && ( getRoundedEndA(uuid).equals(LocationUtil.getRoundedLocation(loc)) || getRoundedEndB(uuid).equals(LocationUtil.getRoundedLocation(loc)));
    }

    public Vector getOtherEnd(UUID uuid, Location loc) {
        if(wireHasEndAtLocation(uuid, loc)) {
            if(getRoundedEndA(uuid).equals(LocationUtil.getRoundedLocation(loc))) {
                return getRoundedEndB(uuid);
            } else {
                return getRoundedEndA(uuid);
            }
        }
        return null;
    }

    public ArrayList<Vector> findCableEnds(UUID uuid, Location loc) {
        ArrayList<Vector> cableEnds = new ArrayList<>();
        // TODO: Find cable ends
        return cableEnds;
    }

    public Vector getRoundedEndA(UUID uuid) {
        return new Vector(getWire(uuid).getA().getBlockX(),getWire(uuid).getA().getBlockY(),getWire(uuid).getA().getBlockZ());
    }

    public Vector getRoundedEndB(UUID uuid) {
        return new Vector(getWire(uuid).getB().getBlockX(),getWire(uuid).getB().getBlockY(),getWire(uuid).getB().getBlockZ());
    }

}
