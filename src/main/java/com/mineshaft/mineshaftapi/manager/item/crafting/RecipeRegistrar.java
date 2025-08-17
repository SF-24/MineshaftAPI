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
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerManager;
import com.mineshaft.mineshaftapi.util.Logger;
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

    public HashMap<String, Boolean> recipeCache = new HashMap<>();
    public RecipeHandler recipeHandler;

    // By default, recipes are not locked
    public RecipeRegistrar(String namespace) {
        init(namespace);
    }

    private void init(String namespace) {
        if(namespace != null && !namespace.isEmpty()) {
            this.namespace = namespace;
        } else {
            this.namespace = "mineshaftdefault";
        }
        Bukkit.getPluginManager().registerEvents(this, MineshaftApi.getInstance());
        recipeHandler = new RecipeHandler(namespace);
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

    public void registerCraftingRecipeSimple(ItemStack item, List<Material> elements, CraftingBookCategory category, boolean isShaped, int width, int height) {
        this.registerCraftingRecipeSimple(item, elements, category, isShaped, width, height, UUID.randomUUID().toString());
    }

    // register a recipe
    public void registerCraftingRecipeSimple(ItemStack item, List<Material> elements, CraftingBookCategory category, boolean isShaped, int width, int height, String key) {
        Recipe recipe = recipeHandler.getCraftingRecipeSimple(item,elements,category,isShaped,width,height,key);
        cacheRecipe(key);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void registerCraftingRecipeComplex(ItemStack item, List<ItemStack> elements, CraftingBookCategory category, boolean isShaped, int width, int height) {
        this.registerCraftingRecipeComplex(item, elements, category, isShaped, width, height, UUID.randomUUID().toString());
    }

        // register a recipe
    public void registerCraftingRecipeComplex(ItemStack item, List<ItemStack> elements, CraftingBookCategory category, boolean isShaped, int width, int height, String key) {
        Recipe recipe = recipeHandler.getCraftingRecipeComplex(item,elements,category,isShaped,width,height,key);
        cacheRecipe(key);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void registerFurnaceRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        registerFurnaceRecipe(item, output, category, xp, timeInTicks, UUID.randomUUID().toString());
    }

    public void registerFurnaceRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks, String id) {
        cacheRecipe(id);
        FurnaceRecipe recipe = recipeHandler.getFurnaceRecipe(item,output,category,xp,timeInTicks,id);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void registerFurnaceRecipeSimpleFood(ItemStack item, ItemStack output) {
        registerFurnaceRecipeSimple(item,output,CookingBookCategory.FOOD);
    }

    public void registerFurnaceRecipeSimple(ItemStack item, ItemStack output, CookingBookCategory category) {
        registerFurnaceRecipe(item,output,category,0,200);
    }

    public void registerBlastFurnaceRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        registerBlastFurnaceRecipe(item,output,category,xp,timeInTicks,UUID.randomUUID().toString());
    }

    public void registerBlastFurnaceRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks, String id) {
        BlastingRecipe recipe = recipeHandler.getBlastFurnaceRecipe(item,output,category,xp,timeInTicks,id);
        cacheRecipe(id);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void registerSmokerRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        registerSmokerRecipe(item,output,category,xp,timeInTicks,UUID.randomUUID().toString());
    }

    public void registerSmokerRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks, String id) {
        SmokingRecipe recipe = recipeHandler.getSmokerRecipe(item, output, category, xp, timeInTicks, id);
        cacheRecipe(id);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void registerCampfireRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks) {
        registerCampfireRecipe(item, output, category, xp, timeInTicks, UUID.randomUUID().toString());
    }

    public void registerCampfireRecipe(ItemStack item, ItemStack output, CookingBookCategory category, int xp, int timeInTicks, String id) {
        CampfireRecipe recipe = recipeHandler.getCampfireRecipe(item, output, category, xp, timeInTicks, id);
        cacheRecipe(id);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void registerShapelessRecipe(ItemStack item, List<ItemStack> components, CraftingBookCategory category) {
        registerCraftingRecipeComplex(item,components,category,false,-1,-1);
    }

    public void registerShapelessRecipeMisc(ItemStack item, List<ItemStack> components) {
        registerCraftingRecipeComplex(item,components,CraftingBookCategory.MISC,false,-1,-1);
    }

    public void cacheRecipe(String id) {
        // By default, given on join
        recipeCache.put(id, true);
    }

    public void cacheRecipe(String id, boolean locked) {
        // Param is equal to whether the recipe is given on join.
        recipeCache.put(id, !locked);
        if (locked) {
            MineshaftApi.getGlobalRecipeCache().cacheLockedRecipe(new RecipeKey(namespace, id));
        }
    }

    public void clearRecipes() {
        for(String id : recipeCache.keySet()) {
            Bukkit.getServer().removeRecipe(new NamespacedKey(namespace, id));
        }
    }

    public void giveRecipes(Player player) {
        for(String id : recipeCache.keySet()) {
            if(recipeCache.get(id) || JsonPlayerBridge.getDiscoveredRecipes(player).contains(new RecipeKey(namespace, id))) {
                player.discoverRecipe(new NamespacedKey(namespace, id));
                Logger.logInfo("Giving recipe " + namespace + " | " + id + " to player " + player.getName());
            }
        }
    }

    public void removeRecipes(Player player) {
        for(String id : recipeCache.keySet()) {
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
