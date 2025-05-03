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

package com.mineshaft.mineshaftapi.manager.json;

import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestObject;
import com.mineshaft.mineshaftapi.manager.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.manager.player_skills.SkillClass;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerDataClass {

    HashMap<PlayerSkills, SkillClass> skills = new HashMap<>();
    HashMap<String, Integer> playerAttributes = new HashMap<>();
    ArrayList<QuestObject> quests = new ArrayList<>();

    HashMap<Integer, String> inventory = null;

    int coins = 0;
    int xp = 0;
    int level = 1;
    int skillPoints = 0;
    double tempArmourClass = 0;


    PlayerDataClass() {
        for(PlayerSkills skill : PlayerSkills.values()) {
            skills.put(skill, new SkillClass(skill));
        }
    }

    /**
     * Inventory
     * */

    public void setInventory(HashMap<Integer, String> inventory) { this.inventory = inventory; }
    public HashMap<Integer, String> getInventory() { return inventory;}

    public void setTempArmourClass(double value) {this.tempArmourClass = value;}
    public double getTempArmourClass() {return tempArmourClass;}

    /**
    * Basic data
    * */

    public int getLevel() { return level; };
    public int getXp() { return xp; }
    public int getCoins() { return coins; }

    // Coins manipulator
    public void setCoins(int amount) {
        this.coins=amount;
    }
    public void setXp(int amount) {this.xp=amount;}
    public void setLevel(int amount) {this.level=amount;}

    /**
     * Player Attributes API
     * */
    public HashMap<String, Integer> getAttributes() {return playerAttributes;}

    public int getAttribute(String attribute) {return playerAttributes.get(attribute)==null?-1:playerAttributes.get(attribute);}
    public boolean hasAttribute(String attribute) {return playerAttributes.containsKey(attribute);}
    public int getSkillPoints() {return skillPoints;}

    public void setAttribute(String attribute, int value) {playerAttributes.put(attribute, value);}
    public void setSkillPoints(int value) {skillPoints = value;}

    /**
     * Skills
     */

    // Skill data manipulation
    public int getSkillLevel(PlayerSkills skill) {
        return skills.get(skill).getLevel();
    }
    public int getSkillXp(PlayerSkills skill) {
        return skills.get(skill).getXp();
    }

    // set XP for skill
    public void setSkillXp(PlayerSkills skill, int amount) {
        SkillClass skillClass = skills.get(skill);
        skillClass.setXp(amount);
        updateSkillLevel(skill);
        skills.put(skill,skillClass);
    }

    public void setSkillLevel(PlayerSkills skill, int amount) {
        SkillClass skillClass = skills.get(skill);
        int newValue = skillClass.getXp()+amount;
        skillClass.setLevel(newValue);
        skills.put(skill,skillClass);
    }

    // check if a skill level needs to be recalculated
    public void updateSkillLevel(PlayerSkills skill) {
        int exp = getSkillXp(skill);
        // TODO: ADD LEVEL CALCULATION
    }

    // check if hashmap is missing any skills
    public void updateSkills() {
        for(PlayerSkills skill : PlayerSkills.values()) {
            if(!skills.containsKey(skill)) {
                skills.put(skill,new SkillClass(skill));
            }
        }
    }

    public HashMap<PlayerSkills, SkillClass> getSkillMap() {
        return skills;
    }

    /**
     * Quests
     */

    public void addQuest(QuestObject questObject) {
        quests.add(questObject);
    }

    public ArrayList<QuestObject> getQuests() {
        return quests;
    }

    public boolean removeQuest(String questName) {
        for(int i=0; i<quests.size(); i++) {
            if(quests.get(i).getName().equalsIgnoreCase(questName)) {
                quests.remove(i);
                return true;
            }
        }
        return false;
    }



}