package com.mineshaft.mineshaftapi.command;

import com.mineshaft.mineshaftapi.text.Logger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
           Player player =  ((Player) sender);
           player.setHealth(((Player) sender).getMaxHealth());
           player.sendMessage(ChatColor.WHITE + "You have been healed");
        } else {
            Logger.logInfo("You are not a player");
        }
        return false;
    }
}
