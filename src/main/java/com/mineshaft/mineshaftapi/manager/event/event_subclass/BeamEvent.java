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

package com.mineshaft.mineshaftapi.manager.event.event_subclass;

import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.fields.LocalEvent;
import org.bukkit.Color;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BeamEvent extends Event {

    int size = 2;
    org.bukkit.Color colour = org.bukkit.Color.RED;
    int power = 10;
    int speed = 20;
    int flyDistance = 40;
    int particleCount = 0;
    org.bukkit.Particle particleType= org.bukkit.Particle.DRAGON_BREATH;

    HashMap<LocalEvent, Object> onHitEntity = new HashMap<>();
    HashMap<LocalEvent, Object> onHitBlock = new HashMap<>();
    HashMap<LocalEvent, Object> onHitPlayer = new HashMap<>();

    public void setPower(int damage) {this.power = damage;}
    public void setSpeed(int speed) {this.speed = speed;}
    public void setFlyDistance(int flyDistance) {this.flyDistance = flyDistance;}
    public void setParticleCount(int particle) {this.particleCount = particle;}
    public void setParticleType(Particle particle) {this.particleType = particle;}
    public void setSize(int size) {this.size = size;}
    public void setColour(org.bukkit.Color colour) {this.colour = colour;}

    public int getParticleCount() {return particleCount;}
    public int getPower() {return power;}
    public int getSpeed() {return speed;}
    public int getFlyDistance() {return flyDistance;}
    public org.bukkit.Particle getParticleType() {return particleType;}
    public Color getColour() {return colour;}
    public int getSize() {return size;}

    public void setOnHitEntity(LocalEvent event, Object value) {
        onHitEntity.clear();
        onHitEntity.put(event, value);
    }

    public void setOnHitPlayer(LocalEvent event, Object value) {
        onHitPlayer.clear();
        onHitPlayer.put(event, value);
    }

    public void setOnHitBlock(LocalEvent event, Object value) {
        onHitBlock.clear();
        onHitBlock.put(event, value);
    }

    public List<LocalEvent> getOnHitBlock() {
        List<LocalEvent> events = new ArrayList<>();
        for(LocalEvent event : onHitBlock.keySet()) {
            events.add(event);
        }
        return events;
    }

    public List<LocalEvent> getOnHitEntity() {
        List<LocalEvent> events = new ArrayList<>();
        for(LocalEvent event : onHitEntity.keySet()) {
            events.add(event);
        }
        return events;
    }

    public List<LocalEvent> getOnHitPlayer() {
        List<LocalEvent> events = new ArrayList<>();
        for(LocalEvent event : onHitPlayer.keySet()) {
            events.add(event);
        }
        return events;
    }

    public Object getOnHitBlockObject(LocalEvent event) {
        return onHitBlock.get(event);
    }

    public Object getOnHitPlayerObject(LocalEvent event) {
        return onHitPlayer.get(event);
    }

    public Object getOnHitEntityObject(LocalEvent event) {
        return onHitEntity.get(event);
    }
}
