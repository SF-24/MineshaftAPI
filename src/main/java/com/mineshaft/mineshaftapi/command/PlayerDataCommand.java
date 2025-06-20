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

import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerDataCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {


        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Specified player does not exist or is offline");
            return false;
        }
        if (args[2].equals("experience")) {
            if (args[1].equalsIgnoreCase("set") && args.length == 4) {
                try {
                    int amount = Integer.parseInt(args[3]);
                    JsonPlayerBridge.setXp(player, amount);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Amount specified must be a number");
                    sendUsageMessage(sender);
                }
            } else if (args[1].equalsIgnoreCase("add") && args.length == 4) {
                try {
                    int amount = Integer.parseInt(args[3]);
                    JsonPlayerBridge.addXp(player, amount);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Amount specified must be a number");
                    sendUsageMessage(sender);
                }
            } else if (args[1].equalsIgnoreCase("get") && args.length==3) {
                sender.sendMessage(player.getName() + " has " + ChatColor.AQUA + JsonPlayerBridge.getXp(player) + ChatColor.WHITE + " XP");
                sendUsageMessage(sender);
                return false;
            }
            if (args[2].equals("ac")||args[2].equals("armour_class")) {
                if(args[1].equalsIgnoreCase("get")) {
                    sender.sendMessage(player.getName() + " has an Armour Class of " + ChatColor.GREEN + PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS,player));
                    JsonPlayerBridge.setTempArmourClass(player);
                } else {
                    sendUsageMessage(sender);
                }
            }
        }
        sendUsageMessage(sender);
        return false;
    }

    public static void sendUsageMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "USAGE:");
        sender.sendMessage(ChatColor.RED + "/player_data <player> <set|add> experience <amount>");
        sender.sendMessage(ChatColor.RED + "/player_data <player> get experience");
        sender.sendMessage(ChatColor.RED + "/player_data <player> get <armour_class|ac>");
    }
}
