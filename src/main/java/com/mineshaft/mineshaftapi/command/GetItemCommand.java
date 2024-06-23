package com.mineshaft.mineshaftapi.command;

import com.mineshaft.mineshaftapi.text.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Logger.logError("Only a player may execute this command");
            return false;
        }



        return false;
    }
}
