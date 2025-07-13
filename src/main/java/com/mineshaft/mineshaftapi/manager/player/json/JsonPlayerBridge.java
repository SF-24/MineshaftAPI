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

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.dependency.DependencyInit;
import com.mineshaft.mineshaftapi.dependency.beton_quest.BetonQuestManager;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestEventsObject;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestObject;
import com.mineshaft.mineshaftapi.events.AbilityEventType;
import com.mineshaft.mineshaftapi.events.MineshaftAbilityModifyEvent;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.player_skills.PlayerSkills;
import com.mineshaft.mineshaftapi.util.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class JsonPlayerBridge {

    public static JsonPlayerManager getJsonInstance(Player player) { return new JsonPlayerManager(player, JsonProfileBridge.getCurrentProfile(player));}

    /**
     * Basic data
     * */

    public static int getCoins(Player player) {
        // if vault is not installed, use built-in currency
        return getJsonInstance(player).getCoins();
    }

    public static void setCoins(Player player, int coins) {
        // if vault is not installed, use built-in currency
        if(coins<0) coins=0;
        getJsonInstance(player).setCoins(coins);
    }

    public static int getXp(Player player) {
        // if vault is not installed, use built-in currency
        return getJsonInstance(player).getXp();
    }

    public static void setXp(Player player, int experience) {
        // if vault is not installed, use built-in currency
        if(experience<0) experience=0;
        getJsonInstance(player).setXp(experience);
    }

    public static void addXp(Player player, int amount) {
        int xp = getXp(player) + amount;
        if(xp<0) xp=0;

        // Notify EXP:
        if(MineshaftApi.getInstance().getConfigManager().enableExperienceNotification()) {
            String msg = "+" + amount + " EXP";
            player.sendActionBar(Component.text(msg, TextColor.color(85,255,255), TextDecoration.BOLD));
        }
        setXp(player, xp);
    }

    public static void setLevel(Player player, int level) {
        if(level<1) level=1;
        getJsonInstance(player).setLevel(level);
    }

    public static int getLevel(Player player) {
        // if vault is not installed, use built-in currency
        return getJsonInstance(player).getLevel();
    }

    public static int getLevelToPower(Player player, int power) {
        // if vault is not installed, use built-in currency
        return (int) Math.pow(getJsonInstance(player).getLevel(), power);
    }


    public static void addCoins(Player player, int amount) {
        int coins = getCoins(player) + amount;
        if(coins<0) coins=0;
        setCoins(player, coins);
    }

    public static boolean hasCoins(Player player, int amount) {
        int balance = getCoins(player);
        return getCoins(player)>=amount;
    }

    /**
     * Skills
     * */

    public static int getSkillLevel(Player player, PlayerSkills skills) {
        return getJsonInstance(player).getSkillLevel(skills);
    }

    public static void addSkillLevels(Player player, PlayerSkills skills, int amount) {
        getJsonInstance(player).addSkillLevels(skills,amount);
    }

    public static void addSkillXp(Player player, PlayerSkills skills, int amount) {
        getJsonInstance(player).addXp(skills,amount);
    }

    public static void setProficiencyLevel(Player player, PlayerSkills skill, int value) {
        getJsonInstance(player).setProficiencyLevel(skill,value);
    }

    public static void setProficiencyLevels(Player player, List<PlayerSkills> skills, int value) {
        if(skills==null || skills.isEmpty()) return;
        for(PlayerSkills skill : skills) {
            setProficiencyLevel(player,skill,value);
        }
    }

    public static int getProficiencyLevel(Player player, PlayerSkills skill) {
        return getJsonInstance(player).getProficiencyLevel(skill);
    }

    public boolean isSkillProficient(Player player, PlayerSkills skill) {
        return getProficiencyLevel(player,skill)>0;
    }

    public boolean isSkillMastery(Player player, PlayerSkills skill) {
        return getProficiencyLevel(player,skill)>1;
    }

    /**
     * Misc
     * */

    public static void setUnarmedDamage(Player player, double amount) {
        getJsonInstance(player).setUnarmedDamage(amount);
    }

    public static double getUnarmedDamage(Player player) {
        return getJsonInstance(player).getUnarmedDamage();
    }

    /**
     * Attributes
     */

    public static void addSkillPoints(Player player, int points) {
        setSkillPoints(player, points+getSkillPoints(player));
    }

    public static int getSkillPoints(Player player) {
        return getJsonInstance(player).getSkillPoints();
    }

    public static void setSkillPoints(Player player, int points) {
        getJsonInstance(player).setSkillPoints(points);
    }

    public static boolean hasAbilityScore(Player player, String attribute) {
        return getJsonInstance(player).hasAbilityScore(attribute);
    }


    public static int getAbilityScoreValue(Player player, String attribute) {
        return getJsonInstance(player).getAbilityScoreValue(attribute.toLowerCase());
    }

    public static int getAbilityScoreModifier(Player player, String attribute) {
        return PlayerStatManager.calculateAbilityScoreModifier(getAbilityScoreValue(player,attribute.toLowerCase()));
    }

    public static HashMap<String, Integer> getAbilityScoreMap(Player player) {
        return getJsonInstance(player).getAbilityScoreMap();
    }

    public static void setAbilityScore(Player player, String attribute, int value) {
        getJsonInstance(player).setAbilityScoreValue(attribute, value);
    }

    /**
    * QUESTS AND DISPLAY
    */



    public static void addQuest(Player player, QuestObject questObject) {

    }

    public static void removeQuest(Player player, String questName) {
        if (getQuest(player, questName) == null) {
            return;
        }
        if (DependencyInit.hasBetonQuest()) {
            QuestEventsObject cancelEvent = getQuest(player,questName).getEventObject();
            if(cancelEvent!=null) {
                final QuestPackage questPackage = (cancelEvent.getQuestPackage());
                BetonQuestManager.runBetonPlayerEvent(player,questPackage,questName);
            }
        } else {
            Logger.logError("Attempted to use quest display API while BetonQuest is not enabled. Quest functionality is unavailable without this plugin!");
        }
        getJsonInstance(player).removeQuest(player, questName);

    }

    public static ArrayList<QuestObject> getQuests(Player player) {
        return getJsonInstance(player).getQuests(player);
    }

    public static QuestObject getQuest(Player player, String questName) {
        for(QuestObject questObject : getQuests(player)) {
            if(questObject.getName().equalsIgnoreCase(questName)) {
                return questObject;
            }
        }
        return null;
    }

    /**
     * Player inventory
     * */

    public static void saveInventory(Player player, boolean saveHotbar) {
        //getJsonInstance(player).setTempArmourClass();
        getJsonInstance(player).setInventory(player, saveHotbar);
    }

    public static void saveLocation(Player player) {
        getJsonInstance(player).setLocation(player);
    }

    public static void saveEffects(Player player) {
        getJsonInstance(player).setEffects(player);
    }

    public static void setTempArmourClass(Player player) {
        getJsonInstance(player).setTempArmourClass();
    }

    public static void loadInventory(Player player) {
        getJsonInstance(player).getInventory(player);
    }

    public static void loadLocation(Player player) {
        getJsonInstance(player).getLocation(player);
    }

    public static void loadEffects(Player player) {
        getJsonInstance(player).getEffects(player);
    }


    public static double getTempArmourClass(Player player) {
        return getJsonInstance(player).getPlayerTempArmourClass();
    }

    /**
     * Player character data
     *
     * @return
     */

    public static String getCharacterDataValue(Player player, String key) {
        return getJsonInstance(player).getCharacterDataValue(key);
    }

    public static void setCharacterDataValue(Player player, String key, String value) {
        getJsonInstance(player).setCharacterDataValue(key,value);
    }

    public static HashMap<String, List<String>> getCharDataList(Player player) {
        return getJsonInstance(player).getCharDataList();
    }

    public static List<String> getCharDataListElement(Player player, String key) {
        return getJsonInstance(player).getCharDataListElement(key);
    }

    public static void setCharDataListElement(Player player, String key, List<String> value) {
        getJsonInstance(player).setCharDataListElement(key, value);
    }

    public static void addToCharDataList(Player player, String key, String element) {

        getJsonInstance(player).addToCharDataList(key, element);
    }

    public static void addToCharDataList(Player player, String key, List<String> element) {
        if(element==null || element.isEmpty()) return;
        getJsonInstance(player).addToCharDataList(key, element);
    }

    public static void removeFromCharDataList(Player player, String key, String element) {
        getJsonInstance(player).removeFromCharDataList(key, element);
    }

    public static List<String> getInCharDataList(Player player, String key) {
        return getJsonInstance(player).getInCharDataList(key);
    }

    /**
     * Stat bonuses
     * */
    public static void clearPlayerStatBonuses(Player player) {
        getJsonInstance(player).clearPlayerStatBonuses();
    }

    public static void addPlayerStatBonus(Player player, ItemStats stat, double amount) {
        getJsonInstance(player).addPlayerStatBonus(stat,amount);
    }

    public static void setPlayerStatBonus(Player player, ItemStats stat,double amount) {
        getJsonInstance(player).setPlayerStatBonus(stat,amount);
    }

    public static double getStatBonus(Player player, ItemStats stat) {
        return getJsonInstance(player).getStatBonus(stat);
    }

    /**
     * Abilities
     * */

    // Minimum level = 1;
    public static void addAbility(Player player, String ability, int level) {
        MineshaftAbilityModifyEvent abilityEvent = new MineshaftAbilityModifyEvent(player,ability, AbilityEventType.ADD);
        Bukkit.getServer().getPluginManager().callEvent(abilityEvent);
        if(!abilityEvent.isCancelled()) {
            getJsonInstance(player).addAbility(ability,Math.min(level,1));
        }
    }

    public static void addPassiveAbility(Player player, String ability, int level) {
        MineshaftAbilityModifyEvent abilityEvent = new MineshaftAbilityModifyEvent(player,ability, AbilityEventType.ADD);
        Bukkit.getServer().getPluginManager().callEvent(abilityEvent);
        if(!abilityEvent.isCancelled()) {
            getJsonInstance(player).addPassiveAbility(ability, level);
        }
    }

    public static void addSpell(Player player, String spell, int level) {
        getJsonInstance(player).addSpell(spell,level);
    }

    public static void removeAbility(Player player, String ability) {
        MineshaftAbilityModifyEvent abilityEvent = new MineshaftAbilityModifyEvent(player,ability, AbilityEventType.REMOVE);
        Bukkit.getServer().getPluginManager().callEvent(abilityEvent);
        if(!abilityEvent.isCancelled()) {
            getJsonInstance(player).removeAbility(ability);
        }
    }

    public static void removePassiveAbility(Player player, String ability) {
        MineshaftAbilityModifyEvent abilityEvent = new MineshaftAbilityModifyEvent(player,ability, AbilityEventType.REMOVE);
        Bukkit.getServer().getPluginManager().callEvent(abilityEvent);
        if(!abilityEvent.isCancelled()) {
            getJsonInstance(player).removePassiveAbility(ability);
        }
    }

    public static void removeSpell(Player player, String spell) {
        getJsonInstance(player).removeSpell(spell);
    }

    public static boolean hasAbility(Player player, String ability) {
        return getJsonInstance(player).hasAbility(ability);
    }

    public static boolean hasPassiveAbility(Player player, String ability) {
        return getJsonInstance(player).hasPassiveAbility(ability);
    }

    public static boolean hasSpell(Player player, String ability) {
        return getJsonInstance(player).hasSpell(ability);
    }

    public static HashMap<String, Integer> getAbilities(Player player) {
        return getJsonInstance(player).getAbilities();
    }

    public static ArrayList<String> getUnknownAbilities(Player player) {
        ArrayList<String> abilities = new ArrayList<>();
        for(String ability : MineshaftApi.getInstance().getAbilities().keySet()) {
            if(!getAbilities(player).containsKey(ability)) {
                abilities.add(ability);
            }
        }
        return abilities;
    }

    public static HashMap<String, Integer> getPassiveAbilities(Player player) {
        return getJsonInstance(player).getPassiveAbilities();
    }

    public static HashMap<String, Integer> getSpells(Player player) {
        return getJsonInstance(player).getSpells();
    }

    /**
     * Proficiencies
     * */


    public static void addWeaponProficiencies(Player player, List<String> proficiencies) {
        if(proficiencies==null || proficiencies.isEmpty()) return;
        addToCharDataList(player, "weaponProficiencies", proficiencies);
    }

    public static void addWeaponProficiency(Player player, String proficiency) {
        addToCharDataList(player, "weaponProficiencies", proficiency);
    }

    public static List<String> getWeaponProficiencies(Player player) {
        if(getInCharDataList(player, "weaponProficiencies")!=null) {
            return getInCharDataList(player, "weaponProficiencies");
        }
        return Collections.emptyList();
    }

    public static JsonPlayerManager getJsonPlayerManager(Player player, String profile) {
        return new JsonPlayerManager(player, profile);
    }



}
