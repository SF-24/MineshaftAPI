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

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.CookingBookCategory;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeHandler {

    String namespace;

    public RecipeHandler(String namespace) {
        this.namespace=namespace;
    }

    public FurnaceRecipe getFurnaceRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks, String id) {
        FurnaceRecipe recipe = new FurnaceRecipe(new NamespacedKey(namespace, id),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        return recipe;
    }

    public BlastingRecipe getBlastFurnaceRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks, String id) {
        BlastingRecipe recipe = new BlastingRecipe(new NamespacedKey(namespace, id),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        return recipe;
    }

    public SmokingRecipe getSmokerRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks, String id) {
        SmokingRecipe recipe = new SmokingRecipe(new NamespacedKey(namespace, id),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        return recipe;
    }

    public CampfireRecipe getCampfireRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks, String id) {
        CampfireRecipe recipe = new CampfireRecipe(new NamespacedKey(namespace, id),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        return recipe;
    }

    public Recipe getCraftingRecipeSimple(ItemStack item, List<Material> elements, CraftingBookCategory category, boolean isShaped, int width, int height, String key) {
        if(width>3)width=3;
        if(width<1||height<1) isShaped=false;

        if(isShaped) {
            ArrayList<Material> components = new ArrayList<>(elements);
            while(components.size() < width*height) {
                components.add(Material.AIR);
            }

            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(namespace, key), item);
            recipe.setCategory(category);

            HashMap<Material, Character> ingredients = new HashMap<>();

            StringBuilder recipeString = new StringBuilder();
            for(Material m : components) {
                if(m.equals(Material.AIR)) {
                    recipeString.append("_");
                } else if (!ingredients.containsKey(m)) {
                    String randomChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()=+,./;'[]<>?:{}|".substring(recipeString.length(),recipeString.length()+1);
                    ingredients.put(m,randomChar.charAt(0));
                    recipeString.append(randomChar);
                } else {
                    String keyChar = ingredients.get(m).toString();
                    ingredients.put(m,keyChar.charAt(0));
                    recipeString.append(keyChar);
                }
            }

            switch (height) {
                case 1 -> recipe.shape(
                        recipeString.substring(0,width).replace('_', ' '));
                case 2 -> recipe.shape(
                        recipeString.substring(0,width).replace('_', ' '),
                        recipeString.substring(width,width*2).replace('_', ' '));
                case 3 -> recipe.shape(
                        recipeString.substring(0,width).replace('_', ' '),
                        recipeString.substring(width,width*2).replace('_', ' '),
                        recipeString.substring(width*2,width*3).replace('_', ' '));
            }

            for(Material m : ingredients.keySet()) {
                if (!m.equals(Material.AIR)) {
                    recipe.setIngredient(ingredients.get(m), m);
                } else {
//                    recipe.setIngredient(ingredients.get(m),RecipeChoice.empty());
                }
            }

            return recipe;
        } else {
            ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(namespace, key), item);
            recipe.setCategory(category);
            for(Material m : elements) {
                recipe.addIngredient(m);
            }
            return recipe;
        }
    }

    // register a recipe
    public Recipe getCraftingRecipeComplex(ItemStack item, List<ItemStack> elements, CraftingBookCategory category, boolean isShaped, int width, int height, String key) {
        if(width>3)width=3;

        if(width<1||height<1) isShaped = false;

        ArrayList<ItemStack> components = new ArrayList<>(elements);

        if(isShaped) {
            while(components.size() < width*height) {
                components.add(new ItemStack(Material.AIR));
            }

            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(namespace, key), item);
            recipe.setCategory(category);

            HashMap<ItemStack, Character> ingredients = new HashMap<>();

            StringBuilder recipeString = new StringBuilder();
            for(ItemStack m : components) {
                if(m.equals(Material.AIR)) {
                    recipeString.append("_");
                } else if (!ingredients.containsKey(m)) {
                    String randomChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()=+,./;'[]<>?:{}|".substring(recipeString.length(),recipeString.length()+1);
                    ingredients.put(m,randomChar.charAt(0));
                    recipeString.append(randomChar);
                } else {
                    String keyChar = ingredients.get(m).toString();
                    ingredients.put(m,keyChar.charAt(0));
                    recipeString.append(keyChar);
                }
            }

            switch (height) {
                case 1 -> recipe.shape(
                        recipeString.substring(0,width).replace('_', ' '));
                case 2 -> recipe.shape(
                        recipeString.substring(0,width).replace('_', ' '),
                        recipeString.substring(width,width*2).replace('_', ' '));
                case 3 -> recipe.shape(
                        recipeString.substring(0,width).replace('_', ' '),
                        recipeString.substring(width,width*2).replace('_', ' '),
                        recipeString.substring(width*2,width*3).replace('_', ' '));
            }

            for(ItemStack m : ingredients.keySet()) {
                if (!m.getType().equals(Material.AIR)) {
                    recipe.setIngredient(ingredients.get(m), m);
                }
            }
            return recipe;
        } else {
            ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(namespace, key), item);
            recipe.setCategory(category);
            for(ItemStack m : components) {
                recipe.addIngredient(m);
            }
            return recipe;
        }
    }

}
