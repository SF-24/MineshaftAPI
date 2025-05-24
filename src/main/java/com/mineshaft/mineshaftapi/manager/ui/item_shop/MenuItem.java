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

package com.mineshaft.mineshaftapi.manager.ui.item_shop;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.inventory.ItemStack;

public class MenuItem {

    protected ItemType itemType;
    protected ItemStack displayItem;
    protected String sellItemName;
    protected int price;

    public MenuItem(ItemType itemType, String sellItemName) {
        this.itemType = itemType;
        this.sellItemName=sellItemName;
        try {
            this.displayItem = MineshaftApi.getInstance().getItemManagerInstance().getItem(sellItemName);
        } catch (NullPointerException e) {
            Logger.logError("Specified custom item name in MenuItem class does not exist");
        }
    }

    public void setPrice(int value) {
        if(value<0) value=0;

        this.price=value;
    }

    public int getPriceIfCanBuy() {
        if(itemType.equals(ItemType.PURCHASE)) {
            return price;
        }
        return -1;
    }

    public String getItemId() {
        return sellItemName;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
