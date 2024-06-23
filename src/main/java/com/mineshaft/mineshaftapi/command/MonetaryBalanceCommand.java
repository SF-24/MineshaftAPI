package com.mineshaft.mineshaftapi.command;

import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.text.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MonetaryBalanceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Logger.logError("You must be a player to run this command");
            return false;
        }

        Player player = (Player) sender;

        switch (args.length) {
            case 1:
                if(args[0].equalsIgnoreCase("get")) {
                    player.sendMessage(ChatColor.WHITE + "You have " + ChatColor.GOLD + JsonPlayerBridge.getCoins(player) + ChatColor.WHITE + " coins");
                } else sendErrorMessage(player, !args[0].equalsIgnoreCase("help"));
                return false;
            case 2:
                if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("remove")) {
                    try {
                        int amount = Integer.parseInt(args[1]);

                        if (args[0].equalsIgnoreCase("set")) {
                            JsonPlayerBridge.setCoins(player, amount);
                        } else if (args[0].equalsIgnoreCase("add")) {
                            JsonPlayerBridge.addCoins(player,amount);
                        } else if (args[0].equalsIgnoreCase("remove")) {
                            JsonPlayerBridge.addCoins(player, -amount);
                        }
                        player.sendMessage(ChatColor.WHITE + "You now have " + ChatColor.GOLD + JsonPlayerBridge.getCoins(player) + ChatColor.WHITE + " coins");
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "NumberFormatException. Amount must be an integer");
                    }
                }
                return false;
            case 0:
            default:
                sendErrorMessage(player, true);
        }

        return false;
    }

    private void sendErrorMessage(Player player, boolean error) {
        if(error) {
            player.sendMessage(ChatColor.RED + "Invalid command execution");
        }
        player.sendMessage("Command use:");
        player.sendMessage(ChatColor.RED + "/balance help");
        player.sendMessage(ChatColor.RED + "/balance get");
        player.sendMessage(ChatColor.RED + "/balance set <amount>");
        player.sendMessage(ChatColor.RED + "/balance add <amount>");
        player.sendMessage(ChatColor.RED + "/balance remove <amount>");
    }
}
