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
import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestObject;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.ProfileManager;
import com.mineshaft.mineshaftapi.manager.ui.SidebarManager;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.util.Logger;
import io.lumine.mythic.bukkit.utils.serialize.InventorySerialization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JsonPlayerManager {

    Player player;

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

    /**
     * Data modification
     * */

    public int getSkillLevel(PlayerSkills skill) {
        return loadData(player).getSkillLevel(skill);
    }



    public void saveFile(PlayerDataClass data) {
        writeData(data, getFile(player));
    }

    public void addSkillLevels(PlayerSkills skill, int amount) {
        PlayerDataClass data = loadData(player);
        int level = data.getSkillLevel(skill);
        level+=amount;
        data.setSkillLevel(skill,level);
        saveFile(data);
    }

    public void setProficiencyLevel(PlayerSkills skill,int proficiencyLevel) {
        PlayerDataClass data = loadData(player);
        data.setProficiencyLevel(skill,proficiencyLevel);
        saveFile(data);
    }

    public int getProficiencyLevel(PlayerSkills skill) {
        PlayerDataClass data = loadData(player);
        return data.getProficiencyLevel(skill);
    }

    /**
     * Basic data:
     * */

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
    }

    public void setLevel(int amount) {
        PlayerDataClass data = loadData(player);
        data.setLevel(amount);
        saveFile(data);
    }

    /**
     * Misc
     * */

    public void setUnarmedDamage(double amount) {
        PlayerDataClass data = loadData(player);
        data.setUnarmedDamage(amount);
        saveFile(data);
    }

    public double getUnarmedDamage() {
        PlayerDataClass data = loadData(player);
        return data.getUnarmedDamage();
    }

    public void setArmourClassBonus(int amount) {
        PlayerDataClass data = loadData(player);
        data.setArmourClassBonus(amount);
        saveFile(data);
    }

    public int getArmourClassBonus() {
        PlayerDataClass data = loadData(player);
        return data.getArmourClassBonus();
    }

    /**
     * Player character data
     *
     */

    public String getCharacterDataValue(String key) {
        PlayerDataClass data = loadData(player);
        return data.getCharacterDataValue(key);
    }

    public void setCharacterDataValue(String key, String value) {
        PlayerDataClass data = loadData(player);
        data.setCharacterDataValue(key,value);
        saveFile(data);
    }

    public HashMap<String, List<String>> getCharDataList() {
        PlayerDataClass data = loadData(player);
        return data.getCharDataList();
    }

    public List<String> getCharDataListElement(String key) {
        PlayerDataClass data = loadData(player);
        return data.getCharDataListElement(key);
    }

    public void setCharDataListElement(String key, List<String> value) {
        PlayerDataClass data = loadData(player);
        data.setCharDataListElement(key, value);
        saveFile(data);
    }

    public void addToCharDataList(String key, String element) {
        PlayerDataClass data = loadData(player);
        data.addToCharDataList(key, element);
        saveFile(data);
    }

    public void addToCharDataList(String key, List<String> elements) {
        PlayerDataClass data = loadData(player);
        for(String val : elements) {
            data.addToCharDataList(key, val);
        }
        saveFile(data);
    }

    public void removeFromCharDataList(String key, String element) {
        PlayerDataClass data = loadData(player);
        data.removeFromCharDataList(key, element);
        saveFile(data);
    }

    public List<String> getInCharDataList(String key) {
        PlayerDataClass data = loadData(player);
        return data.getInCharDataList(key);
    }

    /**
     * Spells and Abilities
     * */

    public void addAbility(String ability, int level) {
        PlayerDataClass data = loadData(player);
        data.addAbility(ability,level);
        saveFile(data);
    }

    public void addPassiveAbility(String ability, int level) {
        PlayerDataClass data = loadData(player);
        data.addPassiveAbility(ability,level);
        saveFile(data);
    }

    public void addSpell(String spell, int level) {
        PlayerDataClass data = loadData(player);
        data.addSpell(spell,level);
        saveFile(data);
    }

    public void removeAbility(String ability) {
        PlayerDataClass data = loadData(player);
        data.removeAbility(ability);
        saveFile(data);
    }

    public void removePassiveAbility(String ability) {
        PlayerDataClass data = loadData(player);
        data.removePassiveAbility(ability);
        saveFile(data);
    }

    public void removeSpell(String spell) {
        PlayerDataClass data = loadData(player);
        data.removeSpell(spell);
        saveFile(data);
    }

    public boolean hasAbility(String ability) {
        PlayerDataClass data = loadData(player);
        return data.hasAbility(ability);
    }

    public boolean hasPassiveAbility(String ability) {
        PlayerDataClass data = loadData(player);
        return data.hasPassiveAbility(ability);
    }

    public boolean hasSpell(String ability) {
        PlayerDataClass data = loadData(player);
        return data.hasSpell(ability);
    }

    public HashMap<String, Integer> getAbilities() {
        PlayerDataClass data = loadData(player);
        return data.getAbilities();
    }

    public HashMap<String, Integer> getPassiveAbilities() {
        PlayerDataClass data = loadData(player);
        return data.getPassiveAbilities();
    }

    public HashMap<String, Integer> getSpells() {
        PlayerDataClass data = loadData(player);
        return data.getSpells();
    }

    /**
     * Player Attributes API
     * */

    public void setSkillPoints(int points) {
        PlayerDataClass data = loadData(player);
        data.setSkillPoints(points);
        saveFile(data);
    }

    public int getSkillPoints() {
        PlayerDataClass data = loadData(player);
        return data.getSkillPoints();
    }

    public void setAttribute(String attribute, int value) {
        PlayerDataClass data = loadData(player);
        data.setAttribute(attribute, value);
        saveFile(data);
    }

    public int getAttribute(String attribute) {
        PlayerDataClass data = loadData(player);
        return data.getAttribute(attribute);
    }

    public boolean hasAttribute(String attribute) {
        PlayerDataClass data = loadData(player);
        return data.hasAttribute(attribute);
    }

    public HashMap<String, Integer> getAttributeMap() {
        PlayerDataClass data = loadData(player);
        return data.getAttributes();
    }

    /**
     * Quests
     * */

    public void addQuest(Player player, QuestObject questObject) {
        PlayerDataClass data = loadData(player);
        data.addQuest(questObject);
        saveFile(data);
    }

    public boolean removeQuest(Player player, String questName) {
        PlayerDataClass data = loadData(player);
        boolean value = data.removeQuest(questName);
        saveFile(data);
        return value;
    }

    public ArrayList<QuestObject> getQuests(Player player) {
        PlayerDataClass data = loadData(player);
        return data.getQuests();
    }

    /**
     * Inventory
     */

    public void setTempArmourClass() {
        PlayerDataClass data = loadData(player);
        data.setTempArmourClass(PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS,player));
        saveFile(data);
    }

    public void setInventory(Player player) {
        PlayerDataClass data = loadData(player);

        HashMap<Integer, String> inventory = new HashMap<>();
        for(int i = 0 ; i < player.getInventory().getSize(); i++) {
            if (player.getInventory().getItem(i) != null) {
                inventory.put(i, InventorySerialization.encodeItemStackToString(player.getInventory().getItem(i)));
            }
        }
        data.setInventory(inventory);
        saveFile(data);
    }

    public void getInventory(Player player) {
        PlayerDataClass data = loadData(player);
        HashMap<Integer, String> inv = data.getInventory();

        for(int i : inv.keySet()) {
            if(inv.get(i)!=null) {
                player.getInventory().setItem(i, InventorySerialization.decodeItemStack(inv.get(i)));
            }
        }
    }

    public double getPlayerTempArmourClass() {
        PlayerDataClass data = loadData(player);
        return data.getTempArmourClass();
    }
}
