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

package com.mineshaft.mineshaftapi.manager.block;

import org.bukkit.Material;

public class BlockManager {

    public static boolean isInteractable(Material material) {
        return material.name().contains("DOOR") ||
                (material.name()).contains("GATE") ||
                (material.name()).contains("chest") ||
                (material.name()).contains("barrel") ||
                (material.name()).contains("smoker") ||
                (material.name()).contains("furnace") ||
                (material.name()).contains("table") ||
                (material.name()).contains("dropper") ||
                (material.name()).contains("dispenser") ||
                (material.name()).contains("sign") ||
                (material.name()).contains("bed") ||
                (material.name()).contains("bell") ||
                (material.name()).contains("brewing_stand") ||
                (material.name()).contains("button") ||
                (material.name()).contains("lever") ||
                (material.name()).contains("cauldron") ||
                (material.name()).contains("anchor") ||
                (material.name()).contains("lectern") ||
                (material.name()).contains("loom") ||
                (material.name()).contains("note_block") ||
                (material.name()).contains("anvil") ||
                (material.name()).contains("grindstone") ||
                (material.name()).contains("stonecutter");
    }

}
