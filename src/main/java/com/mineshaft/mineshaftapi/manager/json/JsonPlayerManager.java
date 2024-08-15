/*
 *     This program is a Minecraft plugin developed in Java for the Spigot API.
 *     It adds multiple RPG features intended for Multiplayer gameplay.
 *
 *     Copyright (C) 2024  Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.mineshaft.mineshaftapi.manager.json;

import com.google.gson.Gson;
import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.ProfileManager;
import com.mineshaft.mineshaftapi.manager.SidebarManager;
import com.mineshaft.mineshaftapi.manager.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.UUID;

public class JsonPlayerManager {

    Player player = null;

    MineshaftApi mineshaftApi = MineshaftApi.getInstance();

    public JsonPlayerManager(Player player) {
        this.player=player;
        try {
            initiateFile(player.getUniqueId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateFile(UUID uuid) throws Exception {

        String path = ProfileManager.getProfilePathOfPlayer(uuid);
        File file = new File(path, "player_data.json");

        if(!file.exists()) {
            makeNewFile(file);
        }
    }

    // gets player data json file
    public static File getFile(Player player) {
        String id = "player_data";

        String path = ProfileManager.getProfilePathOfPlayer(player.getUniqueId());
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

        PlayerDataClass playerDataClass = makeEmptyData();
        writeData(playerDataClass, file);
    }


    // write data to a file
    public static void writeData(PlayerDataClass settingsData, File file) {
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
    public static PlayerDataClass makeEmptyData() {
        return new PlayerDataClass();
    }

    //loads player json data file
    public PlayerDataClass loadData(Player player) {
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

        PlayerDataClass pdc = gson.fromJson(reader, PlayerDataClass.class);
        if(pdc==null) {
            Logger.logError("ERROR! PlayerDataClass is null");
        }

        assert pdc != null;
        pdc.updateSkills();

        return pdc;
    }

    public void saveFile(PlayerDataClass data, UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        writeData(data, getFile(player));
    }

    public void saveFile(PlayerDataClass data) {
        writeData(data, getFile(player));
    }

    public int getSkillLevel(Player player, PlayerSkills skill) {
        return loadData(player).getSkillLevel(skill);
    }

    public void addSkillLevel(Player player, PlayerSkills skill, int amount) {
        PlayerDataClass data = loadData(player);
        int level = data.getSkillLevel(skill);
        level+=amount;
        data.setSkillLevel(skill,level);
        saveFile(data);
    }

    public void addXp(PlayerSkills skill, int amount) {
        PlayerDataClass data = loadData(player);
        int exp = data.getSkillXp(skill);
        exp += amount;
        data.setSkillXp(skill, exp);
        saveFile(data);
    }

    public int getXp() {
        PlayerDataClass data = loadData(this.player);
        return data.getXp();
    }

    public int getLevel() {
        PlayerDataClass data = loadData(this.player);
        return data.getLevel();
    }

    public int getCoins() {
        PlayerDataClass data = loadData(this.player);
        int coins = data.getCoins();
        return coins;
    }

    public void setCoins(int amount) {
        PlayerDataClass data = loadData(player);
        data.setCoins(amount);
        saveFile(data);
        if(MineshaftApi.getInstance().getConfigManager().getSidebarEnabled()) {
            SidebarManager.updateCoins(player);
        }
    }

    public void setXp(int amount) {
        PlayerDataClass data = loadData(player);
        data.setXp(amount);
        saveFile(data);
        // TODO: update XP and level
    }

    public void setLevel(int amount) {
        PlayerDataClass data = loadData(player);
        data.setLevel(amount);
        saveFile(data);
        // TODO: update level display
    }
}
