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

package com.mineshaft.mineshaftapi.manager.event.event_subclass;

import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import com.mineshaft.mineshaftapi.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerWandEvent extends Event {


    public PlayerWandEvent(EventType type) {
        super(type);
    }

    public void wandLightEvent(Player player) {
        if(player==null) return;
        if(ItemUtil.isWand(player.getInventory().getItemInMainHand())) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if(item.getType() != Material.LAVA_BUCKET) {
                ItemMeta meta = item.getItemMeta();
                item = new ItemStack(Material.LAVA_BUCKET);
                item.setItemMeta(meta);
            }
            player.getInventory().setItemInMainHand(item);
        }
    }

    public void wandExtinguishEvent(Player player) {
        if(player==null) return;
        if(ItemUtil.isWand(player.getInventory().getItemInMainHand())) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if(item.getType() != Material.MINECART) {
                ItemMeta meta = item.getItemMeta();
                item = new ItemStack(Material.MINECART);
                item.setItemMeta(meta);
            }
            player.getInventory().setItemInMainHand(item);
        }
    }
}
