/*
 * Copyright (c) 2026. Sebastian Frynas
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

import com.mineshaft.mineshaftapi.manager.location.WarpManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String [] args) {

        if(args[0].equalsIgnoreCase("list")) {
            sender.sendMessage("Listing warps: ");
            for(String warpName : WarpManager.getWarps().keySet()) {
                sender.sendMessage(ChatColor.AQUA + "- " + warpName);
            }
        } else if(args[0].equalsIgnoreCase("get") && args.length<=2) {
            if(args.length==1) {
                sender.sendMessage(ChatColor.RED + "Please specify a name for the id");
            } else if(WarpManager.getWarp(args[1])!=null) {
                Location loc = WarpManager.getWarp(args[1]);
                sender.sendMessage("Position: " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", World: " + loc.getWorld());
            }
        } else if(args.length<=2 && sender instanceof Player player) {
            if(args[0].equalsIgnoreCase("add")) {
                if(args.length==1) {
                    player.sendMessage(ChatColor.RED + "Please specify a name for the id");
                    return false;
                } else if(!WarpManager.getWarps().containsKey(args[1])) {
                    WarpManager.addWarp(args[1], player.getLocation());
                    player.sendMessage("Created warp: " + ChatColor.AQUA + args[1]);
                } else {
                    player.sendMessage(ChatColor.RED + "Warp already exists");
                }
            } else if(args[0].equalsIgnoreCase("set")) {
                if(args.length==1) {
                    player.sendMessage(ChatColor.RED + "Please specify a warp name");
                    return false;
                } else if(!WarpManager.getWarps().containsKey(args[1])) {
                    player.sendMessage(ChatColor.RED + "Cannot override warp. The id '" + ChatColor.DARK_RED + args[1] + ChatColor.RED + "' does not exist. Use /warp add <warp id>");
                } else {
                    WarpManager.addWarp(args[1], player.getLocation());
                    player.sendMessage("Overridden warp location: " + ChatColor.AQUA + args[1]);
                }
            } else if(args[0].equalsIgnoreCase("remove")) {
                if(args.length==1) {
                    player.sendMessage(ChatColor.RED + "Please specify a warp name");
                } else if(!WarpManager.getWarps().containsKey(args[1])) {
                    player.sendMessage(ChatColor.RED + "Cannot remove warp, the specified id is not registered. Please specify a valid id.");
                } else {
                    WarpManager.removeWarp(args[1]);
                    player.sendMessage("Removed warp: " + ChatColor.AQUA + args[1]);
                }
            } else if(args[0].equalsIgnoreCase("goto")||args[0].equalsIgnoreCase("go")||args[0].equalsIgnoreCase("tp")) {
                if(args.length==1) {
                    player.sendMessage(ChatColor.RED + "Please specify a warp name");
                } else if(!WarpManager.getWarps().containsKey(args[1])) {
                    player.sendMessage(ChatColor.RED + "Cannot teleport to warp, the specified id is not registered. Please specify a valid id.");
                } else if(WarpManager.getWarp(args[1])!=null) {
                    player.teleport(WarpManager.getWarp(args[1]));
                    player.sendMessage("Warped to: " + ChatColor.AQUA + args[1]);
                } else {
                    player.sendMessage(ChatColor.RED + "Null location for warp. Error!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Incorrect syntax");
            }
        } else {
            sender.sendMessage(Component.text("Error! Invalid syntax. ", NamedTextColor.RED));
            sender.sendMessage(Component.text("Use: /warp list", NamedTextColor.RED));
            sender.sendMessage(Component.text("Use: /warp <set|add|get|tp|go|goto> <warp id>", NamedTextColor.RED));
        }

        return false;
    }
}
