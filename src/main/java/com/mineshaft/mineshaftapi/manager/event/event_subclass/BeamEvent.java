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

import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Particle;

public class BeamEvent extends TargeterEvent {

    boolean hasProjectile=false;
    @Getter
    @Setter
    int size = 2;
    @Getter
    @Setter
    org.bukkit.Color colour = org.bukkit.Color.RED;
    @Getter
    @Setter
    int power = 10;
    @Getter
    @Setter
    int speed = 20;
    //  public void setSound(String sound) {this.sound = sound;}
    @Getter
    @Setter
    int flyDistance = 40;
    @Getter
    @Setter
    int particleCount = 0;
    @Getter
    @Setter
    org.bukkit.Particle particleType= org.bukkit.Particle.DRAGON_BREATH;

    public BeamEvent(EventType type) {
        super(type);
    }

//    private String sound;

    public void setProjectile(boolean projectile) { this.hasProjectile=projectile;}

    public boolean hasProjectile() {return this.hasProjectile;}

    //    public String getSound() {return sound;}

}
