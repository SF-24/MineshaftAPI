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

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.event.Event;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VectorPlayerEvent extends Event {

    private String sound = null;

    public void setSound(String sound) {this.sound = sound;}
    public String getSound() {return sound;}

    public void playSound(Player player) {
        if(sound!=null) {
            player.getWorld().playSound(player.getLocation(),sound, 1,1);
        }
    }

    public void dashPlayerEvent(Player player) {
        Vector dir = player.getLocation().getDirection().normalize();

        dir = dir.add(dir);
        dir = dir.add(player.getLocation().getDirection().normalize());
        dir = dir.add(player.getLocation().getDirection().normalize());

        if(dir.getY()>0.25) {
            dir.setY(0.25);
        }

        player.setVelocity(dir);
    }
    public void leapPlayerEvent(Player player) {
        Vector dir = player.getLocation().getDirection().normalize();

        dir = dir.add(dir);

        if(dir.getY()>0.9) {
            dir.setY(0.9);
        }

        if(dir.getY()<0.5) {
            dir.setY(0.5);
        }

        player.setSneaking(false);
        player.setVelocity(dir);

        Bukkit.getScheduler().runTaskLater(MineshaftApi.getInstance(),() -> {
            player.setSneaking(true);
            player.setSneaking(false);
        },5);
    }
}
