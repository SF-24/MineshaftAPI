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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Event {

    protected String parent = "null";
    protected String name = "Unnamed Event";
    protected Vector offset = new Vector(0.0,0.0,0.0);
    protected EventType eventType = EventType.BEAM;
    protected TriggerType target = TriggerType.ANY;
    protected String sound = null;

    public HashMap<String, Object> customParameters = new HashMap<>();

    public void setSound(String sound) {this.sound=sound;}
    public void setTarget(TriggerType target) {this.target = target;}
    public void setOffset(Vector offset) {this.offset=offset;}
    public void setOffset(double x, double y, double z) {this.offset=new Vector(x,y,z);}
    public void setName(String name) {this.name=name;}
    public void setEventType(EventType eventType) {this.eventType=eventType;}
    public void addParameter(String key, Object value) {customParameters.put(key,value);}

    public HashMap<String, Object> getParameters() {return customParameters;}
    public Vector getOffset() {return offset;}
    public String getName() {return name;}
    public Object getParameter(String key) {return customParameters.get(key);}
    public EventType getEventType() {return eventType;}
    public TriggerType getTarget() {return target;}
    public String getSound() {return sound;}

    public void playSound(Location loc) {
        if(sound!=null) {
            loc.getWorld().playSound(loc,sound, 1,1);
        }
    }

}
