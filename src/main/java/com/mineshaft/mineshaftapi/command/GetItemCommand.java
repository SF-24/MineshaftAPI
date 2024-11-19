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
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.ItemManagerAccessUtility;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.QuickFunction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

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
        } else if(args.length>1 && args.length <= 3) {

            if(args[0].equalsIgnoreCase("gui")) {
                String folder = args[1];


                if(args.length>2) {
                    try{
                        int page = Integer.parseInt(args[2]);
                        if(page<1) {
                            page=1;
                            player.sendMessage(ChatColor.RED+"number cannot be less than 1");
                        }
                        sendItemListUi(player,folder,page);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED+ "Incorrect number format. Use:");
                        sendSyntax(player);
                    }
                } else {
                    sendItemListUi(player,folder,1);
                }
            } else {
                player.sendMessage(ChatColor.RED + "Incorrect command syntax. Use:");
                sendSyntax(player);
            }
        } else {
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

    protected void sendItemListUi(Player player, String folder, int page) {

        Inventory itemInventory = Bukkit.createInventory(null, 54, ChatColor.BLACK + "Item View UI");

        ItemStack emptyItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta emptyItemMeta = emptyItem.getItemMeta();
        emptyItemMeta = QuickFunction.setLocalisedName(emptyItemMeta,"immutable");
        emptyItemMeta.setDisplayName("");
        emptyItem.setItemMeta(emptyItemMeta);

        for(int i = 45; i<54; i++) {
            itemInventory.setItem(i,emptyItem);
        }

        ArrayList<ArrayList<ItemStack>> itemList = ItemManagerAccessUtility.getItemsEnMasseFull(folder,-2);

        if(itemList.size()>=2) {
            if(page!=itemList.size()) {
                ItemStack nextItem = new ItemStack(Material.ARROW);
                ItemMeta nextMeta = nextItem.getItemMeta();
                nextMeta.setDisplayName("Next Page");
                nextMeta=QuickFunction.setLocalisedName(nextMeta,"next");
                nextItem.setItemMeta(nextMeta);
                itemInventory.setItem(53, nextItem);
            }
            if(page!=1) {
                ItemStack backItem = new ItemStack(Material.ARROW);
                ItemMeta backMeta = backItem.getItemMeta();
                backMeta.setDisplayName("Previous Page");
                backMeta=QuickFunction.setLocalisedName(backMeta,"back");
                backItem.setItemMeta(backMeta);
                itemInventory.setItem(45,backItem);
            }
        }

        player.openInventory(itemInventory);
    }

    public static void sendSyntax(Player player) {
        player.sendMessage(ChatColor.RED + "/getitem <item>");
        player.sendMessage(ChatColor.RED + "/getitem <gui> <folder> [page]");
        player.sendMessage(ChatColor.RED + "/getitem <gui> null");
    }
}
