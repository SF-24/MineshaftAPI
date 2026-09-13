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

import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.item_components.armour.ArmourManager;
import com.mineshaft.mineshaftapi.manager.item.item_components.armour.ArmourResistanceTypes;
import de.tr7zw.nbtapi.NBT;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ArmourResistancesComponent extends CustomItemComponent {
    @Getter
    private final ArrayList<ArmourResistanceTypes> armourResistanceTypes;

    public ArmourResistancesComponent(ArrayList<ArmourResistanceTypes> armourResistanceTypes) {
        this.armourResistanceTypes = armourResistanceTypes;
    }

    @Override
    public void applyPropertyToItemPostItemMeta(ItemStack itemStack) {
        NBT.modify(itemStack,nbt->{
            for(ArmourResistanceTypes element : armourResistanceTypes) {
                nbt.setBoolean(element.name().toLowerCase(), true);
            }
        });
    }

    // For easy function building
    public ArmourResistancesComponent addResistance(ArmourResistanceTypes armourResistanceType) {
        armourResistanceTypes.add(armourResistanceType);
        return this;
    }

    @Override
    public CustomItemComponent fromItem(ItemStack itemStack) {
        return new ArmourResistancesComponent(ArmourManager.getArmourResistances(ItemManager.getItemIdFromItem(itemStack)));
    }
}
