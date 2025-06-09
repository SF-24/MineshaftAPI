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

package com.mineshaft.mineshaftapi.manager.player.json;

import com.mineshaft.mineshaftapi.dependency.world_guard.Town;

import java.util.ArrayList;

public class PlayerDiscoveryClass {

    ArrayList<Town> townDiscoveries = new ArrayList<>();

    public void addDiscoveredTown(Town town) {
        townDiscoveries.add(town);
    }

    public boolean hasDiscoveredTown(Town town) {
        return townDiscoveries.contains(town);
    }

    public ArrayList<Town> getDiscoveredTowns() {
        return townDiscoveries;
    }
}
