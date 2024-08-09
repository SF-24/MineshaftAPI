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
import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.EventManager;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TargetEventMechanic implements ITargetedEntitySkill {

    protected final String event;
    protected final int damage;

    public TargetEventMechanic(MythicLineConfig config) {
        this.event = config.getString(new String[] {"event", "e"}, "example-event");
        this.damage = config.getInteger(new String[] {"damage", "d"}, 5);
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity targetEntity) {
        data.getParameters();

        MineshaftApi.getAnyPlayer().sendMessage("executing event!");

        UUID uuid = data.getCaster().getEntity().getUniqueId();

        AbstractLocation targetLoc = targetEntity.getLocation();

        final Location loc = getLocation(data, targetLoc);

        String eventName = data.getParameters().get(event);

        EventManager eventManager = MineshaftApi.getInstance().getEventManagerInstance();

        Event event = eventManager.getEvent("blaster-shot");
/*
        if(event instanceof BeamEvent) {
            MineshaftApi.getAnyPlayer().sendMessage("Beam event!");

            if (((BeamEvent) event).getOnHitEntity().contains(LocalEvent.DAMAGE)) {
                ((BeamEvent) event).setOnHitEntity(LocalEvent.DAMAGE,damage);
            }
            if (((BeamEvent) event).getOnHitPlayer().contains(LocalEvent.DAMAGE)) {
                ((BeamEvent) event).setOnHitPlayer(LocalEvent.DAMAGE,damage);
            }
        }*/

        eventManager.runEvent(event, loc, uuid);

        return SkillResult.SUCCESS;
    }

    private static @NotNull Location getLocation(SkillMetadata data, AbstractLocation targetLoc) {
        Location loc = new Location(Bukkit.getWorld(data.getCaster().getLocation().getWorld().getUniqueId()), data.getCaster().getLocation().getX(), data.getCaster().getLocation().getY(), data.getCaster().getLocation().getZ());

        AbstractLocation casterLoc = data.getCaster().getLocation();

        double x = targetLoc.getX()-casterLoc.getX();
        double y = targetLoc.getY()-casterLoc.getY();
        double z = targetLoc.getZ()-casterLoc.getZ();

        loc.setDirection(new Vector(x,y,z));

        return loc;
    }

}
