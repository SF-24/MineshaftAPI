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

import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class AttributeHandler extends ComponentHandler {
    private final EquipmentSlot slot;
    private final HashMap<ItemStats,Double> statMap;

    // Handle custom attributes at the end, using nbt modifciation.
    double maximum_dex_modifier = 0;
    double defence = 0;
    double ranged_damage = 0;
    double speed = 0;

    public AttributeHandler(EquipmentSlot equipSlot, HashMap<ItemStats,Double> statMap, ConfigurationSection yamlConfiguration) {
        super(yamlConfiguration);
        this.slot=equipSlot;
        this.statMap=statMap;
    }

    @SuppressWarnings({"removal"})
    @Override
    public void handlePropertyPreMeta(ItemMeta meta) {
        for (ItemStats stat : statMap.keySet()) {
            double value = statMap.get(stat);

            // If an item has an attack speed modifier, the attack speed is 4 + the modifier.

            if (stat.equals(ItemStats.ATTACK_SPEED)) {
                value = value-4;
            }

            AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID().toString(), value, AttributeModifier.Operation.ADD_NUMBER);
            if (slot != null) {
                attributeModifier = new AttributeModifier(UUID.randomUUID(), UUID.randomUUID().toString(), value, AttributeModifier.Operation.ADD_NUMBER, slot);
            }
            if(stat.equals(ItemStats.DAMAGE)) {
                attributeModifier = new AttributeModifier(UUID.randomUUID().toString(), value-1, AttributeModifier.Operation.ADD_NUMBER);
                if (slot != null) {
                    attributeModifier = new AttributeModifier(UUID.randomUUID(), UUID.randomUUID().toString(), value-1, AttributeModifier.Operation.ADD_NUMBER, slot);
                }
            }

            switch (stat) {
                case DAMAGE:
                    meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, attributeModifier);
                    break;
                case MAXIMUM_ADDED_DEX_MODIFIER:
                    // For use with MineshaftRpg only
                    // Does nothing on its own
                    maximum_dex_modifier = value;
                    break;
                case ARMOUR:
                    meta.addAttributeModifier(Attribute.ARMOR, attributeModifier);
                    break;
                case ARMOUR_CLASS:
                    defence = value;
                    break;
                case SPEED:
                    speed = value;
                    break;
                case RANGED_DAMAGE:
                    ranged_damage = value;
                    break;
                case HEALTH:
                    meta.addAttributeModifier(Attribute.MAX_HEALTH, attributeModifier);
                    break;
                case ATTACK_REACH:
                    meta.addAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE, attributeModifier);
                    break;
                case MINING_REACH:
                    meta.addAttributeModifier(Attribute.BLOCK_INTERACTION_RANGE, attributeModifier);
                    break;
                case REACH:
                    meta.addAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE, attributeModifier);
                    meta.addAttributeModifier(Attribute.BLOCK_INTERACTION_RANGE, attributeModifier);
                    break;
                case ATTACK_SPEED:
                    meta.addAttributeModifier(Attribute.ATTACK_SPEED, attributeModifier);
                    break;
                case ATTACK_KNOCKBACK:
                    meta.addAttributeModifier(Attribute.ATTACK_KNOCKBACK, attributeModifier);
                    break;
                case SNEAKING_SPEED:
                    meta.addAttributeModifier(Attribute.SNEAKING_SPEED, attributeModifier);
                    break;
                case MINING_SPEED:
                    meta.addAttributeModifier(Attribute.BLOCK_BREAK_SPEED, attributeModifier);
                    break;
                default:
            }
        }
    }

    @Override
    public void handlePropertyPostMeta(ItemStack item) {
        if (speed != 0) {
            ItemManager.setCustomItemAttribute(item, ItemStats.SPEED, speed);
        }
        if (defence != 0) {
            ItemManager.setCustomItemAttribute(item, ItemStats.ARMOUR_CLASS, defence);
        }
        if (maximum_dex_modifier != 0) {
            ItemManager.setCustomItemAttribute(item, ItemStats.MAXIMUM_ADDED_DEX_MODIFIER, maximum_dex_modifier);
        }
        if(ranged_damage!=0) {
            ItemManager.setCustomItemAttribute(item, ItemStats.RANGED_DAMAGE, ranged_damage);
        }
    }
}
