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

import org.apache.commons.lang.WordUtils;

public class TextFormatter {

    public static String capitaliseString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String capitaliseStringFully(String string) {
        return WordUtils.capitalizeFully(string);
    }

    public static String addSpacesToString(String string) {
        string = string.replace("-"," ");
        string = string.replace("_"," ");
        return string;
    }

    public static String convertStringToName(String string) {
        return capitaliseStringFully(addSpacesToString(string));
    }
}
