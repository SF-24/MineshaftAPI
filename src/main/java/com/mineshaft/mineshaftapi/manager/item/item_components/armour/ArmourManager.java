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

package com.mineshaft.mineshaftapi.manager.item.item_components.armour;

import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.item_components.custom_property_classes.ArmourResistancesComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.EquippableComponent;

import java.util.ArrayList;
import java.util.UUID;

public class ArmourManager {

    public static ArmourType getArmourType(UUID uniqueId) {
        return ArmourConfigHandler.getArmourType(ItemManager.getItemDefinition(uniqueId));
    }

    public static ArrayList<ArmourResistanceTypes> getArmourResistances(UUID uniqueId) {
        return ArmourConfigHandler.getArmourResistances(ItemManager.getItemDefinition(uniqueId));
    }

    public static ArmourResistancesComponent getArmourResistanceComponent(UUID uniqueId) {
        return new ArmourResistancesComponent(ArmourConfigHandler.getArmourResistances(ItemManager.getItemDefinition(uniqueId)));
    }

    public static class ArmourConfigHandler {
        // Get armour resistances
        public static ArrayList<ArmourResistanceTypes> getArmourResistances(ConfigurationSection yamlConfiguration) {
            ArrayList<ArmourResistanceTypes> resistances = new ArrayList<>();
            for(String resistance : yamlConfiguration.getStringList("armour.resistances")) {
                resistances.add(ArmourResistanceTypes.valueOf(resistance.toUpperCase()));
            }
            if(yamlConfiguration.contains("armour.cold_protection")) {
                resistances.add(ArmourResistanceTypes.COLD_PROTECTION);
            }
            return resistances;
        }

        public static ArmourType getArmourType(ConfigurationSection yamlConfiguration) {
            if (yamlConfiguration.contains("armour.type")) {
                return ArmourType.valueOf(yamlConfiguration.getString("armour.type"));
            } else if (yamlConfiguration.contains("armor.type")) {
                return ArmourType.valueOf(yamlConfiguration.getString("armor.type"));
            } else {
                return ArmourType.NONE;
            }
        }
    }

}
