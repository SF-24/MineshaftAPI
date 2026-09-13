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

package com.mineshaft.mineshaftapi.manager.item.item_components.handlers;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

public class EquippableComponentHandler extends ComponentHandler {
    private final EquipmentSlot itemSlot;

    public EquippableComponentHandler(EquipmentSlot itemSlot, ConfigurationSection yamlConfiguration) {
        super(yamlConfiguration);
        this.itemSlot = itemSlot;
    }

    @Override
    public void handlePropertyPreMeta(ItemMeta itemMeta) {
        itemMeta.setEquippable(getEquippableComponent(itemMeta));
    }


    @Override
    public void handlePropertyPostMeta(ItemStack itemStack) {
    }

    private EquippableComponent getEquippableComponent(ItemMeta itemMeta) {
        EquippableComponent baseEquippableComponent = itemMeta.getEquippable();
        if(baseEquippableComponent==null) {baseEquippableComponent=new ItemStack(Material.IRON_CHESTPLATE).getItemMeta().getEquippable();}

        baseEquippableComponent.setSlot(itemSlot);
        for(String key : yamlConfiguration.getConfigurationSection("armour").getKeys(false)) {
            String path = "armour."+key;
            switch (key) {
                case "equip_sound":
                    baseEquippableComponent.setEquipSound(Registry.SOUNDS.get(NamespacedKey.fromString(yamlConfiguration.getString(path))));
                    break;
                case "model":
                    baseEquippableComponent.setModel(NamespacedKey.fromString(yamlConfiguration.getString(path)));
                    break;
                case "damage_on_hurt":
                    baseEquippableComponent.setDamageOnHurt(yamlConfiguration.getBoolean(path));
                    break;
                default:
                    break;
            }
        }
        return baseEquippableComponent;
    }
}
