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

package com.mineshaft.mineshaftapi.dependency.world_guard;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Town {

    String parentRegion;
    String id;
    int size = 1;
    @NotNull List<String> regions;

    public Town(String id, String parentRegion, @NotNull List<String> regions) {
        this.id =id;
        this.parentRegion=parentRegion;
        this.regions=regions;
    }

    public Town(String id, String parentRegion, @NotNull List<String> regions, int size) {
        this.id =id;
        this.parentRegion=parentRegion;
        this.regions=regions;
        this.size=size;
    }

    public int getSize() {return size;}
    public String getId() {return id;}
    public String getName() {return id.replace("_","").replace("-","");}
    public @NotNull List<String> getRegions() {return regions;}
    public String getParentRegion() {return parentRegion;}
}
