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

package com.mineshaft.mineshaftapi.manager.item.fields;

public enum ItemSubcategoryProperty {

    LIGHT("Light", 1),
    HEAVY("Heavy", 2),
    FINESSE("Finesse", 5),
    ;
    final String name;
    private final int priority;

    ItemSubcategoryProperty(String name, int priority) {
        this.name=name;
        this.priority=priority;
    }

    public String getName() {return name;}
    public int getPriority() {return priority;}
    public int getMaxPriority() {
        int maxPriority = 0;
        for(ItemSubcategoryProperty property : ItemSubcategoryProperty.values()) {
            if(property.getPriority() > maxPriority) {
                maxPriority=property.getPriority();
            }
        }
        return maxPriority;
    }
}
