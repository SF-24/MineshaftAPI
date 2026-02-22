/*
 * Copyright (c) 2026. Sebastian Frynas
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

package com.mineshaft.mineshaftapi.util.player;

import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerUtil {

    public static UUID getPlayerId(String playerName) {
        return Bukkit.getOfflinePlayer(playerName).getUniqueId();
    }

    public static boolean isValidOfflinePlayer(String playerName) {
        return Bukkit.getOfflinePlayer(playerName).getPlayer()!=null;
    }

    public static boolean isValidPlayer(String playerName) {
        return Bukkit.getPlayer(playerName) != null;
    }

    public static boolean isValidPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid) != null;
    }

    public static boolean isValidOfflinePlayer(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getPlayer()!=null;
    }

    public static String getPlayerName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}
