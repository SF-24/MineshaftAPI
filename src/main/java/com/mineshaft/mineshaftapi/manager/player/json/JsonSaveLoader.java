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
import java.util.UUID;

public class JsonSaveLoader {

    private final Class<?> clazz;
    String path;
    Player player;
    String fileName;

    public JsonSaveLoader(Player player, String path, String fileName, Class<?> clazz) {
        this.fileName = fileName;
        this.clazz=clazz;
        this.path=path;
        if(player == null) {
            return;
        } else {
            player.getUniqueId();
        }
        this.player=player;
        try {
            initiateFile(player.getUniqueId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initiateFile(UUID uuid) throws Exception {

        String path = ProfileManager.getProfileFolderPath();
        File file = new File(path, this.fileName+".json");

        if(!file.exists()) {
            makeNewFile(file,clazz);
        }
    }

    // gets player data json file
    public static File getFile(Player player, String path, String fileName, Class<?> clazz) {
        File file = new File(path,fileName + ".json");

        if(!file.exists()) {
            makeNewFile(file,clazz);
        }
        return file;
    }

    public static void makeNewFile(File file, Class<?> clazz) {
        try {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Object jsonSaveObject;
        try {
            jsonSaveObject = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if(jsonSaveObject instanceof JsonSaveObject) {
            writeData(jsonSaveObject, file);
        }
    }


    // write data to a file
    public static void writeData(Object settingsData, File file) {
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


    //loads player json data file
    public JsonSaveObject loadData(Player player) {
        File file = getFile(player, path,fileName,clazz);
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

        Object pdc = gson.fromJson(reader, clazz);

        if((pdc instanceof JsonSaveObject)) {
            return (JsonSaveObject) pdc;

        }
        Logger.logError("ERROR! PlayerProfileClass is null");
        return null;
    }

    public void saveFile(JsonSaveObject data, UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        writeData(data, getFile(player,path,fileName,clazz));
    }

    public void saveFile(JsonSaveObject data) {
        writeData(data, getFile(player,path,fileName,clazz));
    }

}
