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
import com.mineshaft.mineshaftapi.manager.player.ProfileManager;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class JsonProfileManager {


    Player player;

    public JsonProfileManager(Player player) {
        if(player==null||player.getUniqueId()==null) return;
        this.player=player;
        try {
            initiateFile(player.getUniqueId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateFile(UUID uuid) throws Exception {

        String path = ProfileManager.getProfileFolderPath();
        File file = new File(path, uuid+".json");

        if(!file.exists()) {
            makeNewFile(file);
        }
    }

    // gets player data json file
    public static File getFile(Player player) {
        String path = ProfileManager.getProfileFolderPath();
        File file = new File(path,player.getUniqueId() + ".json");

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

        PlayerProfileClass playerDataClass = makeEmptyData();
        writeData(playerDataClass, file);
    }


    // write data to a file
    public static void writeData(PlayerProfileClass settingsData, File file) {
        Writer writer = null;
        Gson gson = new Gson();

        try {
            writer = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(writer==null) {
            Logger.logError("ERROR! Attempted writing to profile file \"" + file.getName() + "\" | Writer == null");
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
    public static PlayerProfileClass makeEmptyData() {
        return new PlayerProfileClass();
    }

    //loads player json data file
    public PlayerProfileClass loadData(Player player) {
        File file = getFile(player);
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

        PlayerProfileClass pdc = gson.fromJson(reader, PlayerProfileClass.class);
        if(pdc==null) {
            Logger.logError("ERROR! PlayerProfileClass is null");
        }
        return pdc;
    }

    public void saveFile(PlayerProfileClass data, UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        writeData(data, getFile(player));
    }

    public void saveFile(PlayerProfileClass data) {
        writeData(data, getFile(player));
    }

    /**
     * Data modification
     */

    public String getCurrentProfile() {
        return loadData(player).getCurrentProfile();
    }

    public void setCurrentProfile(String currentProfile) {
        PlayerProfileClass ppc = loadData(player);
        ppc.setCurrentProfile(currentProfile);
        saveFile(ppc);
    }

    public void addProfile(String name) {
        PlayerProfileClass ppc = loadData(player);
        ppc.addProfile(name);
        saveFile(ppc);
    }

    public void removeProfile(String name) {
        PlayerProfileClass ppc = loadData(player);
        ppc.removeProfile(name);
        saveFile(ppc);
    }

    public ArrayList<String> getProfiles() {
        PlayerProfileClass ppc = loadData(player);
        return ppc.getProfiles();
    }
}
