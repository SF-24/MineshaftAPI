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
import net.minecraft.world.entity.player.Player;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MineshaftCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        // Execute command

        if(args.length==0) {
            // No arguments
            sendMessageToSender(sender, ChatColor.RED + "Too few arguments!");
            sendSyntaxError(sender);
        }
        else if(args.length==1) {

            // 1 Argument
            if(args[0].equals("reload")) {
                MineshaftApi.reloadPlugin();
                sender.sendMessage(ChatColor.AQUA + "Plugin has been reloaded");
            } else {
                sendSyntaxError(sender);
            }

        } else if(args.length==2) {

            // 2 Arguments

            if(args[0].equals("reload")) {
                if(args[1].equals("all")) {

                    // reload plugin
                    MineshaftApi.reloadPlugin();
                    sender.sendMessage(ChatColor.AQUA + "Plugin has been reloaded");

                } else if(args[1].equals("items")) {

                    // reload items only
                    MineshaftApi.reloadItems();
                    sender.sendMessage(ChatColor.AQUA + "Custom items have been reloaded");

                } else if(args[1].equals("config") || args[1].equals("configs")) {

                    // TODO: reload configs
                    sendMessageToSender(sender, ChatColor.RED + "This functionality is yet to be implemented");
                } else {
                    sendSyntaxError(sender);
                }

            } else {

                // Syntax error
                sendMessageToSender(sender, ChatColor.RED + "Error in command syntax.");
                sendSyntaxError(sender);
            }
        } else {
            // To many arguments
            sendMessageToSender(sender,  ChatColor.RED + "Too many arguments!");
            sendSyntaxError(sender);
        }

        return false;
    }

    public void sendMessageToSender(CommandSender sender, String message) {
        String prefix = "";

        if(!(sender instanceof Player)) {
            prefix = "[MineshaftApi] ";
        }

        sender.sendMessage(prefix + message);
    }

    public void sendSyntaxError(CommandSender sender) {
        sendMessageToSender(sender, ChatColor.RED + "Syntax error. Incorrect usage!");
        sendMessageToSender(sender, ChatColor.WHITE + "Use: /mineshaft reload [all|items|config]");
    }
}
