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
import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.EventManager;
import com.mineshaft.mineshaftapi.manager.event.event_subclass.BeamEvent;
import com.mineshaft.mineshaftapi.manager.event.fields.LocalEvent;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import org.bukkit.Location;

import java.util.UUID;

public class EventMechanic implements ITargetedLocationSkill {

    protected final String event;
    protected final int damage;

    public EventMechanic(MythicLineConfig config) {
        this.event = config.getString(new String[] {"event", "e"}, "example-event");
        this.damage = config.getInteger(new String[] {"damage", "d"}, 5);
    }

    @Override
    public SkillResult castAtLocation(SkillMetadata data, AbstractLocation targetLoc) {
        data.getParameters();

        UUID uuid = data.getCaster().getEntity().getUniqueId();

        //Vector vector = new Vector();

        final Location loc = MythicAssist.getLocation(data, targetLoc, 0);
//        loc.setDirection(new Vector(x,y,z));

        String eventName = data.getParameters().get(event);

        EventManager eventManager = MineshaftApi.getInstance().getEventManagerInstance();

        Event event = eventManager.getEvent(eventName);

        if(event instanceof BeamEvent) {
            if (((BeamEvent) event).getOnHitEntity().contains(LocalEvent.DAMAGE)) {
                ((BeamEvent) event).setOnHitEntity(LocalEvent.DAMAGE,damage);
            }
            if (((BeamEvent) event).getOnHitPlayer().contains(LocalEvent.DAMAGE)) {
                ((BeamEvent) event).setOnHitPlayer(LocalEvent.DAMAGE,damage);
            }
        }

        eventManager.runEvent(event, loc, uuid);

        return SkillResult.SUCCESS;
    }
}
