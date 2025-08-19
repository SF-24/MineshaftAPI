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

package com.mineshaft.mineshaftapi.dependency.beton_quest;

import com.mineshaft.mineshaftapi.dependency.DependencyInit;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.BetonEventObject;
import com.mineshaft.mineshaftapi.util.Logger;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.api.profile.OnlineProfile;
import org.betonquest.betonquest.api.quest.QuestException;
import org.betonquest.betonquest.api.quest.event.EventID;
import org.bukkit.entity.Player;

public class BetonQuestManager {

    public static void runBetonEvent(Player player, BetonEventObject betonEvent) {
        if(!DependencyInit.hasBetonQuest()) return;
        runBetonPlayerEvent(player, BetonQuest.getInstance().getQuestPackageManager().getPackages().get(betonEvent.getQuestPackageName()),betonEvent.getEvent());
    }

    public static void runBetonPlayerEvent(Player player, QuestPackage questPackage, String event) {
        if(!DependencyInit.hasBetonQuest()) return;
        final OnlineProfile playerProfile = BetonQuest.getInstance().getProfileProvider().getProfile(player);
        try {
            BetonQuest.getInstance().getQuestTypeApi().event(playerProfile,new EventID(BetonQuest.getInstance().getQuestPackageManager(),questPackage,event));
        } catch (QuestException e) {
            Logger.logError("Could not execute BetonQuest event with name: " + event + " of package " + questPackage);
        }
    }

    public static BetonEventObject getBetonQuestEventObject(String packageName, String name, boolean isPlayerEvent) {
        if(!DependencyInit.hasBetonQuest()) return null;
        return new BetonEventObject(isPlayerEvent,packageName,name);
    }

    public static void runBetonPlayerEvent(Player player, String questPackage, String event) {
        if(!DependencyInit.hasBetonQuest() || getPackage(questPackage) == null) return;
        final OnlineProfile playerProfile = BetonQuest.getInstance().getProfileProvider().getProfile(player);
        try {
            BetonQuest.getInstance().getQuestTypeApi().event(playerProfile,new EventID(BetonQuest.getInstance().getQuestPackageManager(),getPackage(questPackage),event));
        } catch (QuestException e) {
            Logger.logError("Could not execute BetonQuest event with name: " + event + " of package " + questPackage);
        }
    }

    public static QuestPackage getPackage(String packageName) {
        if(!DependencyInit.hasBetonQuest()) return null;
        return BetonQuest.getInstance().getQuestPackageManager().getPackages().get(packageName);
    }


}
