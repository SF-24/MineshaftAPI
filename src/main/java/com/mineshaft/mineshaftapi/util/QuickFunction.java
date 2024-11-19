/*
 *     This program is a Minecraft plugin developed in Java for the Spigot API.
 *     It adds multiple RPG features intended for Multiplayer gameplay.
 *
 *     Copyright (C) 2024  Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.mineshaft.mineshaftapi.util;

import org.bukkit.inventory.meta.ItemMeta;

public class QuickFunction {

    /**
    * For running functions which otherwise result errors due to deprecation
     * and being marked for removal
    */

    @SuppressWarnings("all")
    public static boolean hasLocalisedName(ItemMeta meta) {
        return meta.hasLocalizedName();
    }

    @SuppressWarnings("all")
    public static ItemMeta setLocalisedName(ItemMeta meta,String name) {
        meta.setLocalizedName(name);
        return meta;
    }

    @SuppressWarnings("all")
    public static String getLocalisedMane(ItemMeta meta) {
        return meta.getLocalizedName();
    }
}
