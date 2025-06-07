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

import org.bukkit.Color;
import org.bukkit.Particle;

public class BeamEvent extends TargeterEvent {

    boolean hasProjectile=false;
    int size = 2;
    org.bukkit.Color colour = org.bukkit.Color.RED;
    int power = 10;
    int speed = 20;
    int flyDistance = 40;
    int particleCount = 0;
    org.bukkit.Particle particleType= org.bukkit.Particle.DRAGON_BREATH;

//    private String sound;

    public void setSpeed(int speed) {this.speed = speed;}
    public void setPower(int damage) {this.power = damage;}
  //  public void setSound(String sound) {this.sound = sound;}
    public void setFlyDistance(int flyDistance) {this.flyDistance = flyDistance;}
    public void setParticleCount(int particle) {this.particleCount = particle;}
    public void setParticleType(Particle particle) {this.particleType = particle;}
    public void setSize(int size) {this.size = size;}
    public void setColour(org.bukkit.Color colour) {this.colour = colour;}
    public void setProjectile(boolean projectile) { this.hasProjectile=projectile;}

    public boolean hasProjectile() {return this.hasProjectile;}
    public int getParticleCount() {return particleCount;}
    public int getPower() {return power;}
    public int getSpeed() {return speed;}
    public int getFlyDistance() {return flyDistance;}
    public org.bukkit.Particle getParticleType() {return particleType;}
    public Color getColour() {return colour;}
    public int getSize() {return size;}
//    public String getSound() {return sound;}

}
