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
import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import com.mineshaft.mineshaftapi.util.maths.PlanarVectorBounds;
import com.mineshaft.mineshaftapi.util.maths.Vector2D;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@Setter @Getter
public class VectorPlayerEvent extends Event {

    boolean legacy;
    private PlanarVectorBounds vectorBounds;

    public VectorPlayerEvent(EventType type) {
        super(type);
    }

    public void dashPlayerEvent(Player player) {
        Vector dir = player.getLocation().getDirection().normalize();
        vectorBounds.trimVector(dir);

        dir = dir.add(dir);
        dir = dir.add(player.getLocation().getDirection().normalize());
        dir = dir.add(player.getLocation().getDirection().normalize());

        player.setVelocity(dir);
    }

    @Deprecated
    public void legacyDashPlayerEvent(Player player) {
        vectorBounds=new PlanarVectorBounds(0,0.25);
        dashPlayerEvent(player);
    }

    @Deprecated
    public void legacyLeapPlayerEvent(Player player) {
        setVectorBounds(new PlanarVectorBounds(0.5,0.9));
        leapPlayerEvent(player);
    }

    public void leapPlayerEvent(Player player, PlanarVectorBounds bounds) {
        setVectorBounds(bounds);
        leapPlayerEvent(player);
    }

    public void leapPlayerEvent(Player player) {
        Vector dir = player.getLocation().getDirection().normalize();

        dir = dir.add(dir);
        vectorBounds.trimVector(dir);

        player.setSneaking(false);
        player.setVelocity(dir);

        Bukkit.getScheduler().runTaskLater(MineshaftApi.getInstance(),() -> {
            player.setSneaking(true);
            player.setSneaking(false);
        },5);
    }
}
