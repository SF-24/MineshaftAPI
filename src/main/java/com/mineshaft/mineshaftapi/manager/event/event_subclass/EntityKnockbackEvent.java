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

import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

@Getter @Setter
public class EntityKnockbackEvent extends Event {

    double knockbackMultiplier = 1;
    boolean limitVerticalKnockback = true;
    public Vector casterVector;

    public EntityKnockbackEvent(EventType type) {
        super(type);
    }

    public void knockbackEntity(Entity entity) {
        if(casterVector!=null) {
            Vector dir=casterVector.normalize();
            dir.multiply(dir);
            dir.multiply(knockbackMultiplier);
//            dir.multiply(-1);
            if(limitVerticalKnockback) {
                dir = dir.setY(0.25);
            }
            entity.setVelocity(dir);
        }
    }
}
