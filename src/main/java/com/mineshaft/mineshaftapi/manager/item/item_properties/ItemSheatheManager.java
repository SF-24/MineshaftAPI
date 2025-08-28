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

package com.mineshaft.mineshaftapi.manager.item.item_properties;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemSheatheManager {

    public static void unSheatheWeapon(ItemStack itemStack) {
        NBT.get(itemStack, nbt->{
            if(!(nbt.hasTag("is_sheathed"))) {
                return;
            }
        });

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(getOpenCustomModelData(itemStack));
        itemStack.setItemMeta(itemMeta);

        NBT.modify(itemStack, nbt->{
            nbt.setBoolean("is_sheathed",false);
        });
    }

    public static void sheatheWeapon(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(getSheathedCustomModelData(itemStack));
        itemStack.setItemMeta(itemMeta);

        NBT.modify(itemStack, nbt->{
            nbt.setBoolean("is_sheathed",true);
        });
    }

    public static int getOpenCustomModelData(ItemStack item) {
        NBT.get(item, nbt->{
            return nbt.getInteger("open_custom_model_data");
        });
        return 0;
    }

    public static int getSheathedCustomModelData(ItemStack item) {
        NBT.get(item, nbt->{
            return nbt.getInteger("custom_model_data");
        });
        return 0;
    }

}
