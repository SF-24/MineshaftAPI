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

import org.apache.commons.lang.WordUtils;

public class TextFormatter {

    public static String capitaliseString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String capitaliseStringFully(String input) {
        if (input == null || input.isEmpty())
        {
            return input;
        }

        // split the input string into words using whitespace
        String[] words = input.split("\\s");

        // create a StringBuilder
        StringBuilder result = new StringBuilder();

        for (String word : words)
        {
            if(word.equalsIgnoreCase("and")||word.equalsIgnoreCase("or")|word.equalsIgnoreCase("of")|word.equalsIgnoreCase("in")||word.equalsIgnoreCase("the")) {
                result.append(word).append(" ");
                continue;
            }
            // capitalize the first letter of each word and append the rest of the word
            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" "); // Add a space between words
        }

        // remove the trailing space and return the capitalized string
        return result.toString().trim();
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
