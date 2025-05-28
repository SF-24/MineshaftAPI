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

import java.util.HashMap;

public class PendingAbilities {

    HashMap<PendingAbilityType, PendingAbility> pendingAbilities = new HashMap<>();

    public boolean isEmpty() {
        return pendingAbilities.isEmpty();
    }

    public void addAbility(PendingAbilityType type, PendingAbility ability) {
        pendingAbilities.put(type,ability);
    }

    public void removeAbility(PendingAbilityType type) {
        pendingAbilities.remove(type);
    }

    public HashMap<PendingAbilityType, PendingAbility> getPendingAbilities() {
        return pendingAbilities;
    }

    public void addStrongAttackAbility(int damageMultiplier,int knockbackPower, String attackSound, boolean particles) {
        PendingAbility ability = new PendingAbility();
        ability.intParams.put("DamageMultiplier", damageMultiplier);
        ability.intParams.put("KnockbackPower", knockbackPower);
        ability.stringParams.put("Particles", String.valueOf(particles));
        ability.stringParams.put("AttackSound", attackSound);
    }

    public class PendingAbility {
        public HashMap<String, Integer> intParams;
        public HashMap<String, String> stringParams;
    }

    public enum PendingAbilityType {
        STRONG_ATTACK,
    }

}
