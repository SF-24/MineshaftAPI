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

package com.mineshaft.mineshaftapi.command;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item.ItemManagerAccessUtility;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Logger.logError("Only a player may execute this command");
            return false;
        }

        Player player = (Player) sender;

        if(args.length==0) {
            sendItemList(player);
        } else if(args.length==1) {

            String item = args[0];
            try {
                player.getInventory().addItem(MineshaftApi.getInstance().getItemManagerInstance().getItem(item));
            } catch(NullPointerException e) {
                player.sendMessage(ChatColor.RED + "Invalid item");
                sendItemList(player);
            }
            return false;
        } else if(args.length <= 3) {

            if(args[0].equalsIgnoreCase("gui")) {
                String folder = args[1];

                if(args.length>2) {
                    try{
                        int page = Integer.parseInt(args[2]);
                        if(page<1) {
                            page=1;
                            player.sendMessage(ChatColor.RED+"number cannot be less than 1");
                        }
                        ItemManagerAccessUtility.sendItemListUi(player,folder,page);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED+ "Incorrect number format. Use:");
                        sendSyntax(player);
                    }
                } else {
                    ItemManagerAccessUtility.sendItemListUi(player,folder,1);
                }
            } else {
                player.sendMessage(ChatColor.RED + "Incorrect command syntax. Use:");
                sendSyntax(player);
            }
        } else {
            player.sendMessage(ChatColor.RED + "Too many parameters");
            return false;
        }



        return false;
    }

    protected void sendItemList(Player player) {
        player.sendMessage(ChatColor.GOLD + "Showing item list:");
        for(String name : MineshaftApi.getInstance().getItemManagerInstance().getItemList().values()) {
            player.sendMessage(" " + ChatColor.BLUE + name);
        }
    }



    public static void sendSyntax(Player player) {
        player.sendMessage(ChatColor.RED + "/getitem <item>");
        player.sendMessage(ChatColor.RED + "/getitem gui <folder> [page]");
        player.sendMessage(ChatColor.RED + "/getitem gui null");
    }
}
