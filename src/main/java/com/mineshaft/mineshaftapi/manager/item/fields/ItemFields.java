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

package com.mineshaft.mineshaftapi.manager.item.fields;

import com.mineshaft.mineshaftapi.manager.VariableTypeEnum;
import org.bukkit.Material;

import java.util.Locale;
import java.util.UUID;

public enum ItemFields {

    name(VariableTypeEnum.STRING, "Cleaver"),
    rarity(VariableTypeEnum.ITEM_RARITY, ItemRarity.EXOTIC.name().toLowerCase(Locale.ROOT)),
    item_category(VariableTypeEnum.ITEM_CATEGORY, ItemCategory.WEAPON_MELEE.name().toLowerCase(Locale.ROOT)),
    material(VariableTypeEnum.MATERIAL, Material.GOLDEN_SWORD.name()),
    id(VariableTypeEnum.UUID, UUID.randomUUID().toString()),
    custom_model_data(VariableTypeEnum.INTEGER, 0),
    parent(VariableTypeEnum.STRING, "null"),
    durability(VariableTypeEnum.INTEGER, 250),
    stack_size(VariableTypeEnum.INTEGER, 1),
    enchantment_glint(VariableTypeEnum.BOOLEAN, false),
    hide_attributes(VariableTypeEnum.BOOLEAN, true),
    subcategory(VariableTypeEnum.STRING, ItemSubcategory.SWORD.name().toLowerCase());

    private final VariableTypeEnum variableType;
    private final Object defaultValue;

    ItemFields(VariableTypeEnum variableType, Object defaultValue) {
        this.variableType=variableType;
        this.defaultValue=defaultValue;
    }

    public VariableTypeEnum getVariableType() {return variableType;}

    public Object getDefaultValue() {return defaultValue;}
}
