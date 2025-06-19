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
import com.mineshaft.mineshaftapi.manager.event.PendingAbilities;
import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import org.bukkit.entity.Entity;

public class PrepareStrongAttackEntityEvent extends Event {

    public double damageMultiplier = 1;
    public double knockbackPower = 1;
    public boolean particles = false;
    public String attackSound = null;

    public PrepareStrongAttackEntityEvent(EventType type) {
        super(type);
    }

    public void prepareStrongAttack(Entity entity) {
        PendingAbilities pendingAbilities = MineshaftApi.getInstance().getPendingAbilities(entity.getUniqueId());
        pendingAbilities.addStrongAttackAbility(damageMultiplier,knockbackPower,attackSound,particles);
        MineshaftApi.getInstance().setPendingAbilities(entity.getUniqueId(), pendingAbilities);

        if(attackSound!=null) {
            entity.getWorld().playSound(entity.getLocation(),attackSound, 1.0f, 1.0f);
        }
    }
}
