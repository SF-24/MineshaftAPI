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