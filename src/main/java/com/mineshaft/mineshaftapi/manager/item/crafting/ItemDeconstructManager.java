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

package com.mineshaft.mineshaftapi.manager.item.crafting;

import com.mineshaft.mineshaftapi.MineshaftApi;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.recipe.CookingBookCategory;

public class ItemDeconstructManager {

    public static double getDurabilityFraction(ItemStack itemStack) {
        return ItemRepairManager.getDamage(itemStack)/ItemRepairManager.getMaximumDamage(itemStack);
    }

    // Register recipes for melting down items
    public static void registerMeltingRecipes(String itemName) {
        if(itemName==null || ItemRecipeManager.getHardcodedCraftingRecipe(itemName)==null) return;
        if(ItemRecipeManager.getCraftingMaterials(itemName)==null || !ItemRecipeManager.getCraftingMaterials(itemName).containsKey("melting")) return;

        ItemStack result = new ItemStack(ItemRecipeManager.getCraftingMaterials(itemName).get("melting"));
        if(result.getType().equals(Material.AIR)) return;

        result.setAmount(RecipeRegistrar.getHardcodedRecipeItemNumber(ItemRecipeManager.getHardcodedCraftingRecipe(itemName),true));

        ItemStack item = MineshaftApi.getInstance().getItemManagerInstance().getItem(itemName);

        // Register recipes via the recipe registrar
        if(MineshaftApi.getInstance().getConfigManager().enableBlastFurnaceMelting()) {
            RecipeRegistrar.registerBlastFurnaceRecipe(item,result, CookingBookCategory.MISC,0,MineshaftApi.getInstance().getConfigManager().getDefaultUnsmeltingTime());
        }
        if(MineshaftApi.getInstance().getConfigManager().enableFurnaceMelting()) {
            RecipeRegistrar.registerFurnaceRecipe(item,result, CookingBookCategory.MISC,0,MineshaftApi.getInstance().getConfigManager().getDefaultUnsmeltingTime());
        }
        if(MineshaftApi.getInstance().getConfigManager().enableSmokerMelting()) {
            RecipeRegistrar.registerSmokerRecipe(item,result, CookingBookCategory.MISC,0,MineshaftApi.getInstance().getConfigManager().getDefaultUnsmeltingTime());
        }
        if(MineshaftApi.getInstance().getConfigManager().enableCampfireMelting()) {
            RecipeRegistrar.registerCampfireRecipe(item,result, CookingBookCategory.MISC,0,MineshaftApi.getInstance().getConfigManager().getDefaultUnsmeltingTime());
        }
    }

    public static ItemStack getMeltingRecipeResult(ItemStack item) {
        String itemName = MineshaftApi.getInstance().getItemManagerInstance().getItemNameFromItem(item);
        if(itemName==null || ItemRecipeManager.getHardcodedCraftingRecipe(itemName)==null) return new ItemStack(Material.AIR);
        if(ItemRecipeManager.getCraftingMaterials(itemName)==null || !ItemRecipeManager.getCraftingMaterials(itemName).containsKey("melting")) return new ItemStack(Material.AIR);

        ItemStack result = new ItemStack(ItemRecipeManager.getCraftingMaterials(itemName).get("melting"));
        if(result.getType().equals(Material.AIR)) return new ItemStack(Material.AIR);

        int newAmount = (int) ((
                        getDurabilityFraction(item)
                        *RecipeRegistrar.getHardcodedRecipeItemNumber(ItemRecipeManager.getHardcodedCraftingRecipe(itemName),true))
                        +0.12);
        if(newAmount == 0) return new ItemStack(Material.AIR);
        result.setAmount(newAmount);
        return result;
    }

}
