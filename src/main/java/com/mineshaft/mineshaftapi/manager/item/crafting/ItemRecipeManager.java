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
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class ItemRecipeManager {

    public static HardcodedRecipe getHardcodedCraftingRecipe(String itemName) {
        // Init. file:
        File fileYaml = new File(MineshaftApi.getInstance().getItemPath(), itemName + ".yml");
        // return null if file does not exist
        if (!fileYaml.exists()) return null;
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        // Declare repair item map
        HardcodedRecipe craftingRecipe = null;


        if(yamlConfiguration.contains("hardcoded-recipe")) {
            if(yamlConfiguration.getString("hardcoded-recipe")==null) return null;
            try {
                // Get the crafting recipe.
                craftingRecipe= HardcodedRecipe.valueOf((yamlConfiguration.getString("hardcoded-recipe")).toUpperCase());
            } catch (IllegalArgumentException e) {
                // If illegal argument, return null
                return null;
            }
        }

        // Get parent repair items
        else if (yamlConfiguration.contains("parent")) {
            String parentName = yamlConfiguration.getString("parent");
            if (parentName != null && !parentName.equalsIgnoreCase("null") && !parentName.equalsIgnoreCase("nil")) {
                craftingRecipe=getHardcodedCraftingRecipe(parentName);
            }
        }
        return craftingRecipe;

    }

    public static HashMap<String, Material> getCraftingMaterials(String itemName) {
        // Init. file:
        File fileYaml = new File(MineshaftApi.getInstance().getItemPath(), itemName + ".yml");
        // return null if file does not exist
        if (!fileYaml.exists()) return null;
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        // Declare repair item map
        HashMap<String, Material> craftItems = new HashMap<>();

        // Get parent repair items
        if (yamlConfiguration.contains("parent")) {
            String parentName = yamlConfiguration.getString("parent");
            if (parentName != null && !parentName.equalsIgnoreCase("null") && !parentName.equalsIgnoreCase("nil")) {
                craftItems.putAll(getCraftingMaterials(parentName));
            }
        }
        if(yamlConfiguration.contains("craft-materials")) {
            for(String key : yamlConfiguration.getConfigurationSection("craft-materials").getKeys(false)) {
                craftItems.put(key, Material.valueOf(yamlConfiguration.getString("craft-materials." + key).toUpperCase()));
            }
        }
        return craftItems;
    }

    // Register item crafting recipes
    public static void registerRecipe(String itemName) {
        // register item recipes
        if(getCraftingMaterials(itemName)==null || getHardcodedCraftingRecipe(itemName)==null)return;

        // Correct up to here.

        if(!getCraftingMaterials(itemName).containsKey("c1")||getCraftingMaterials(itemName).containsKey("c2")) {return;}
        RecipeRegistrar.addHardcodedRecipe(MineshaftApi.getInstance().getItemManagerInstance().getItem(itemName),getHardcodedCraftingRecipe(itemName),getCraftingMaterials(itemName).get("c1"),getCraftingMaterials(itemName).get("c2"));
    }


}
