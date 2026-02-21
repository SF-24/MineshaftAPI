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

package com.mineshaft.mineshaftapi.manager.location;

import com.google.gson.Gson;
import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.save_data.SaveLocation;
import org.bukkit.Location;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WarpJsonManager {

    public WarpJsonManager() {
        try {
            initiateFile();
        } catch (Exception e) {
            Logger.logError("Cannot create Warp Json File");
            e.printStackTrace();
        }
    }

    private void initiateFile() {
        getFile();
    }

    // gets player data json file
    public static File getFile() {
        File file = new File(MineshaftApi.getPluginDataPath(),"warp_data.json");

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

        WarpJsonClass jsonClass = makeEmptyData();
        writeData(jsonClass, file);
    }


    // write data to a file
    public static void writeData(WarpJsonClass settingsData, File file) {
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
    public static WarpJsonClass makeEmptyData() {
        return new WarpJsonClass();
    }

    //loads player json data file
    public WarpJsonClass loadData() {
        File file = getFile();

        Gson gson = new Gson();
        Reader reader = null;

        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            Logger.logError("Cannot create reader for warp data file");
            e.printStackTrace();
        }

        if(reader==null) {
            Logger.logError("Error reading file \"" + file.getName() + "\"");
            return null;
        }

        WarpJsonClass wdc = gson.fromJson(reader, WarpJsonClass.class);

        if(wdc==null) {
            Logger.logError("ERROR! WarpJsonClass is null for file: " + file.getPath());
        }

        assert wdc != null;
        return wdc;
    }

    public void saveFile(WarpJsonClass data) {
        writeData(data, getFile());
    }

    public Map<String, SaveLocation> getWarps() {
        return loadData().getWarps();
    }

    public void setWarps(HashMap<String, SaveLocation> newWarps) {
        WarpJsonClass warpJsonClass = loadData();
        warpJsonClass.setWarps(newWarps);
        saveFile(warpJsonClass);
    }

    public void addWarp(String name, Location location) {
        WarpJsonClass warpJsonClass = loadData();
        warpJsonClass.addWarp(name,location);
        saveFile(warpJsonClass);
    }

    public void removeWarp(String name) {
        WarpJsonClass warpJsonClass = loadData();
        warpJsonClass.removeWarp(name);
        saveFile(warpJsonClass);
    }

}
