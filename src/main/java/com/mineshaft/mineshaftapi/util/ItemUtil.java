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

package com.mineshaft.mineshaftapi.util;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.player.ActionType;
import de.tr7zw.changeme.nbtapi.NBT;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import net.kyori.adventure.key.Key;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemUtil {

    public static FoodComponent getFoodComponent(int nutrition, float saturation, boolean alwaysEdible) {
        FoodComponent component = new ItemStack(Material.APPLE).getItemMeta().getFood();
        component.setNutrition(nutrition);
        component.setSaturation(saturation);
        component.setCanAlwaysEat(alwaysEdible);
        return component;
    }

    public static Consumable getConsumable(float consumeTime, String sound, ItemUseAnimation animation, List<ConsumeEffect> effects, boolean particles) {
        return Consumable.consumable().sound(Key.key(sound)).hasConsumeParticles(particles).consumeSeconds(consumeTime).addEffects(effects).animation(animation).build();
    }

    public static void addFoodComponent(ItemStack item, FoodComponent component) {
        ItemMeta meta = item.getItemMeta();
        meta.setFood(component);
        item.setItemMeta(meta);
    }

    public static void setConsumableComponent(ItemStack item, Consumable component) {
        item.setData(DataComponentTypes.CONSUMABLE, component);
    }

    public static void setDefaultData(ItemStack item, String name, int customModelData) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
    }

    public static void setDefaultFood(ItemStack item, int nutrition, float saturation, float eatTimeSeconds, boolean isDrink, boolean drinkSounds) {
        ItemMeta meta = item.getItemMeta();
        meta.setFood(getFoodComponent(nutrition, saturation, false));
        item.setItemMeta(meta);
        String eatSound = "entity.generic.eat";
        if(drinkSounds) {
            eatSound="entity.generic.drink";
        }
        if(!isDrink) {
            setConsumableComponent(item, getConsumable(eatTimeSeconds, eatSound,ItemUseAnimation.EAT,List.of(),true));
        } else {
            setConsumableComponent(item, getConsumable(eatTimeSeconds, eatSound,ItemUseAnimation.DRINK,List.of(),false));

        }
    }

    public static boolean isWand(ItemStack item) {
        final UUID[] uuid = new UUID[1];
        AtomicBoolean isWand = new AtomicBoolean(false);

        try {
            NBT.get(item, nbt -> {
                if(nbt.getBoolean("isWand")) {
                    isWand.set(true);
                }

                String id = nbt.getString("uuid");
                if (id.equalsIgnoreCase("null")) return;
                uuid[0] = UUID.fromString(id);
            });
        } catch (Exception ignored) {
        }

        if(isWand.get()) {return true;}

        UUID uniqueId = uuid[0];
        if(uniqueId==null || !MineshaftApi.getInstance().getItemManagerInstance().isValidUUID(uniqueId)) {
            return false;
        }
        if(ItemManager.getInteractEventsFromItem(ItemManager.getItemName(uniqueId), ActionType.RIGHT_CLICK)==null) {
            return false;
        }

        return Objects.requireNonNull(ItemManager.getInteractEventsFromItem(ItemManager.getItemName(uniqueId), ActionType.RIGHT_CLICK)).contains("wand");
    }

}
