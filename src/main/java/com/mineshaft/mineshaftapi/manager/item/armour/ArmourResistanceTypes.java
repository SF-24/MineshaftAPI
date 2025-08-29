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

package com.mineshaft.mineshaftapi.manager.item.armour;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public enum ArmourResistanceTypes {
    COLD_PROTECTION("Cold Protection", "cold_protection"),
    FIRE_PROTECTION("Fire Protection", "fire_protection"),

    ;
    final String display,nbt;
    ArmourResistanceTypes(String display, String nbt) {
        this.display=display;
        this.nbt=nbt;
    }

}
