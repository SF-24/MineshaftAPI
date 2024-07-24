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

package com.mineshaft.mineshaftapi.command;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.text.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Logger.logError("Only a player may execute this command");
            return false;
        }

        Player player = (Player) sender;

        if(args.length==0) {
            player.sendMessage(ChatColor.RED + "Please specify an item name");
            sendItemList(player);
            return false;
        }

        if(args.length>1) {

            player.sendMessage(ChatColor.RED + "Too many parameters");
            return false;
        }

        String itemName = args[0];
        ItemStack item = MineshaftApi.getInstance().getItemManagerInstance().getItem(itemName);

        if(item==null) {player.sendMessage(ChatColor.RED + "Selected item does not exist!"); sendItemList(player); return false;}

        player.getInventory().addItem(item);

        return false;
    }

    protected void sendItemList(Player player) {
        player.sendMessage(ChatColor.GOLD + "Showing item list:");
        for(String name : MineshaftApi.getInstance().getItemManagerInstance().getItemList().values()) {
            player.sendMessage(" " + ChatColor.BLUE + name);
        }
    }
}
