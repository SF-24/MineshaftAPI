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
import com.mineshaft.mineshaftapi.util.Logger;
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
        String id = null;
        String name = null;
        String description = null;
        String objectives = null;
        String cancelEvent = null;
        if(this.id!=null) id=this.id.getValue(profile);
        if(this.name!=null) name=this.name.getValue(profile);
        if(this.description!=null) description=this.description.getValue(profile);
        if(this.objectives!=null) {
            try {
                objectives = this.objectives.getValue(profile);
            } catch (Exception e) {
                Logger.logError("Error! this.objectives = null, in BetonDisplayQuestEvent:64");
            }
        }
        if(this.cancelEvent!=null) cancelEvent=this.cancelEvent.getValue(profile);
        if(objectives==null) {
            QuestObject questObject = new QuestObject(name,description,List.of(), new QuestEventsObject(questPackage, cancelEvent));
            JsonPlayerBridge.addQuest(profile.getPlayer(), id, questObject);
        } else {
            QuestObject questObject = new QuestObject(name,description,List.of(objectives), new QuestEventsObject(questPackage, cancelEvent));
            JsonPlayerBridge.addQuest(profile.getPlayer(), id, questObject);
        }
    }
}
