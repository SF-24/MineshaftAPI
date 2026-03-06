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

import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestEventsObject;
import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestObject;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import org.betonquest.betonquest.api.QuestException;
import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.api.instruction.Argument;
import org.betonquest.betonquest.api.profile.OnlineProfile;
import org.betonquest.betonquest.api.quest.action.OnlineAction;

import java.util.List;

public class BetonDisplayQuestEvent implements OnlineAction {
    final Argument<String> id;
    final Argument<String> name;
    final Argument<String> description;
    final Argument<String> objectives;
    final Argument<String> cancelEvent;
    final QuestPackage questPackage;

    public BetonDisplayQuestEvent(Argument<String> id, Argument<String> name, Argument<String> description, Argument<String> objectives, Argument<String> cancelEvent, QuestPackage questPackage) {
        this.id = id;
        this.name=name;
        this.description=description;
        this.objectives=objectives;
        this.cancelEvent=cancelEvent;
        this.questPackage=questPackage;
    }

    @Override
    public void execute(final OnlineProfile profile) throws QuestException {
        QuestObject questObject = new QuestObject(name.getValue(profile), description.getValue(profile), List.of(objectives.getValue(profile)), new QuestEventsObject(questPackage, cancelEvent.getValue(profile)));
        JsonPlayerBridge.addQuest(profile.getPlayer(), id.getValue(profile), questObject);
    }
}
