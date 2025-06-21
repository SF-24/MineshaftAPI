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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ButtonUtil {

    //
    // Button Declarations for consistent use between plugins
    //

    public static Material getButtonMaterial(ButtonType temporarilyIgnored) {
        return Material.PEONY;
    }

    public static int getButtonCustomModelData(ButtonType type, ButtonVariant variant) {
        switch (type) {
            case INFO -> {
                return 10;
            }
            case TICK -> {
                switch (variant) {
                    case DEFAULT, GREEN -> {
                        return 23;
                    }
                    case RED -> {
                        return 21;
                    }
                    case YELLOW -> {
                        return 25;
                    }
                    case GREY -> {
                        return 24;
                    }
                }
            }
            case ADD -> {
                switch (variant) {
                    case DEFAULT, GREEN -> {
                        return 26;
                    }
                    case RED -> {
                        return 27;
                    }
                    case YELLOW -> {
                        return 28;
                    }
                    case GREY -> {
                        return 29;
                    }
                }
            }
            case QUESTION_MARK -> {
                return 54;
            }
            case REFRESH -> {
                return 25;
            }
            case ARROW_LEFT -> {
                switch (variant) {
                    case DEFAULT -> {
                        return 55;
                    }
                    case RED -> {
                        return 56;
                    }
                }
            }
            case ARROW_RIGHT -> {
                switch (variant) {
                    case DEFAULT -> {
                        return 57;
                    }
                    case RED -> {
                        return 58;
                    }
                }
            }
            case ARROW_UP -> {
                switch (variant) {
                    case DEFAULT -> {
                        return 52;
                    }
                    case RED -> {
                        return 53;
                    }
                }
            }
            case ARROW_DOWN -> {
                switch (variant) {
                    case DEFAULT -> {
                        return 50;
                    }
                    case RED -> {
                        return 51;
                    }
                }
            }
            default -> {return 0;}
        }
        return 0;
    }

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
        ItemStack button = new ItemStack(ButtonUtil.getButtonMaterial(ButtonType.ARROW_UP));
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(TextFormatter.capitaliseString(direction.name().toLowerCase()));
        meta.setCustomModelData(ButtonUtil.getButtonCustomModelData(ButtonUtil.getButtonFromDirection(direction), variant));
        button.setItemMeta(meta);
        return button;
    }

    public static ItemStack getButton(ButtonType type, ButtonVariant variant, String name, ArrayList<String> lore, String onClick) {
        ItemStack button = new ItemStack(getButtonMaterial(type));
        ItemMeta meta = button.getItemMeta();
        if(name!=null) meta.setDisplayName(ChatColor.WHITE + name);
        if(lore!=null) meta.setLore(lore);
        meta.setCustomModelData(getButtonCustomModelData(type,variant));
        button.setItemMeta(meta);
        if(onClick!=null&&!onClick.equalsIgnoreCase("")) {
            UIUtil.setOnclick(button,onClick);
        }
        return button;
    }

}
