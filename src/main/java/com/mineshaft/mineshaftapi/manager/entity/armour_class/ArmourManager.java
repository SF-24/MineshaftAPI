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

package com.mineshaft.mineshaftapi.manager.entity.armour_class;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.entity.Entity;

public class ArmourManager {

    public static void setArmourClass(Entity entity, int armourClass) {
        NBT.modify(entity, nbt->{
            nbt.setInteger("armour_class",armourClass);
        });
    }

    public static int getArmourClass(Entity entity) {
        if(entity==null) return 0;
        NBT.get(entity, nbt->{
            return nbt.getInteger("armour_class");
        });
        return 0;
    }

}
