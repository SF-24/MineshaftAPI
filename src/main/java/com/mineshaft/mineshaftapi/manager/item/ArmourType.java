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

package com.mineshaft.mineshaftapi.manager.item;

public enum ArmourType {

    NONE("",0),
    CLOTHES("Clothes", 0),
    LIGHT_ARMOUR("Light Armour", 0),
    MEDIUM_ARMOUR("Medium Armour", 13),
    HEAVY_ARMOUR("Heavy Armour", 15),;

    private final String name;
    private final int strRequirement;

    ArmourType(String name, int strRequirement) {
        this.name=name;
        this.strRequirement=strRequirement;
    }

    public String getName() { return name;}
    public int getStrRequirement() { return strRequirement;}

}
