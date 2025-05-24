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

package com.mineshaft.mineshaftapi.manager.player;

import com.mineshaft.mineshaftapi.manager.item.ArmourType;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArmourEquipManager {

    public static boolean tryEquip(Player player, ItemStack itemStack) {
        final boolean[] returnValue = {false};
        if(JsonPlayerBridge.hasAttribute(player, "str") && !itemStack.getType().equals(Material.AIR) && itemStack.getItemMeta()!=null) {
            try {
                NBT.get(itemStack, nbt->{
                    if(nbt!=null && nbt.hasNBTData()) {
                        String type = (nbt.getString("ArmourType"));
                        //player.sendMessage("Type: " + type);
                        if(type!=null) {
                            ArmourType armourType = ArmourType.valueOf(type.toUpperCase());
                            if(JsonPlayerBridge.getAttribute(player, "str")<armourType.getStrRequirement()) {
                                player.sendMessage(ChatColor.WHITE + "You must have at least "+ChatColor.RED + "STRENGTH " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + armourType.getStrRequirement() + ChatColor.WHITE + " to equip this armour.");
                                returnValue[0] = true;
                            }
                        }
                    }
                });
            } catch (NullPointerException ignored) {}
        }
        return returnValue[0];
    }

}
