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

import com.google.gson.Gson;
import com.mineshaft.mineshaftapi.dependency.world_guard.Town;
import com.mineshaft.mineshaftapi.manager.player.ProfileManager;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class JsonDiscoveryManager {

    Player player;
    String profile;

    public JsonDiscoveryManager(Player player, String profile) {
        this.player=player;
        this.profile=profile;
        try {
            initiateFile(player.getUniqueId(), profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateFile(UUID uuid, String profile) throws Exception {

        String path = ProfileManager.getProfilePathOfPlayer(uuid,profile);
        File file = new File(path, "discoveries.json");

        if(!file.exists()) {
            makeNewFile(file);
        }
    }

    // gets player data json file
    public static File getFile(Player player, String profile) {
        String id = "discoveries";

        String path = ProfileManager.getProfilePathOfPlayer(player.getUniqueId(),profile);
        File file = new File(path,id + ".json");

        if(!file.exists()) {
            makeNewFile(file);
        }
        return file;
    }

    public static void makeNewFile(File file) {
        try {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        PlayerDiscoveryClass PlayerDiscoveryClass = makeEmptyData();
        writeData(PlayerDiscoveryClass, file);
    }


    // write data to a file
    public static void writeData(PlayerDiscoveryClass settingsData, File file) {
        Writer writer = null;
        Gson gson = new Gson();

        try {
            writer = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(writer==null) {
            Logger.logError("ERROR! Attempted writing to file \"" + file.getName() + "\" | Writer == null");
            return;
        }

        //IF WRITER IS NOT NULL
        gson.toJson(settingsData, writer);
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Logger.logError("Error in MineshaftApi - Could not flush or close writer");
        }

        Logger.logInfo("Written json data correctly");
    }

    // make empty data file
    public static PlayerDiscoveryClass makeEmptyData() {
        return new PlayerDiscoveryClass();
    }

    //loads player json data file
    public PlayerDiscoveryClass loadData(Player player) {
        File file = getFile(player,profile);
        Gson gson = new Gson();
        Reader reader = null;

        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(reader==null) {
            Logger.logError("Error reading file \"" + file.getName() + "\"");
            return null;
        }

        PlayerDiscoveryClass playerDiscoveryClass = gson.fromJson(reader, PlayerDiscoveryClass.class);
        if(playerDiscoveryClass==null) {
            Logger.logError("ERROR! PlayerDiscoveryClass is null");
        }
        assert playerDiscoveryClass != null;
        return playerDiscoveryClass;
    }

    public void saveFile(PlayerDiscoveryClass data, UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        writeData(data, getFile(player,profile));
    }

    public void saveFile(PlayerDiscoveryClass data) {
        writeData(data, getFile(player,profile));
    }

    /**
     * Data modification
     * */

    public ArrayList<Town> getDiscoveredTowns() {
        return loadData(player).getDiscoveredTowns();
    }

    public boolean hasDiscoveredTown(Town town) { return loadData(player).hasDiscoveredTown(town); }

    public void addDiscoveredTown(Town town) {
        PlayerDiscoveryClass data = loadData(player);
        data.addDiscoveredTown(town);
        saveFile(data);
    }


}
