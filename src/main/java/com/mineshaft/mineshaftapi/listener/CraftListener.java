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

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item.crafting.ItemDeconstructManager;
import com.mineshaft.mineshaftapi.manager.item.crafting.ItemRepairManager;
import com.mineshaft.mineshaftapi.manager.item.crafting.RecipeKey;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.*;

public class CraftListener implements Listener {

    @EventHandler
    void onSmelt(BlockCookEvent e) {
        if(MineshaftApi.getInstance().getItemManagerInstance().getItemNameFromItem(e.getSource())!=null) {
            String itemName = MineshaftApi.getInstance().getItemManagerInstance().getItemNameFromItem(e.getSource());
            e.setResult(ItemDeconstructManager.getMeltingRecipeResult(e.getSource()));
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

    @EventHandler
    public void onPlayerCraft(CraftItemEvent e) {
        if(e.getWhoClicked() instanceof Player player) {
            Recipe recipe = e.getRecipe();
            NamespacedKey namespacedKey = null;
            if(recipe instanceof ShapedRecipe) {
                namespacedKey=((ShapedRecipe) recipe).getKey();
            } else if(recipe instanceof ShapelessRecipe) {
                namespacedKey=((ShapelessRecipe) recipe).getKey();
            } else if(recipe instanceof CampfireRecipe) {
                namespacedKey=((CampfireRecipe) recipe).getKey();
            } else if(recipe instanceof BlastingRecipe) {
                namespacedKey=((BlastingRecipe) recipe).getKey();
            } else if(recipe instanceof FurnaceRecipe) {
                namespacedKey=((FurnaceRecipe) recipe).getKey();
            } else if(recipe instanceof SmokingRecipe) {
                namespacedKey=((SmokingRecipe) recipe).getKey();
            }
            if(namespacedKey!=null && MineshaftApi.getGlobalRecipeCache().isLocked(namespacedKey)) {
                // If the recipe is locked and the player has not learned it, stop the item from being crafted.
                if(!JsonPlayerBridge.getDiscoveredRecipes(player).contains(new RecipeKey(namespacedKey))) {
                    player.sendMessage(ChatColor.RED + "You have not learned this crafting recipe.");
                    e.setCancelled(true);
                }
            }
        }
    }

}
