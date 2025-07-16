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

package com.mineshaft.mineshaftapi.manager.event;

import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import com.mineshaft.mineshaftapi.manager.event.fields.TriggerType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Event {

    // Dummy parameter used specifically for certain events and with MineshaftRpg
    @Getter @Setter
    int castStrength = 0;

    protected String parent = "null";
    @Getter @Setter
    protected String name = "Unnamed Event";
    @Getter
    protected Vector offset = new Vector(0.0,0.0,0.0);
    @Getter @Setter
    protected EventType eventType;
    @Getter @Setter
    protected TriggerType target = TriggerType.ANY;
    @Getter @Setter
    protected String sound = null;

    public HashMap<String, Object> customParameters = new HashMap<>();

    public Event(EventType type) {this.eventType=type;}

    public void setOffset(Vector offset) {this.offset=offset;}
    public void setOffset(double x, double y, double z) {this.offset=new Vector(x,y,z);}

    public void addParameter(String key, Object value) {customParameters.put(key,value);}

    public HashMap<String, Object> getParameters() {return customParameters;}

    public Object getParameter(String key) {return customParameters.get(key);}

    public void playSound(Location loc) {
        if(sound!=null) {
            loc.getWorld().playSound(loc,sound, 1,1);
        }
    }

    public void clone(Event loadedEvent) {
        loadedEvent.setName(this.getName());
        loadedEvent.setOffset(this.getOffset());
        loadedEvent.setTarget(this.getTarget());
        loadedEvent.customParameters = this.getParameters();
        loadedEvent.setSound(sound);
        loadedEvent.setEventType(eventType);
    }

}
