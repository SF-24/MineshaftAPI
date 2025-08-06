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

package com.mineshaft.mineshaftapi.manager.block.json;

import com.google.gson.Gson;
import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Location;

import java.io.*;
import java.util.ArrayList;

public class JsonBlockCacheManager {

    String world;

    public JsonBlockCacheManager(String world) {
        this.world=world;
        try {
            initiateFile(world);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateFile(String world) throws Exception {

        String path = MineshaftApi.getInstance().getBlockCachePath();
        File file = new File(path, world);

        if(!file.exists()) {
            makeNewFile(file);
        }
    }

    // gets player data json file
    public static File getFile(String world) {

        String path = MineshaftApi.getInstance().getBlockCachePath();
        File file = new File(path,world + ".json");

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

        BlockCache cacheClass = makeEmptyData();
        writeData(cacheClass, file);
    }


    // write data to a file
    public static void writeData(BlockCache settingsData, File file) {
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
    public static BlockCache makeEmptyData() {
        return new BlockCache();
    }

    //loads player json data file
    public BlockCache loadData() {
        File file = getFile(world);
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

        BlockCache playerDiscoveryClass = gson.fromJson(reader, BlockCache.class);
        if(playerDiscoveryClass==null) {
            Logger.logError("ERROR! PlayerDiscoveryClass is null");
        }
        assert playerDiscoveryClass != null;
        return playerDiscoveryClass;
    }

    public void saveFile(BlockCache data, String world) {
        writeData(data, getFile(world));
    }

    public void saveFile(BlockCache data) {
        writeData(data, getFile(world));
    }

    /**
     * Data modification
     */

    public ArrayList<BlockClass> getBlockCache() {
        return loadData().getBlockCache();
    }

    public BlockClass getBlock(Location location) {
        return loadData().getBlock(location);
    }

    public void cacheBlock(BlockClass blockClass) {
        BlockCache data = loadData();
        data.cacheBlock(blockClass);
        saveFile(data);
    }

    public void removeBlock(Location location) {
        BlockCache data = loadData();
        data.removeBlock(location);
        saveFile(data);
    }
}
