/*
 * Copyright (c) 2025. Sebastian Frynas
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

package com.mineshaft.mineshaftapi.manager.player;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.player.json.JsonProfileBridge;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class ProfileManager {

    public static String getPluginPath() {
        return MineshaftApi.getInstance().getPluginDataPath();
    }

    public static String getProfileFolderPath() {
        return MineshaftApi.getInstance().getPluginDataPath() + File.separator + "Profiles";
    }

    public static String getProfilePathOfPlayer(Player player) {
        return getDataPathOfPlayer(player.getUniqueId()) + File.separator + JsonProfileBridge.getCurrentProfile(player);
    }

    public static String getProfilePathOfPlayer(UUID playerId) {
        return getDataPathOfPlayer(playerId) + File.separator + JsonProfileBridge.getCurrentProfile(Bukkit.getPlayer(playerId));
    }

    public static String getDataPathOfPlayer(UUID uuid) {
        return MineshaftApi.getInstance().getPlayerDataPath() + File.separator + uuid;
    }

}
