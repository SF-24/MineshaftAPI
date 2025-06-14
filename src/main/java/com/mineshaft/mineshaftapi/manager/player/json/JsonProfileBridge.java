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

package com.mineshaft.mineshaftapi.manager.player.json;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JsonProfileBridge {

    public static JsonProfileManager getProfileJsonInstance(Player player) {return new JsonProfileManager(player);};

    public static String getCurrentProfile(Player player) {
        if(player==null) return null;
        return getProfileJsonInstance(player).getCurrentProfile();
    }

    public static List<String> getUnlockedCultures(Player player) {
        if(player==null) return List.of();
        return getProfileJsonInstance(player).getUnlockedCultures();
    }

    public static void addUnlockedCulture(Player player, String culture) {
        if(player==null) return;
        getProfileJsonInstance(player).addUnlockedCulture(culture);
    }

    public static void setCurrentProfile(Player player, String profile) {
        getProfileJsonInstance(player).setCurrentProfile(profile);
    }

    public static void addProfile(Player player, String profile) {
        getProfileJsonInstance(player).addProfile(profile);
    }

    public static void removeProfile(Player player, String profile) {
        getProfileJsonInstance(player).removeProfile(profile);
    }

    public static ArrayList<String> getProfiles(Player player) {
        return getProfileJsonInstance(player).getProfiles();
    }


}
