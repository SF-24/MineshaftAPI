package com.mineshaft.mineshaftapi.listener;

import com.mineshaft.mineshaftapi.manager.json.JsonPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // Make a new json player manager
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
    }
}
