package com.mineshaft.mineshaftapi.command;

import com.mineshaft.mineshaftapi.text.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Logger.logInfo("Only a player can run this command");
            return false;
        }

        Player player = (Player) sender;
        player.sendMessage(ChatColor.RED + "This functionality is yet to be implemented");

        return false;
    }
}
