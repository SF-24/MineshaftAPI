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

package com.mineshaft.mineshaftapi.manager.item_shop;

import java.util.HashMap;

public class ItemMenu {

    protected String title = "Unnamed UI";
    protected int size = 27;
    protected HashMap<Integer, MenuItem> items;

    public ItemMenu(int size, String title, HashMap<Integer, MenuItem> items) {
        this.size = size;
        this.title = title;
        this.items = items;
    }

    public Integer getSize() {return size;}
    public String getTitle() {return title;}
    public HashMap<Integer, MenuItem> getItems() {return items;}
}
