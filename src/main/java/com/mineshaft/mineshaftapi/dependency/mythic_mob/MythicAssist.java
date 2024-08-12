/*
 *     This program is a Minecraft plugin developed in Java for the Spigot API.
 *     It adds multiple RPG features intended for Multiplayer gameplay.
 *
 *     Copyright (C) 2024  Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.mineshaft.mineshaftapi.dependency.mythic_mob;

import com.mineshaft.mineshaftapi.MineshaftApi;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.skills.SkillMetadata;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Random;

public class MythicAssist {

    public static Location getLocation(SkillMetadata data, AbstractLocation targetLoc, int offset) {
        Location loc = new Location(Bukkit.getWorld(data.getCaster().getLocation().getWorld().getUniqueId()), data.getCaster().getLocation().getX(), data.getCaster().getLocation().getY(), data.getCaster().getLocation().getZ());

        AbstractLocation casterLoc = data.getCaster().getLocation();

        double x = targetLoc.getX()-casterLoc.getX();
        double y = targetLoc.getY()-casterLoc.getY();
        double z = targetLoc.getZ()-casterLoc.getZ();

        // Calculate the hypotenuse of the x,z,L triangle
        double L = Math.sqrt(Math.pow(x,2) + Math.pow(z,2));

        Random random = new Random();

        // Calculate the pitch (p)
        double pitch = Math.atan(y/L);

        // Calculate the yaw (γ)
        double yaw = Math.atan(z/x);

        loc.setPitch((float) pitch);
        loc.setYaw((float) yaw);

        double pitchError = 0;
        double yawError = 0;

        if(offset>0) {
            double error = ((double)offset)/100;

            pitchError = random.nextDouble(error);
            yawError = random.nextDouble(error);
            if(random.nextBoolean()) {
                pitchError=-pitchError;
            }
            if(random.nextBoolean()) {
                yawError=-yawError;
            }
        }

        double updatedPitch = pitch + pitchError;
        double updatedYaw = yaw + yawError;

        double x2 = Math.cos(updatedYaw)*L;
        double z2 = Math.sin(updatedYaw)*L;
        double y2 = Math.tan(updatedPitch)*x2;

        if((z2<0 && z>0)||(z2>0 && z<0)) z2=z2*-1;
        if((x2<0 && x>0)||(x2>0 && x<0)) x2=x2*-1;
        if((y2<0 && y>0)||(y2>0 && y<0)) y2=y2*-1;

        //loc.setDirection(new Vector(x,y,z));
        Vector vector2 = new Vector(x2,y2,z2);
        if(offset>0) {
            loc.setDirection(new Vector(x2, y2, z2));
        } else {
            loc.setDirection(new Vector(x,y,z));
        }
        return loc;
    }

}
