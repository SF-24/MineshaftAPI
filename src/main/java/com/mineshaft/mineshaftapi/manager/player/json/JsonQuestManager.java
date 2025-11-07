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
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestObject;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestStatus;
import com.mineshaft.mineshaftapi.manager.player.ProfileManager;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class JsonQuestManager {

    Player player;
    String profile;

    public JsonQuestManager(Player player, String profile) {
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
        File file = new File(path, "quest_data.json");

        if(!file.exists()) {
            makeNewFile(file);
        }
    }

    // gets player data json file
    public static File getFile(Player player, String profile) {
        String id = "quest_data";

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

        QuestDataClass QuestDataClass = makeEmptyData();
        writeData(QuestDataClass, file);
    }


    // write data to a file
    public static void writeData(QuestDataClass settingsData, File file) {
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
    public static QuestDataClass makeEmptyData() {
        return new QuestDataClass();
    }

    //loads player json data file
    public QuestDataClass loadData(Player player) {
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

        QuestDataClass pdc = gson.fromJson(reader, QuestDataClass.class);
        if(pdc==null) {
            Logger.logError("ERROR! QuestDataClass is null");
        }

        assert pdc != null;
        return pdc;
    }

    public void saveFile(QuestDataClass data, UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        writeData(data, getFile(player,profile));
    }

    /**
     * Data modification
     * */

    public void saveFile(QuestDataClass data) {
        writeData(data, getFile(player,profile));
    }

    public QuestObject getQuest(String id) {
        QuestDataClass data = loadData(player);
        return data.getQuest(id);
    }

    public boolean hasQuest(String id) {
        QuestDataClass data = loadData(player);
        return data.hasQuest(id);
    }

    public void removeQuest(String id) {
        QuestDataClass data = loadData(player);
        data.removeQuest(id);
        saveFile(data);
    }

    public void addQuest(String id, QuestObject questObject) {
        QuestDataClass data = loadData(player);
        data.addQuest(id,questObject);
        saveFile(data);
    }

    public HashMap<String, QuestObject> getQuests() {
        QuestDataClass data = loadData(player);
        return data.getQuests();
    }

    public HashMap<String, QuestObject> getQuests(QuestStatus status) {
        QuestDataClass data = loadData(player);
        HashMap<String, QuestObject> returnValues = new HashMap<>();
        for(String id : data.getQuests().keySet()) {
            QuestObject questObject = data.getQuests().get(id);
            if(questObject.getStatus().equals(status)) {
                returnValues.put(id, questObject);
            }
        }
        return returnValues;
    }
}
