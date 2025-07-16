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

import com.mineshaft.mineshaftapi.MineshaftApi;

public class RNGUtil {

    public static int randIntInRange(int min, int max) {
        if(min > max) {
            int tempMax = min;
            min = max;
            max = tempMax;
        }
        return (int) (Math.random() * (max - min) + min);
    }

    public static class DiceUtil {
        public static int rollDice(int sides) {
            return MineshaftApi.getRandom().nextInt(sides) + 1;
        }

        public static int rollDice(int sides, int diceCount) {
            int sum = 0;
            for(int i = 0; i < diceCount; i++) {
                sum+=rollDice(sides);
            }
            return sum;
        }
    }
}
