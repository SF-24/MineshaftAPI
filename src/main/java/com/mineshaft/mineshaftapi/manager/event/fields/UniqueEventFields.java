/*
 *     This program is a Minecraft plugin developed in Java for the Spigot API.
 *     It adds multiple RPG features intended for Multiplayer gameplay.
 *
 *     Copyright (C) 2024  Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.mineshaft.mineshaftapi.manager.event.fields;

import com.mineshaft.mineshaftapi.manager.VariableTypeEnum;

public enum UniqueEventFields {

    AMOUNT(VariableTypeEnum.INTEGER, 5),
    TARGET(VariableTypeEnum.STRING, "self"),
    SOUND(VariableTypeEnum.STRING, ""),
    COLOUR(VariableTypeEnum.STRING, "000000"),
    RANGE(VariableTypeEnum.INTEGER, 40),
    SPEED(VariableTypeEnum.INTEGER, 4),
    PROJECTILE_TYPE(VariableTypeEnum.PROJECTILE_TYPE, "ARROW"),
    TRIGGER(VariableTypeEnum.STRING, "entity"),
    PARTICLE(VariableTypeEnum.INTEGER,50);

    private final Object defaultValue;
    private final VariableTypeEnum variableType;

    UniqueEventFields(VariableTypeEnum variableType, Object defaultValue) {
        this.variableType = variableType;
        this.defaultValue = defaultValue;
    }

    public Object getDefaultValue() {return defaultValue;}
    public VariableTypeEnum getVariableType() {return variableType;}


}
