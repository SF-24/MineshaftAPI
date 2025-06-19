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

package com.mineshaft.mineshaftapi.manager.event.click;

import org.bukkit.event.block.Action;

import java.util.ArrayList;
import java.util.Arrays;

public enum ClickType {

    LEFT(new ArrayList<>(Arrays.asList(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK)),'L'),
    RIGHT(new ArrayList<>(Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)),'R');

    final ArrayList<org.bukkit.event.block.Action> clickTypes;
    final char abbreviation;

    ClickType(ArrayList<org.bukkit.event.block.Action> clickTypes, char abbreviation) {
        this.abbreviation=abbreviation;
        this.clickTypes=clickTypes;
    }

    public ArrayList<Action> getClickTypes() {return clickTypes;}
    public char getAbbreviation() {return abbreviation;}
}
