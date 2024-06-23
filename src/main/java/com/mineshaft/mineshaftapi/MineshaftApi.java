package com.mineshaft.mineshaftapi;

import com.mineshaft.mineshaftapi.command.GetItemCommand;
import com.mineshaft.mineshaftapi.command.HealCommand;
import com.mineshaft.mineshaftapi.command.MenuCommand;
import com.mineshaft.mineshaftapi.command.MonetaryBalanceCommand;
import com.mineshaft.mineshaftapi.listener.JoinListener;
import com.mineshaft.mineshaftapi.manager.PlayerManager;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.text.Language;
import com.mineshaft.mineshaftapi.text.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MineshaftApi extends JavaPlugin {

    ItemManager itemManager;

    @Override
    public void onEnable() {
        switch (getDebugLanguage()) {
            case POLISH:
                Logger.logInfo("Wtyczka mineshaft API została włączona");
            case ENGLISH:
                Logger.logInfo("Plugin mineshaft API has been enabled");
        }

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        getCommand("heal").setExecutor(new HealCommand());
        getCommand("menu").setExecutor(new MenuCommand());
        getCommand("balance").setExecutor(new MonetaryBalanceCommand());
        getCommand("getitem").setExecutor(new GetItemCommand());

        itemManager=new ItemManager();
        itemManager.initialiseItems();
    }

    @Override
    public void onDisable() {
        switch (getDebugLanguage()) {
            case POLISH:
                Logger.logInfo("Wtyczka mineshaft API została wyłączona");
            case ENGLISH:
                Logger.logInfo("Plugin mineshaft API has been disabled");
        }

        PlayerManager.KickPlayers();
    }

    public static Language getLanguage(Player player) {
        return Language.ENGLISH;
    }

    public static Language getDebugLanguage() {return Language.ENGLISH;}

    public static MineshaftApi getInstance() {
        return MineshaftApi.getPlugin(MineshaftApi.class);
    }

    private final String pluginFolder = "plugins" + File.separator +"Mineshaft";

    public String getPluginFolder() { return MineshaftApi.getInstance().pluginFolder;}

    public String getPluginDataPath() {return getPluginFolder() + File.separator + "Data"; }

    public String getPlayerDataPath() {return getPluginDataPath() + File.separator + "PlayerData"; }

    public String getItemPath() {return getPluginDataPath() + File.separator + "Items"; }

    public ItemManager getItemManagerInstance() {return itemManager;}
}
