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
import com.mineshaft.mineshaftapi.manager.BlockUI;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.ItemManagerAccessUtility;
import com.mineshaft.mineshaftapi.util.DirUtil;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class GetItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player player)) {
            Logger.logError("Only a player may execute this command");
            return false;
        }

        if(args.length==0) {
            // Prevents error
            sendItemList(player);
            return false;
        } else if(args.length==1 || args.length==2){
            // Generate the path
            String path = MineshaftApi.getItemPath()+File.separator+args[0];
            path = DirUtil.getDirPathFromFilePath(path);
            if(path.isBlank()) {
                path=MineshaftApi.getItemPath();
            }

            String item = DirUtil.getFileFromFilePath(args[0]);
            player.sendMessage("Giving item " + item + " in directory " + path.replace(File.separator,"|"));
            Logger.logWarning("Giving item " + item + " in directory " + path.replace(File.separator,"|"));

            // Error in try/catch
            try {
                ItemStack itemStack = MineshaftApi.getInstance().getItemManagerInstance().getItem(path,item);
                if(args.length==2) {
                    try {
                        int amount = Integer.parseInt(args[1]);
                        itemStack.setAmount(amount);
                    } catch (NumberFormatException ignored) {
                        player.sendMessage(ChatColor.RED + "Invalid amount");
                    }
                }
                player.getInventory().addItem(itemStack);
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(ChatColor.RED + "Invalid item or item definition");
                player.sendMessage(ChatColor.RED + "Type: '/getitem' to view item list");
//                sendItemList(player);
            }
            return false;
        }
        if(args.length <= 3) {

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
        for(String name : MineshaftApi.getInstance().getItemManagerInstance().getItemList().values().stream().sorted().toList()) {
            player.sendMessage(" " + ChatColor.BLUE + name);
        }
    }



    public static void sendSyntax(Player player) {
        player.sendMessage(ChatColor.RED + "/getitem <item> [amount]");
        player.sendMessage(ChatColor.RED + "/getitem gui <folder> [page]");
        player.sendMessage(ChatColor.RED + "/getitem gui null");
    }
}
