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

package com.mineshaft.mineshaftapi.manager.item;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.util.Logger;
import jdk.jfr.Description;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemManagerAccessUtility {

    @Description("Page numbers starting with 1." +
            "To avoid pagination set pageSize to 0 or -1. " +
            "Set page size to -2 to use default value (45)")
    public static ArrayList<ItemStack> getItemsEnMasse(String folder, int page, int pageSize) {
        if(page>getItemsEnMasseFull(folder,pageSize).size()) {
            Logger.logError("Attempted to access non-existent page. Error in:\n" +
                    "GetItemCommand:104");
            return null;
        }

        return getItemsEnMasseFull(folder,pageSize).get(page-1);
    }

    public static ArrayList<ArrayList<ItemStack>> getItemsEnMasseFull(String folder, int pageSize) {
        if(pageSize==-2) pageSize=45;

        ArrayList<ArrayList<ItemStack>> itemList = new ArrayList<>();

        ArrayList<ItemStack> pageList = new ArrayList<>();
        for(String name : MineshaftApi.getInstance().getItemManagerInstance().getItemList().values()) {
            pageList.add(MineshaftApi.getInstance().getItemManagerInstance().getItem(name));
            if(pageSize>0 && pageList.size()>=pageSize) {
                itemList.add(pageList);
                pageList.clear();
            }
        }
        return itemList;
    }

}
