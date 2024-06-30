package com.mineshaft.mineshaftapi.command;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.text.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            return false;
        }

        if(args.length>1) {
            player.sendMessage(ChatColor.RED + "Too many parameters");
            return false;
        }

        String itemName = args[0];
        ItemStack item = MineshaftApi.getInstance().getItemManagerInstance().getItem(itemName);

        if(item==null) player.sendMessage(ChatColor.RED + "Selected item does not exist!");

        player.getInventory().addItem(item);

        return false;
    }
}
