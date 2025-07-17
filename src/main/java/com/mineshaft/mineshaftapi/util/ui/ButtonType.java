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

import org.bukkit.Material;

public enum ButtonType {

    INFO,
    QUESTION_MARK,
    REFRESH,
    PLUS,
    TICK,

    ARROW_LEFT,
    ARROW_RIGHT,
    ARROW_FORWARDS,
    ARROW_BACK,
    ARROW_UP,
    ARROW_DOWN,

    ;

    public int getCustomModelData() {
        return getCustomModelData(ButtonVariant.DEFAULT);
    }

    public int getCustomModelData(ButtonVariant variant) {
        switch (this) {
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
            case PLUS -> {
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
                    case RED -> {
                        return 56;
                    }
                    default -> {
                        return 55;
                    }
                }
            }
            case ARROW_RIGHT -> {
                switch (variant) {
                    case RED -> {
                        return 58;
                    }
                    default -> {
                        return 57;
                    }
                }
            }
            case ARROW_UP -> {
                switch (variant) {
                    case RED -> {
                        return 53;
                    }
                    default -> {
                        return 52;
                    }
                }
            }
            case ARROW_DOWN -> {
                switch (variant) {
                    case RED -> {
                        return 51;
                    }
                    default -> {
                        return 50;
                    }
                }
            }
            default -> {return 0;}
        }
        return 0;
    }

    public Material getMaterial() {
        return Material.PEONY;
    }

}
