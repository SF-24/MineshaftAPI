package com.mineshaft.mineshaftapi.manager;

import com.mineshaft.mineshaftapi.MineshaftApi;
import org.bukkit.entity.Player;

public class PlayerManager {
    public static void KickPlayers() {
        for(Player player : MineshaftApi.getInstance().getServer().getOnlinePlayers()) {
            switch (MineshaftApi.getLanguage(player)) {
                case ENGLISH:
                    player.kickPlayer("Server is reloading or has stopped");
                case POLISH:
                    player.kickPlayer("Serwer został wyłączony lub jest przeładowywany");
            }
        }
    }

}
