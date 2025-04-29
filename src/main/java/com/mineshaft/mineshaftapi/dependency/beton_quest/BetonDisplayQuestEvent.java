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

import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.PlayerQuestManagment;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestEventsObject;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestObject;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.api.quest.event.online.OnlineEvent;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.quest.event.NotificationSender;

import java.util.ArrayList;

public class BetonDisplayQuestEvent implements OnlineEvent {
    final String name;
    final String description;
    final ArrayList<String> objectives;
    final String cancelEvent;
    final String questPackage;

    public BetonDisplayQuestEvent(String name, String description, ArrayList<String> objectives, String cancelEvent, String questPackage, final NotificationSender experienceSender) throws InstructionParseException {
        this.name=name;
        this.description=description;
        this.objectives=objectives;
        this.cancelEvent=cancelEvent;
        this.questPackage=questPackage;
    }


    @Override
    public void execute(OnlineProfile profile) throws QuestRuntimeException {
        QuestObject questObject = new QuestObject(name, description, objectives, new QuestEventsObject(cancelEvent, questPackage));
        PlayerQuestManagment.addQuestToPlayer(profile.getPlayer(), questObject);
    }

}
