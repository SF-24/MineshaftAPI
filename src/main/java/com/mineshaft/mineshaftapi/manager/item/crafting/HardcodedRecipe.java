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

import org.bukkit.inventory.recipe.CraftingBookCategory;

public enum HardcodedRecipe {

    SHORTSWORD(CraftingBookCategory.EQUIPMENT),
    SWORD(CraftingBookCategory.EQUIPMENT),
    LONGSWORD(CraftingBookCategory.EQUIPMENT),
    PICKAXE(CraftingBookCategory.EQUIPMENT),
    AXE(CraftingBookCategory.EQUIPMENT),
    SHOVEL(CraftingBookCategory.EQUIPMENT),
    HOE(CraftingBookCategory.EQUIPMENT),
    HELMET_SIMPLE(CraftingBookCategory.EQUIPMENT),
    CHESTPLATE_SIMPLE(CraftingBookCategory.EQUIPMENT),
    LEGGINGS_SIMPLE(CraftingBookCategory.EQUIPMENT),
    BOOTS_SIMPLE(CraftingBookCategory.EQUIPMENT),
    ;
    private final CraftingBookCategory category;

    HardcodedRecipe(CraftingBookCategory category) {
        this.category=category;
    }

    public org.bukkit.inventory.recipe.CraftingBookCategory getCategory() {return category;}
}
