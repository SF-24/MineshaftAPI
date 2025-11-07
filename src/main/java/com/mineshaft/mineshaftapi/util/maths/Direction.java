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

package com.mineshaft.mineshaftapi.util.maths;

import org.bukkit.util.Vector;

public enum Direction {

    UP, DOWN, LEFT, RIGHT, FORWARDS, BACK
;
    public Vector getUnitVector() {
        switch (this) {
            case UP -> {
                return new Vector(0,1,0);
            }
            case DOWN -> {
                return new Vector(0,-1,0);
            }
            case LEFT -> {
                return new Vector(1,0,0);
            }
            case RIGHT -> {
                return new Vector(-1,0,0);
            }
            case FORWARDS -> {
                return new Vector(0,0,1);
            }
            case BACK -> {
                return new Vector(0,0,-1);
            }
        }
        return new Vector(0,0,0);
    }
}
