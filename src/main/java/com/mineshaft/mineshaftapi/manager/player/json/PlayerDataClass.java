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

import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestObject;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.manager.player.player_skills.SkillClass;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerDataClass {

    // Character Data

    double x = 0;
    double y = 64;
    double z = 0;
    float pitch = 0;
    float yaw = 0;
    String world = null;

    HashMap<PlayerSkills, SkillClass> skills = new HashMap<>();
    HashMap<String, Integer> playerAttributes = new HashMap<>();

    HashMap<String, String> playerCharacterData = new HashMap<>();
    HashMap<String, List<String>> playerCharacterListData = new HashMap<>();

    // Quests

    ArrayList<QuestObject> quests = new ArrayList<>();

    // Saved Inventory

    HashMap<Integer, String> inventory = null;

//    ArrayList<PotionEffect> effects;

    // Reputation

    HashMap<String, Integer> reputation = new HashMap<>();

    // Abilities

    HashMap<String, Integer> abilities = new HashMap<>();
    HashMap<String, Integer> spells = new HashMap<>();
    HashMap<String, Integer> passiveAbilities = new HashMap<>();

    // More character data

    int coins = 0;
    int xp = 0;
    int level = 1;
    int skillPoints = 0;
    double tempArmourClass = 0;

    double unarmedDamage = 1.0d;
    int armourClassBonus = 0;

    PlayerDataClass() {
        for(PlayerSkills skill : PlayerSkills.values()) {
            skills.put(skill, new SkillClass(skill));
        }
    }

    /**
     * Inventory
     * */

    public void setLocation(Location location) {
        this.world=location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch=location.getPitch();
        this.yaw=location.getYaw();
    }
    public void setInventory(HashMap<Integer, String> inventory) { this.inventory = inventory; }
    //public void setEffects(ArrayList<PotionEffect> effects) { this.effects = effects; }
    public HashMap<Integer, String> getInventory() { return inventory;}
    public String getLocWorld() { return world;}
    public double getLocX() { return x;}
    public double getLocY() { return y;}
    public double getLocZ() { return z;}
    public float getLocPitch() {return pitch;}
    public float getLocYaw() {return yaw;}
    //public ArrayList<PotionEffect> getEffects() { return effects;}

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

    // Unarmed damage
    public void setUnarmedDamage(double value) {this.unarmedDamage=value;}
    public double getUnarmedDamage() {return this.unarmedDamage;}
    public void setArmourClassBonus(int value) {this.armourClassBonus = value;}
    public int getArmourClassBonus() {return this.armourClassBonus;}

    /**
     * Abilities
     */

    public HashMap<String, Integer> getAbilities() { return abilities;}
    public HashMap<String, Integer> getSpells() { return spells;}
    public HashMap<String, Integer> getPassiveAbilities() { return passiveAbilities;}

    public boolean hasAbility(String ability) { return abilities.containsKey(ability);}
    public boolean hasPassiveAbility(String ability) { return passiveAbilities.containsKey(ability);}
    public boolean hasSpell(String spell) {return spells.containsKey(spell);}

    public void addAbility(String ability, int level) {this.abilities.put(ability, level);}
    public void addPassiveAbility(String ability, int level) {this.passiveAbilities.put(ability, level);}
    public void addSpell(String spell, int level) {this.spells.put(spell, level);}

    public void removeAbility(String ability) {this.abilities.remove(ability);}
    public void removePassiveAbility(String ability) {this.passiveAbilities.remove(ability);}
    public void removeSpell(String spell) {this.spells.remove(spell);}

    /**
     * Character Data
     *
     */

    public HashMap<String, String> getCharacterData() {return playerCharacterData;}
    public String getCharacterDataValue(String key) {return playerCharacterData.get(key);}
    public void setCharacterDataValue(String key, String value) {playerCharacterData.put(key,value);}

    public HashMap<String, List<String>> getCharDataList() {return playerCharacterListData;}
    public List<String> getCharDataListElement(String key) {return playerCharacterListData.get(key);}
    public void setCharDataListElement(String key, List<String> value) {playerCharacterListData.put(key,value);}

    public void addToCharDataList(String listKey, String element) {
        List<String> list = playerCharacterListData.get(listKey);
        if(list==null) list = new ArrayList<>();
        list.add(element);
        playerCharacterListData.put(listKey,list);
    }

    public void removeFromCharDataList(String listKey, String element) {
        List<String> list = playerCharacterListData.get(listKey);
        if(list==null) return;
        list.remove(element);
        playerCharacterListData.put(listKey,list);
    }

    public List<String> getInCharDataList(String listKey) {
        return playerCharacterListData.get(listKey);
    }

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

    public void addSkillXp(PlayerSkills skill, int amount) {
        SkillClass skillClass = skills.get(skill);
        int newValue = skillClass.getXp()+amount;
        skillClass.setXp(newValue);
        skills.put(skill,skillClass);
    }

    public void setSkillLevel(PlayerSkills skill, int value) {
        SkillClass skillClass = skills.get(skill);
        skillClass.setLevel(value);
        skills.put(skill,skillClass);
    }

    public void setProficiencyLevel(PlayerSkills skill, int value) {
        SkillClass skillClass = skills.get(skill);
        skillClass.setProficiencyLevel(value);
        skills.put(skill,skillClass);
    }

    public int getProficiencyLevel(PlayerSkills skill) {
        return skills.get(skill).getProficiencyLevel();
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