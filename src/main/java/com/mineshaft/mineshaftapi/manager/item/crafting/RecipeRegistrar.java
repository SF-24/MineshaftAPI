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
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.CookingBookCategory;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RecipeRegistrar {

    public static final Material air = Material.AIR;

    public static void addHardcodedRecipe(ItemStack item, HardcodedRecipe recipe, Material m1, Material m2) {
        Logger.logDebug("adding hardcoded recipe " + recipe + " to " + item);

        switch(recipe) {
            case SWORD,SHORTSWORD -> registerCraftingRecipe(item,
                    List.of(m1, m1, m2), recipe.getCategory(),
                    true, 1, 3
            );
            case LONGSWORD -> registerCraftingRecipe(item,
                    List.of(
                            air, m1, air,
                            air, m1, air,
                            m1, m2, m1
                    )
                    , recipe.getCategory(),
                    true, 3, 3
            );
            case PICKAXE -> registerCraftingRecipe(item,
                    List.of(
                            m1, m1, m1,
                            air, m2, air,
                            air, m2, air
                    )
                    , recipe.getCategory(),
                    true, 3, 3
            );
            case AXE -> {

                registerCraftingRecipe(item,
                        List.of(
                                m1, m1,
                                m2, m1,
                                m2, air
                        )
                        , recipe.getCategory(),
                        true, 2, 3
                );
                registerCraftingRecipe(item,
                        List.of(
                                m1, m1,
                                m1, m2,
                                air, m2
                        )
                        , recipe.getCategory(),
                        true, 2, 3
                );
            }

            case SHOVEL -> registerCraftingRecipe(item,
                    List.of(
                            m1,
                            m2,
                            m2
                    )
                    , recipe.getCategory(),
                    true, 1, 3
            );
            case HOE -> {
                registerCraftingRecipe(item,
                    List.of(
                            m1, m1,
                            air, m2,
                            air, m2
                    )
                    , recipe.getCategory(),
                    true, 2, 3
                );
                registerCraftingRecipe(item,
                        List.of(
                                m1, m1,
                                m2, air,
                                m2, air
                        )
                        , recipe.getCategory(),
                        true, 2, 3
                );
            }
            case HELMET_SIMPLE ->registerCraftingRecipe(item,
                    List.of(
                            m1, m1,m1,
                            m1, air,m1
                    )
                    , recipe.getCategory(),
                    true, 3, 2
            );
            case CHESTPLATE_SIMPLE ->registerCraftingRecipe(item,
                    List.of(
                            m1, air, m1,
                            m1, m1, m1,
                            m1, m1, m1
                    )
                    , recipe.getCategory(),
                    true, 3, 3
            );
            case LEGGINGS_SIMPLE ->registerCraftingRecipe(item,
                    List.of(
                            m1, m1, m1,
                            m1, air, m1,
                            m1, air, m1
                    )
                    , recipe.getCategory(),
                    true, 3, 3
            );
            case BOOTS_SIMPLE ->registerCraftingRecipe(item,
                    List.of(
                            m1, air,m1,
                            m1, air,m1
                    )
                    , recipe.getCategory(),
                    true, 3, 2
            );
        }
    }

    public static int getHardcodedRecipeItemNumber(HardcodedRecipe recipe, boolean mainItem) {
        switch(recipe) {
            case SWORD, SHORTSWORD -> {
                if(mainItem) return 2; else return 1;
            }
            case LONGSWORD -> {
                if(mainItem) return 4; else return 1;
            }
            case PICKAXE, AXE -> {
                if(mainItem) return 3; else return 2;
            }
            case SHOVEL -> {
                if(mainItem) return 1; else return 2;
            }
            case HOE -> {
                if(mainItem) return 2; else return 2;
            }
            case HELMET_SIMPLE -> {
                if(mainItem) return 5;
            }
            case CHESTPLATE_SIMPLE -> {
                if(mainItem) return 8;
            }
            case LEGGINGS_SIMPLE -> {
                if(mainItem) return 7;
            }
            case BOOTS_SIMPLE -> {
                if(mainItem) return 4;
            }
        }
        return 0;
    }

    // register a recipe
    public static void registerCraftingRecipe(ItemStack item, List<Material> components, CraftingBookCategory category, boolean isShaped, int width, int height) {
        if(width>3)width=3;

        if(isShaped) {
            while(components.size() < width*height) {
                components.add(Material.AIR);
            }

            String key = UUID.randomUUID().toString();
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(MineshaftApi.getInstance(), key), item);
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

            System.out.println("recipe pattern: " + recipeString + ", key: " + ingredients);

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

            Bukkit.getServer().addRecipe(recipe);
        } else {
            String key = UUID.randomUUID().toString();
            ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(MineshaftApi.getInstance(), key), item);
            recipe.setCategory(category);
            for(Material m : components) {
                recipe.addIngredient(m);
            }
        }
    }

    public static void registerFurnaceRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        FurnaceRecipe recipe = new FurnaceRecipe(new NamespacedKey(MineshaftApi.getInstance(), UUID.randomUUID().toString()),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        Bukkit.getServer().addRecipe(recipe);
    }

    public static void registerBlastFurnaceRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        BlastingRecipe recipe = new BlastingRecipe(new NamespacedKey(MineshaftApi.getInstance(), UUID.randomUUID().toString()),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        Bukkit.getServer().addRecipe(recipe);
    }

    public static void registerSmokerRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        SmokingRecipe recipe = new SmokingRecipe(new NamespacedKey(MineshaftApi.getInstance(), UUID.randomUUID().toString()),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        Bukkit.getServer().addRecipe(recipe);
    }

    public static void registerCampfireRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        CampfireRecipe recipe = new CampfireRecipe(new NamespacedKey(MineshaftApi.getInstance(), UUID.randomUUID().toString()),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        Bukkit.getServer().addRecipe(recipe);
    }
}
