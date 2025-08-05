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

import lombok.Getter;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;

@Getter
public class GlobalRecipeCache {

    ArrayList<RecipeKey> lockedRecipes = new ArrayList<>();

    public void cacheLockedRecipe(RecipeKey key) {
        lockedRecipes.add(key);
    }

    public void clearLockedRecipes() {
        lockedRecipes.clear();
    }

    public void clearNameSpace(String namespace) {
        lockedRecipes.removeIf(recipeKey -> recipeKey.getNamespace().equals(namespace));
    }

    public boolean isLocked(NamespacedKey key) {
        return lockedRecipes.contains(new RecipeKey(key));
    }

    public boolean isLocked(RecipeKey key) {
        return lockedRecipes.contains(key);
    }
}
