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
import com.mineshaft.mineshaftapi.util.Logger;
import io.netty.channel.Channel;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.jetbrains.annotations.NotNull;

public class MineshaftCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        // Execute command

        if(args.length==0) {
            // No arguments
            sendMessageToSender(sender, ChatColor.RED + "Too few arguments!");
            sendSyntaxError(sender);
        } else if(args.length==1 && args[0].equals("fix_handler")) {
            for(org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                Channel channel = ((CraftPlayer)player).getHandle().connection.connection.channel;
                channel.eventLoop().submit(() -> {
                    channel.pipeline().remove(player.getName());
                    return null;
                });
            }

        } else if(args.length==1 && args[0].equals("sockets")||args[0].equals("slots")) {

            // TODO: Open the socket UI

        } else if(args[0].equals("reload")) {
            if(args.length==1) {
                MineshaftApi.reloadPlugin();
                sender.sendMessage(ChatColor.AQUA + "Plugin has been reloaded");
            } else if(args.length==2) {
                switch (args[1]) {
                    case "all":
                        // reload plugin
                        MineshaftApi.reloadPlugin();
                        sender.sendMessage(ChatColor.AQUA + "Plugin has been reloaded");
                        break;
                    case "items":
                    case "item":
                        // reload items only
                        MineshaftApi.reloadItems();
                        sender.sendMessage(ChatColor.AQUA + "Custom items have been reloaded");
                        break;
                    case "events":
                    case "event":

                        // reload events only
                        MineshaftApi.reloadEvents();
                        sender.sendMessage(ChatColor.AQUA + "Custom items have been reloaded");
                        break;
                    case "config":
                    case "configs":
                        MineshaftApi.reloadConfigs();
                        sender.sendMessage(ChatColor.AQUA + "Configuration files have been reloaded");
                        break;
                    default:
                        // send error message
                        sendSyntaxError(sender);
                        break;
                }
            } else {
                sendMessageToSender(sender, ChatColor.RED + "Error in command syntax.");
                sendSyntaxError(sender);
            }
        } else if(args[0].equals("event")||args[0].equals("events")) {
            if(args.length==2 && args[1].equals("list")) {
                sendMessageToSender(sender, ChatColor.GOLD + "Showing event list:");

                for(String name : MineshaftApi.getInstance().getEventManagerInstance().getEventList()) {
                    sendMessageToSender(sender," " + ChatColor.BLUE + name);
                }
            } else if((args.length==2 || args.length==3) && args[1].equals("trigger")) {
                if(args.length==2) {
                    sendMessageToSender(sender, ChatColor.RED + "Please specify an event to trigger");
                } else if(!(sender instanceof org.bukkit.entity.Player)) {
                    sendMessageToSender(sender, ChatColor.RED + "Unfortunately events can only be triggered by players");
                } else {

                    if(MineshaftApi.getInstance().getEventManagerInstance().getEventList().contains(args[2])) {
                        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;

                        Logger.logInfo(MineshaftApi.getInstance().getEventManagerInstance().getEvent(args[2]).toString());

                        boolean success = MineshaftApi.getInstance().getEventManagerInstance().runEvent(MineshaftApi.getInstance().getEventManagerInstance().getEvent(args[2]), player.getLocation(), player.getUniqueId(), player);
                        if(success) {
                            sendMessageToSender(sender, ChatColor.AQUA + "Event successfully executed");
                        } else {
                            sendMessageToSender(sender, ChatColor.RED + "Event could not be executed successfully");
                        }
                    } else {
                        sendMessageToSender(sender, ChatColor.RED + "Event does not exist or has not been loaded");
                    }

                }

            } else {
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
        sendMessageToSender(sender, ChatColor.WHITE + "Use: /mineshaft <event|events> list");
        sendMessageToSender(sender, ChatColor.WHITE + "Use: /mineshaft <event|events> trigger <event>");
    }
}
