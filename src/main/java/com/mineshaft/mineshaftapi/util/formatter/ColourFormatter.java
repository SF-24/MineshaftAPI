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

package com.mineshaft.mineshaftapi.util.formatter;

import org.bukkit.Color;

public class ColourFormatter {

    public static Color getColourFromString(String colour) {
        if(!(colour.length() == 9||colour.length() == 12)) {
            return Color.fromRGB(0,0,0);
        }
        int r= Integer.parseInt(colour.substring(0,3));
        int g = Integer.parseInt(colour.substring(3,6));
        int b = Integer.parseInt(colour.substring(6,9));
        if(colour.length() == 12) {
            int a = Integer.parseInt(colour.substring(9,12));
            return Color.fromARGB(a,r,g,b);
        }
        return Color.fromRGB(r,g,b);
    }

}
