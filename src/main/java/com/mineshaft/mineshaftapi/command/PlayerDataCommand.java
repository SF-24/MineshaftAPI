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
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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


        if(args.length==0||args.length==1) {
            sendUsageMessage(sender);
            return false;
        }

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
                return false;
            } else if (args[1].equalsIgnoreCase("get") && args.length==3) {
                sender.sendMessage(player.getName() + " has " + ChatColor.AQUA + JsonPlayerBridge.getXp(player) + ChatColor.WHITE + " XP");
                sendUsageMessage(sender);
                return false;
            }
        } else if ((args[2].equals("ac")||args[2].equals("armour_class"))&&args.length==3) {
            if(args[1].equalsIgnoreCase("get")) {
                sender.sendMessage(Component.text(player.getName() + " has an Armour Class of ").append(Component.text(PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS,player),NamedTextColor.GREEN)));
                JsonPlayerBridge.setTempArmourClass(player);
                return false;
            } else {
                sendUsageMessage(sender);
            }
        } else if(args[2].equalsIgnoreCase("ability")) {
            if(args.length==3) {
                if(args[1].equalsIgnoreCase("get")) {
                    sender.sendMessage("Listing abilities:");
                    for(String abilityName : JsonPlayerBridge.getAbilities(player).keySet()) {
                        sender.sendMessage(Component.text(abilityName, NamedTextColor.DARK_AQUA));
                    }
                    return false;
                }
            } else if(args.length==4) {
                if(args[1].equalsIgnoreCase("add")) {
                    String ability = args[3];
                    if(!JsonPlayerBridge.getAbilities(player).containsKey(ability)) {
                        if(MineshaftApi.getInstance().getAbilities().contains(ability)) {
                            JsonPlayerBridge.addAbility(player, ability, 1);
                            sender.sendMessage("Command execution successful");
                        } else {
                            sender.sendMessage(Component.text("This ability does not exist",NamedTextColor.RED));
                        }
                    } else {
                        sender.sendMessage(Component.text("The player already knows the ability " + ability, NamedTextColor.RED));
                    }
                    return false;
                } else if(args[1].equalsIgnoreCase("remove")) {
                    String ability = args[3];
                    if(JsonPlayerBridge.getAbilities(player).containsKey(ability)) {
                        JsonPlayerBridge.removeAbility(player, ability);
                        sender.sendMessage("Command execution successful");
                    } else {
                        sender.sendMessage(Component.text("The player does not know " + ability,NamedTextColor.RED));
                    }
                    return false;
                }
            }
            return false;
        }
        sendUsageMessage(sender);
        return false;
    }

    public static void sendUsageMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "USAGE:");
        sender.sendMessage(ChatColor.RED + "/player_data <player> <set|add> experience <amount>");
        sender.sendMessage(ChatColor.RED + "/player_data <player> get experience");
        sender.sendMessage(ChatColor.RED + "/player_data <player> get <armour_class|ac>");
        sender.sendMessage(ChatColor.RED + "/player_data <player> add ability <ability name>");
    }
}
