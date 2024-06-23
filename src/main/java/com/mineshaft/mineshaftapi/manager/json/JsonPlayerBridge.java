package com.mineshaft.mineshaftapi.manager.json;

import org.bukkit.entity.Player;

public class JsonPlayerBridge {

    public static int getCoins(Player player) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        return jsonPlayerManager.getCoins();
    }

    public static void setCoins(Player player, int coins) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        if(coins<0) coins=0;
        jsonPlayerManager.setCoins(coins);
    }

    public static void addCoins(Player player, int amount) {
        int coins = getCoins(player) + amount;
        if(coins<0) coins=0;
        setCoins(player, coins);
    }

}
