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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.CookingBookCategory;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RecipeRegistrar implements Listener {

    String namespace;

    public ArrayList<String> recipeCache = new ArrayList<>();

    public RecipeRegistrar(String namespace) {
        if(namespace == null) {
            this.namespace = namespace;
        } else {
            this.namespace = "mineshaftdefault";
        }
            Bukkit.getPluginManager().registerEvents(this, MineshaftApi.getInstance());
    }

    public final Material air = Material.AIR;

    public void addHardcodedRecipe(ItemStack item, HardcodedRecipe recipe, Material m1, Material m2) {

        switch(recipe) {
            case SWORD,SHORTSWORD -> this.registerCraftingRecipeSimple(item,
                    List.of(m1, m1, m2), recipe.getCategory(),
                    true, 1, 3
            );
            case LONGSWORD -> this.registerCraftingRecipeSimple(item,
                    List.of(
                            air, m1, air,
                            air, m1, air,
                            m1, m2, m1
                    )
                    , recipe.getCategory(),
                    true, 3, 3
            );
            case PICKAXE -> this.registerCraftingRecipeSimple(item,
                    List.of(
                            m1, m1, m1,
                            air, m2, air,
                            air, m2, air
                    )
                    , recipe.getCategory(),
                    true, 3, 3
            );
            case AXE -> {

                this.registerCraftingRecipeSimple(item,
                        List.of(
                                m1, m1,
                                m2, m1,
                                m2, air
                        )
                        , recipe.getCategory(),
                        true, 2, 3
                );
                this.registerCraftingRecipeSimple(item,
                        List.of(
                                m1, m1,
                                m1, m2,
                                air, m2
                        )
                        , recipe.getCategory(),
                        true, 2, 3
                );
            }

            case SHOVEL -> this.registerCraftingRecipeSimple(item,
                    List.of(
                            m1,
                            m2,
                            m2
                    )
                    , recipe.getCategory(),
                    true, 1, 3
            );
            case HOE -> {
                this.registerCraftingRecipeSimple(item,
                    List.of(
                            m1, m1,
                            air, m2,
                            air, m2
                    )
                    , recipe.getCategory(),
                    true, 2, 3
                );
                this.registerCraftingRecipeSimple(item,
                        List.of(
                                m1, m1,
                                m2, air,
                                m2, air
                        )
                        , recipe.getCategory(),
                        true, 2, 3
                );
            }
            case HELMET_SIMPLE -> this.registerCraftingRecipeSimple(item,
                    List.of(
                            m1, m1,m1,
                            m1, air,m1
                    )
                    , recipe.getCategory(),
                    true, 3, 2
            );
            case CHESTPLATE_SIMPLE -> this.registerCraftingRecipeSimple(item,
                    List.of(
                            m1, air, m1,
                            m1, m1, m1,
                            m1, m1, m1
                    )
                    , recipe.getCategory(),
                    true, 3, 3
            );
            case LEGGINGS_SIMPLE -> this.registerCraftingRecipeSimple(item,
                    List.of(
                            m1, m1, m1,
                            m1, air, m1,
                            m1, air, m1
                    )
                    , recipe.getCategory(),
                    true, 3, 3
            );
            case BOOTS_SIMPLE -> this.registerCraftingRecipeSimple(item,
                    List.of(
                            m1, air,m1,
                            m1, air,m1
                    )
                    , recipe.getCategory(),
                    true, 3, 2
            );
        }
    }

    public int getHardcodedRecipeItemNumber(HardcodedRecipe recipe, boolean mainItem) {
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
    public void registerCraftingRecipeSimple(ItemStack item, List<Material> elements, CraftingBookCategory category, boolean isShaped, int width, int height) {
        if(width>3)width=3;
        if(width<1||height<1) isShaped=false;

        if(isShaped) {
            ArrayList<Material> components = new ArrayList<>(elements);
            while(components.size() < width*height) {
                components.add(Material.AIR);
            }

            String key = UUID.randomUUID().toString();
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

            cacheRecipe(key);
            Bukkit.getServer().addRecipe(recipe);
        } else {
            String key = UUID.randomUUID().toString();
            ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(namespace, key), item);
            cacheRecipe(key);
            recipe.setCategory(category);
            for(Material m : elements) {
                recipe.addIngredient(m);
            }
        }
    }

    // register a recipe
    public void registerCraftingRecipeComplex(ItemStack item, List<ItemStack> elements, CraftingBookCategory category, boolean isShaped, int width, int height) {
        if(width>3)width=3;

        if(width<1||height<1) isShaped = false;

        ArrayList<ItemStack> components = new ArrayList<>(elements);

        if(isShaped) {
            while(components.size() < width*height) {
                components.add(new ItemStack(Material.AIR));
            }

            String key = UUID.randomUUID().toString();
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(namespace, key), item);
            recipe.setCategory(category);
            cacheRecipe(key);

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

            Bukkit.getServer().addRecipe(recipe);
        } else {
            String key = UUID.randomUUID().toString();
            ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(namespace, key), item);
            cacheRecipe(key);
            recipe.setCategory(category);
            for(ItemStack m : components) {
                recipe.addIngredient(m);
            }
            Bukkit.getServer().addRecipe(recipe);
        }
    }

    public void registerFurnaceRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        String id = UUID.randomUUID().toString();
        cacheRecipe(id);
        FurnaceRecipe recipe = new FurnaceRecipe(new NamespacedKey(namespace, id),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void registerFurnaceRecipeSimpleFood(ItemStack item, ItemStack output) {
        registerFurnaceRecipeSimple(item,output,CookingBookCategory.FOOD);
    }

    public void registerFurnaceRecipeSimple(ItemStack item, ItemStack output, CookingBookCategory category) {
        registerFurnaceRecipe(item,output,category,0,200);
    }

    public void registerBlastFurnaceRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        String id = UUID.randomUUID().toString();
        BlastingRecipe recipe = new BlastingRecipe(new NamespacedKey(namespace, id),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        cacheRecipe(id);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void registerSmokerRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        String id = UUID.randomUUID().toString();
        SmokingRecipe recipe = new SmokingRecipe(new NamespacedKey(namespace, id),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        cacheRecipe(id);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void registerCampfireRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        String id = UUID.randomUUID().toString();
        cacheRecipe(id);
        CampfireRecipe recipe = new CampfireRecipe(new NamespacedKey(namespace, id),output,new RecipeChoice.ExactChoice(item),xp,timeInTicks);
        recipe.setCategory(category);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void registerShapelessRecipe(ItemStack item, List<ItemStack> components, CraftingBookCategory category) {
        registerCraftingRecipeComplex(item,components,category,false,-1,-1);
    }

    public void registerShapelessRecipeMisc(ItemStack item, List<ItemStack> components) {
        registerCraftingRecipeComplex(item,components,CraftingBookCategory.MISC,false,-1,-1);
    }

    public void cacheRecipe(String id) {
        recipeCache.add(id);
    }

    public void clearRecipes() {
        for(String id : recipeCache) {
            Bukkit.getServer().removeRecipe(new NamespacedKey(namespace, id));
        }
    }

    public void giveRecipes(Player player) {
        for(String id : recipeCache) {
            player.discoverRecipe(new NamespacedKey(namespace, id));
        }
    }

    public void removeRecipes(Player player) {
        for(String id : recipeCache) {
            player.undiscoverRecipe(new NamespacedKey(namespace, id));
        }
    }

    @EventHandler
    void joinEvent(PlayerJoinEvent e) {
        giveRecipes(e.getPlayer());
    }

    @EventHandler
    void quitEvent(PlayerQuitEvent e) {
        removeRecipes(e.getPlayer());
    }
}
