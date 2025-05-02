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

import com.mineshaft.mineshaftapi.dependency.DependencyInit;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.entity.Entity;

public class MythicMobManager {

    public static boolean isMythicMob(Entity bukkitEntity) {
        if(DependencyInit.hasMythicMobs()) {
            return MythicBukkit.inst().getMobManager().getActiveMob(bukkitEntity.getUniqueId()).isPresent();
        }
        return false;
    }

}
