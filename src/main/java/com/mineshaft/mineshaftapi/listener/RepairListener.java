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

package com.mineshaft.mineshaftapi.listener;

import com.mineshaft.mineshaftapi.manager.item.ItemRepairManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class RepairListener implements Listener {

    void onCraft(PrepareItemCraftEvent e) {
        if(ChatColor.translateAlternateColorCodes('&',e.getView().getTitle()).equalsIgnoreCase(ChatColor.BLACK + "Item Repair")) {

            int items = 0;
            for(int i = 0; i<9; i++) {
                if(e.getInventory().getItem(i)!=null) items++;
            }
            if(items==2) {
                e.getView().setItem(9, new ItemStack(Material.DIAMOND_BLOCK));

            } else {
                e.getView().setItem(9, new ItemStack(Material.AIR));
            }
        }
    }


    @EventHandler
    void onAnvilRepair(PrepareAnvilEvent e) {
        ItemStack item = e.getInventory().getFirstItem();
        if(e.getInventory().getSecondItem()==null || e.getInventory().getSecondItem().getType().equals(Material.AIR)) {
            return;
        }
        Material repairItem = e.getInventory().getSecondItem().getType();

        e.getView().setRepairCost(0);
        e.getView().setMaximumRepairCost(0);

        // Null check
        if(item == null || item.getType() == Material.AIR) return;

        int repairAmount = e.getInventory().getSecondItem().getAmount();

        if(ItemRepairManager.isRepairItem(item,repairItem)) {
            try {
                e.getView().setRepairItemCountCost(Math.min(repairAmount,ItemRepairManager.getRepairItemNumber(item,repairItem)));
                e.setResult(ItemRepairManager.getRepairedItem(item, repairItem, repairAmount));
//                Bukkit.getScheduler().runTaskLater(MineshaftApi.getInstance(),()->{
//                    e.getInventory().setUpperItem(ItemRepairManager.getRepairedItem(item, repairItem, repairAmount));
//                    if(repairAmount<=ItemRepairManager.getRepairItemNumber(item,repairItem)) {
//                        e.getInventory().setLowerItem(new ItemStack(Material.AIR));
//                    } else {
//                        ItemStack mat = e.getInventory().getLowerItem();
//                        mat.setAmount(repairAmount-ItemRepairManager.getRepairItemNumber(item,repairItem));
//                        e.getInventory().setLowerItem(mat);
//                    }
//                },1/99);
            } catch (Exception ignored) {}
        }
    }

}
