package com.mineshaft.mineshaftapi.manager;

import com.mineshaft.mineshaftapi.MineshaftApi;

import java.io.File;
import java.util.UUID;

public class ProfileManager {

    public static String getProfilePathOfPlayer(UUID playerId) {
        return getDataPathOfPlayer(playerId) + File.separator + "Default";
    }

    public static String getDataPathOfPlayer(UUID uuid) {
        return MineshaftApi.getInstance().getPlayerDataPath() + File.separator + uuid;
    }

}
