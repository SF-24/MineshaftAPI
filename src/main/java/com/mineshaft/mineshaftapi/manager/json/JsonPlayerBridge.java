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

import com.mineshaft.mineshaftapi.dependency.DependencyInit;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestEventsObject;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestObject;
import com.mineshaft.mineshaftapi.util.Logger;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.config.Config;
import org.betonquest.betonquest.exceptions.ObjectNotFoundException;
import org.betonquest.betonquest.id.EventID;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonPlayerBridge {

    /**
     * Basic data
     * */

    public static int getCoins(Player player) {
        // if vault is not installed, use built-in currency
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        return jsonPlayerManager.getCoins();
    }

    public static void setCoins(Player player, int coins) {
        // if vault is not installed, use built-in currency
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        if(coins<0) coins=0;
        jsonPlayerManager.setCoins(coins);
    }

    public static int getXp(Player player) {
        // if vault is not installed, use built-in currency
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        return jsonPlayerManager.getXp();
    }

    public static void setXp(Player player, int experience) {
        // if vault is not installed, use built-in currency
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        if(experience<0) experience=0;
        jsonPlayerManager.setXp(experience);
    }

    public static void addXp(Player player, int amount) {
        int xp = getXp(player) + amount;
        if(xp<0) xp=0;
        setXp(player, xp);
    }

    public static void setLevel(Player player, int level) {
        if(level<1) level=1;
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        jsonPlayerManager.setLevel(level);
    }

    public static int getLevel(Player player) {
        // if vault is not installed, use built-in currency
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        return jsonPlayerManager.getLevel();
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
     * Attributes
     */

    public static int getSkillPoints(Player player) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        return jsonPlayerManager.getSkillPoints();
    }

    public static void setSkillPoints(Player player, int points) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        jsonPlayerManager.setSkillPoints(points);
    }

    public static boolean hasAttribute(Player player, String attribute) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        return jsonPlayerManager.hasAttribute(attribute);
    }


    public static int getAttribute(Player player, String attribute) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        return jsonPlayerManager.getAttribute(attribute);
    }

    public static HashMap<String, Integer> getAttributeMap(Player player) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        return jsonPlayerManager.getAttributeMap();
    }

    public static void setAttribute(Player player, String attribute, int value) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        jsonPlayerManager.setAttribute(attribute, value);
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
                final QuestPackage questPackage = Config.getPackages().get(cancelEvent.getQuestPackage());
                final Profile playerProfile = PlayerConverter.getID(player);
                try {
                    BetonQuest.event(playerProfile, new EventID(questPackage,cancelEvent.getCancelEvent()));
                } catch (ObjectNotFoundException e) {
                    Logger.logError("Could not execute BetonQuest event with name: " + cancelEvent.getCancelEvent() + " of package " + cancelEvent.getQuestPackage());
                }
            }
        } else {
            Logger.logError("Attempted to use quest display API while BetonQuest is not enabled. Quest functionality is unavailable without this plugin!");
        }
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        jsonPlayerManager.removeQuest(player, questName);

    }

    public static ArrayList<QuestObject> getQuests(Player player) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        return jsonPlayerManager.getQuests(player);
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

    public static void saveInventory(Player player) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        jsonPlayerManager.setInventory(player);
    }

    public static void loadInventory(Player player) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        jsonPlayerManager.getInventory(player);
    }

    public static double getTempArmourClass(Player player) {
        JsonPlayerManager jsonPlayerManager = new JsonPlayerManager(player);
        return jsonPlayerManager.getPlayerTempArmourClass();
    }
}
