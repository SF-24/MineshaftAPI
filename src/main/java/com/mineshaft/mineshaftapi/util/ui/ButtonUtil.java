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

package com.mineshaft.mineshaftapi.util.ui;

import com.mineshaft.mineshaftapi.util.UIUtil;
import com.mineshaft.mineshaftapi.util.formatter.TextFormatter;
import com.mineshaft.mineshaftapi.util.maths.Direction2D;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ButtonUtil {

    //
    // Button Declarations for consistent use between plugins
    //

    public static ButtonType getButtonFromDirection(Direction2D direction) {
        switch (direction) {
            case UP -> {
                return ButtonType.ARROW_UP;
            }
            case DOWN -> {
                return ButtonType.ARROW_DOWN;
            }
            case LEFT -> {
                return ButtonType.ARROW_LEFT;
            }
            case RIGHT -> {
                return ButtonType.ARROW_RIGHT;
            }
        }
        return ButtonType.ARROW_UP;
    }

    /**
     @param direction Where the arrow icon will be facing
     @param variant The colour of the arrow to be displayed. 0=green, 1=red
     */
    public static ItemStack getArrowDirectionButton(Direction2D direction, ButtonVariant variant) {
        ItemStack button = new ItemStack(ButtonType.ARROW_UP.getMaterial());
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(TextFormatter.capitaliseString(direction.name().toLowerCase()));
        meta.setCustomModelData(ButtonUtil.getButtonFromDirection(direction).getCustomModelData(variant));
        button.setItemMeta(meta);
        return button;
    }

    public static ItemStack getButton(ButtonType type, ButtonVariant variant, String name, ArrayList<String> lore, String onClick) {
        ItemStack button = new ItemStack(type.getMaterial());
        ItemMeta meta = button.getItemMeta();
        if(name!=null) meta.setDisplayName(ChatColor.WHITE + name);
        if(lore!=null) meta.setLore(lore);
        meta.setCustomModelData(type.getCustomModelData(variant));
        button.setItemMeta(meta);
        if(onClick!=null&&!onClick.equalsIgnoreCase("")) {
            UIUtil.setOnclick(button,onClick);
        }
        return button;
    }

    public static ItemStack getButton(ButtonType type, ButtonVariant variant, String name, List<String> lore, String onClick) {
        ItemStack button = getButton(type, variant, name, new ArrayList<>(), onClick);
        ItemMeta itemMeta = button.getItemMeta();
        itemMeta.setLore(lore);
        button.setItemMeta(itemMeta);
        return button;
    }

}
