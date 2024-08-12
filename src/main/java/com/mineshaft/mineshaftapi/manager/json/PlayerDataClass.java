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

import com.mineshaft.mineshaftapi.manager.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.manager.player_skills.SkillClass;

import java.util.HashMap;

public class PlayerDataClass {

    HashMap<PlayerSkills, SkillClass> skills = new HashMap<>();

    int coins = 0;
    int xp = 0;
    int level = 1;

    PlayerDataClass() {
        for(PlayerSkills skill : PlayerSkills.values()) {
            skills.put(skill, new SkillClass(skill));
        }
    }


    public int getLevel() { return level; };
    public int getXp() { return xp; }
    public int getCoins() { return coins; }

    // Coins manipulator
    public void setCoins(int amount) {
        this.coins=amount;
    }
    public void setXp(int amount) {this.xp=amount;}
    public void setLevel(int amount) {this.level=amount;}

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

}