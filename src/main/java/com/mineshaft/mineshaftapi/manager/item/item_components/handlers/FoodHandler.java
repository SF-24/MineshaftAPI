/*
 * Copyright (c) 2026. Sebastian Frynas
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

package com.mineshaft.mineshaftapi.manager.item.item_components.handlers;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;

public class FoodHandler extends ComponentHandler {
    public FoodHandler(ConfigurationSection yamlConfiguration) {
        super(yamlConfiguration);
    }

    @Override
    public void handlePropertyPreMeta(ItemMeta itemMeta) {
        FoodComponent component = new ItemStack(Material.APPLE).getItemMeta().getFood();

        String path = "food.";

        for (String field : yamlConfiguration.getConfigurationSection("food").getKeys(false)) {
            switch (field) {
                case "saturation":
                    component.setSaturation((float) yamlConfiguration.getDouble(path + "saturation"));
                case "nutrition":
                    component.setNutrition(yamlConfiguration.getInt(path + "nutrition"));
                case "always_edible":
                    component.setCanAlwaysEat(yamlConfiguration.getBoolean(path + "always_edible"));
            }
        }
        itemMeta.setFood(component);
    }

    @Override
    public void handlePropertyPostMeta(ItemStack itemStack) {

    }
}
