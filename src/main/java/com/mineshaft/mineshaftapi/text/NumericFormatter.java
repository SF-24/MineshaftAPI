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

package com.mineshaft.mineshaftapi.text;

import java.text.DecimalFormat;

public class NumericFormatter {
    public static DecimalFormat getDecimalFormat() {
        return new DecimalFormat("#,###.00");
    }

    public static DecimalFormat getIntegerFormat() {
        return new DecimalFormat("#,###");
    }

    public static String formatNumber(double number) {
        return getDecimalFormat().format(number);
    }

    public static String formatNumberAdvanced(double number) {

        double updatedNumber = number - (int) number;

        if(updatedNumber==0) {
            return formatInteger((int) number);
        }
        return formatDecimal(number);
    }

    public static String formatDecimal(double decimal) {
        return getDecimalFormat().format(decimal);
    }

    public static String formatInteger(int integer) {
        return getIntegerFormat().format(integer);
    }

}
