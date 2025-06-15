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

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UIUtil {

    public static ItemStack setOnclick(ItemStack item, String onClick) {
        NBT.modify(item, nbt->{
            nbt.setString("onClick", onClick);
        });
        return item;
    }
    
    public static String getOnclick(ItemStack item) {
        final String[] returnValue = {null};
        NBT.get(item, nbt->{
            Logger.logInfo("On click: " + nbt.getString("onClick"));
            returnValue[0] = nbt.getString("onClick");
        });
        return returnValue[0];
    }

    // Check if a page in the GUI is valid
    public static boolean isPageValid(List<ItemStack> items, int page, int slotsPerPage) {
        if(page<=0) return false;

        // Top value of the page
        int upperBound = page * slotsPerPage;
        // Lowest value of the page
        int lowerBound = upperBound - slotsPerPage;

        // Whether the next page is valid
        return items.size()>lowerBound;
    }

    // Sort the item stack list in a GUI
    public static ArrayList<ItemStack> getPageItem(List<ItemStack> item, int page, int slotsPerPage) {
        // Top value of the page
        int upperBound = page * slotsPerPage;
        // Lowest value of the page
        int lowerBound = upperBound - slotsPerPage;

        ArrayList<ItemStack> newItems = new ArrayList<>();
        try {
            for(int i=lowerBound; i<upperBound; i++) {
                newItems.add(item.get(i));
            }
        } catch (IndexOutOfBoundsException ignored) {}
        return newItems;
    }

}
