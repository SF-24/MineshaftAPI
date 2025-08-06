/*
 * Copyright (c) 2025. Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public static License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public static License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public static License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.mineshaft.mineshaftapi.dependency.lead_wires;

import com.mineshaft.mineshaftapi.util.LocationUtil;
import me.saharnooby.plugins.leadwires.LeadWires;
import me.saharnooby.plugins.leadwires.api.LeadWiresAPI;
import me.saharnooby.plugins.leadwires.wire.Wire;
import org.bukkit.Bukkit;
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

    public static boolean wireHasEndAtLocation(UUID uuid, Location loc) {
        return getWire(uuid).getWorld().equals(loc.getWorld().getName()) && ( getRoundedEndA(uuid).equals(LocationUtil.getRoundedLocation(loc)) || getRoundedEndB(uuid).equals(LocationUtil.getRoundedLocation(loc)));
    }

    public static Vector getOtherEnd(UUID uuid, Location loc) {
        if(wireHasEndAtLocation(uuid, loc)) {
            if(getRoundedEndA(uuid).equals(LocationUtil.getRoundedLocation(loc))) {
                return getRoundedEndB(uuid);
            } else {
                return getRoundedEndA(uuid);
            }
        }
        return null;
    }

    public static Location getOtherEndAsLocation(UUID uuid, Location loc) {
        if(getOtherEnd(uuid, loc)==null || Bukkit.getWorld(getWire(uuid).getWorld())==null) return null;
        return getOtherEnd(uuid, loc).toLocation(Bukkit.getWorld(getWire(uuid).getWorld()));
    }

    public static ArrayList<Vector> findCableEnds(UUID uuid, Location loc) {
        ArrayList<Vector> cableEnds = new ArrayList<>();
        // TODO: Find cable ends
        return cableEnds;
    }

    public static ArrayList<UUID> findConnectorsAtPoint(UUID uuid, Location loc) {
        ArrayList<UUID> connectors = new ArrayList<>();
        for(UUID element : getApi().getWires().keySet()) {
            if(!element.equals(uuid) && wireHasEndAtLocation(element, loc)) {
                connectors.add(element);
            }
        }
        return connectors;
    }

    public static ArrayList<UUID> findConnectorsAtOtherEnd(UUID uuid, Location loc) {
        return findConnectorsAtPoint(uuid, getOtherEndAsLocation(uuid, loc));
    }

    public static ArrayList<UUID> getWires(Location loc) {
        ArrayList<UUID> wires = new ArrayList<>();
        for(UUID element : getApi().getWires().keySet()) {
            if(wireHasEndAtLocation(element, loc)) {
                wires.add(element);
            }
        }
        return wires;
    }

    public static Vector getRoundedEndA(UUID uuid) {
        return new Vector(getWire(uuid).getA().getBlockX(),getWire(uuid).getA().getBlockY(),getWire(uuid).getA().getBlockZ());
    }

    public static Vector getRoundedEndB(UUID uuid) {
        return new Vector(getWire(uuid).getB().getBlockX(),getWire(uuid).getB().getBlockY(),getWire(uuid).getB().getBlockZ());
    }

}
