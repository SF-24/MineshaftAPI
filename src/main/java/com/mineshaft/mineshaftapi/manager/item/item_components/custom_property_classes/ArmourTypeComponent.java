/*
 * Copyright (c) 2026. Sebastian Frynas
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

package com.mineshaft.mineshaftapi.manager.item.item_components.custom_property_classes;

import com.mineshaft.mineshaftapi.manager.item.item_components.armour.ArmourType;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.inventory.ItemStack;

public class ArmourTypeComponent extends CustomItemComponent {

    public ArmourType armourType;

    public ArmourTypeComponent(ArmourType armourType) {
        super();
        this.armourType=armourType;
    }

    @Override
    public void applyPropertyToItemPostItemMeta(ItemStack itemStack) {
        NBT.modify(itemStack, nbt -> {
            nbt.setString("ArmourType",armourType.name().toLowerCase());
        });
    }

    public CustomItemComponent fromItem(ItemStack itemStack) {
        NBT.get(itemStack, nbt -> {
            return new ArmourTypeComponent(ArmourType.valueOf(nbt.getString("ArmourType")));
        });
        return new ArmourTypeComponent(ArmourType.NONE);
    }
}
