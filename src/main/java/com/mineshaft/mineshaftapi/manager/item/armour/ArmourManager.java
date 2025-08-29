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

import com.mineshaft.mineshaftapi.manager.item.ItemManager;

import java.util.ArrayList;
import java.util.UUID;

public class ArmourManager {

    public static ArmourType getArmourType(UUID uniqueId) {
        if (ItemManager.getYamlConfiguration(uniqueId).contains("armour.type")) {
            return ArmourType.valueOf(ItemManager.getYamlConfiguration(uniqueId).getString("armour.type"));
        } else if (ItemManager.getYamlConfiguration(uniqueId).contains("armor.type")) {
            return ArmourType.valueOf(ItemManager.getYamlConfiguration(uniqueId).getString("armor.type"));
        } else {
            return ArmourType.NONE;
        }
    }

    // Get armour resistances
    public static ArrayList<ArmourResistanceTypes> getArmourResistances(UUID uniqueId) {
        ArrayList<ArmourResistanceTypes> resistances = new ArrayList<>();
        for(String resistance : ItemManager.getYamlConfiguration(uniqueId).getStringList("armour.resistances")) {
            resistances.add(ArmourResistanceTypes.valueOf(resistance.toUpperCase()));
        }
        if(ItemManager.getYamlConfiguration(uniqueId).contains("armour.cold_protection")) {
            resistances.add(ArmourResistanceTypes.COLD_PROTECTION);
        }
        return resistances;
    }

}
