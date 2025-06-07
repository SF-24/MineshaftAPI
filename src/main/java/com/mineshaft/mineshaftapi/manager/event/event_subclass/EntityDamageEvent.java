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
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class EntityDamageEvent extends Event {

    public double damage = 5;
    public DamageSource source = DamageSource.builder(DamageType.GENERIC).build();
    public Entity damager = null;

    public void setDamageType(DamageType type) {
        source = DamageSource.builder(type).build();
    }

    public void damageEntity(Entity entity) {
        if(entity instanceof LivingEntity) {
            if(damager!=null) {
                ((LivingEntity) entity).damage(damage, entity);
            } else if(source!=null) {
                ((LivingEntity) entity).damage(damage, source);
            } else {
                ((LivingEntity) entity).damage(damage);
            }
        }
    }
}
