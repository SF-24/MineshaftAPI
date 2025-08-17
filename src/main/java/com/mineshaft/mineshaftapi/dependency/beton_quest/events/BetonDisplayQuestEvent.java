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

package com.mineshaft.mineshaftapi.dependency.beton_quest.events;

import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.PlayerQuestManagment;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestEventsObject;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestObject;
import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.api.instruction.variable.Variable;
import org.betonquest.betonquest.api.profile.OnlineProfile;
import org.betonquest.betonquest.api.quest.QuestException;
import org.betonquest.betonquest.api.quest.event.online.OnlineEvent;

import java.util.List;

public class BetonDisplayQuestEvent implements OnlineEvent {
    final Variable<String> name;
    final Variable<String> description;
    final Variable<List<String>> objectives;
    final Variable<String> cancelEvent;
    final QuestPackage questPackage;

    public BetonDisplayQuestEvent(Variable<String> name, Variable<String> description, Variable<List<String>> objectives, Variable<String> cancelEvent, QuestPackage questPackage) {
        this.name=name;
        this.description=description;
        this.objectives=objectives;
        this.cancelEvent=cancelEvent;
        this.questPackage=questPackage;
    }

    @Override
    public void execute(final OnlineProfile profile) throws QuestException {
        QuestObject questObject = new QuestObject(name.getValue(profile), description.getValue(profile), objectives.getValue(profile), new QuestEventsObject(questPackage, cancelEvent.getValue(profile)));
        PlayerQuestManagment.addQuestToPlayer(profile.getPlayer(), questObject);
    }
}
