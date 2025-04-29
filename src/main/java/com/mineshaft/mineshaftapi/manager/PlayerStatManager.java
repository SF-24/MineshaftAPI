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

package com.mineshaft.mineshaftapi.manager;

import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemCategory;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class PlayerStatManager {

    public static double getPlayerStat(ItemStats stat, Player player) {
        int value = 0;
        if(stat.equals(ItemStats.SPEED)) value = 100;

        List<ItemCategory> armourCategories = new java.util.ArrayList<>();
        armourCategories.add(ItemCategory.ARMOUR_HELMET);
        armourCategories.add(ItemCategory.ARMOUR_CHESTPLATE);
        armourCategories.add(ItemCategory.ARMOUR_LEGGINGS);
        armourCategories.add(ItemCategory.ARMOUR_BOOTS);
        
        ItemCategory mainHandCategory = ItemManager.getItemNbtCategory(player.getInventory().getItemInMainHand());
        ItemCategory offHandCategory = ItemManager.getItemNbtCategory(player.getInventory().getItemInMainHand());
        
        if(!armourCategories.contains(mainHandCategory)) {
            value += ItemManager.getItemNbtStat(player.getInventory().getItemInMainHand(), stat);
        }
        if(!armourCategories.contains(offHandCategory)) {
            value += ItemManager.getItemNbtStat(player.getInventory().getItemInOffHand(), stat);
        }
        value+=ItemManager.getItemNbtStat(player.getInventory().getHelmet(), stat);
        value+=ItemManager.getItemNbtStat(player.getInventory().getChestplate(), stat);
        value+=ItemManager.getItemNbtStat(player.getInventory().getLeggings(), stat);
        value+=ItemManager.getItemNbtStat(player.getInventory().getBoots(), stat);

        return value;
    }

}
