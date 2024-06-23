package com.mineshaft.mineshaftapi.manager.json;

import com.google.gson.Gson;
import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.ProfileManager;
import com.mineshaft.mineshaftapi.manager.SidebarManager;
import com.mineshaft.mineshaftapi.manager.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.text.Logger;
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
        System.out.println("PATH: " + path);

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
            System.err.println("[rpg] Error reading file \"" + file.getName() + "\"");
            return null;
        }

        PlayerDataClass pdc = gson.fromJson(reader, PlayerDataClass.class);
        if(pdc==null) {
            System.out.println("ERROR! PlayerDataClass is null");
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

    public int getCoins() {
        PlayerDataClass data = loadData(this.player);
        int coins = data.getCoins();
        return coins;
    }

    public void setCoins(int amount) {
        PlayerDataClass data = loadData(player);
        data.setCoins(amount);
        saveFile(data);
        SidebarManager.updateCoins(player);
    }
}
