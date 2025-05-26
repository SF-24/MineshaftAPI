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

import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.QuestException;
import org.betonquest.betonquest.api.quest.event.PlayerEvent;
import org.betonquest.betonquest.api.quest.event.PlayerEventFactory;
import org.betonquest.betonquest.api.quest.event.online.OnlineEventAdapter;
import org.betonquest.betonquest.instruction.argument.Argument;
import org.betonquest.betonquest.instruction.variable.Variable;
import org.betonquest.betonquest.quest.PrimaryServerThreadData;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadEvent;

import java.util.List;

public class BetonDisplayQuestEventFactory implements PlayerEventFactory {

    private final BetonQuestLoggerFactory loggerFactory;
    private final PrimaryServerThreadData data;

    public BetonDisplayQuestEventFactory(final BetonQuestLoggerFactory loggerFactory, PrimaryServerThreadData data) {
        this.loggerFactory = loggerFactory;
        this.data=data;
    }


    @Override
    public PlayerEvent parsePlayer(org.betonquest.betonquest.instruction.Instruction instruction) throws QuestException {

        final Variable<String> name = instruction.get(Argument.STRING);
        final Variable<String> description = instruction.get(Argument.STRING);
        final Variable<String> cancelEvent = instruction.get(Argument.STRING);
        final Variable<List<String>> objectivesBase = instruction.getList(Argument.STRING);

        QuestPackage questPackage = instruction.getPackage();

        return new PrimaryServerThreadEvent(new OnlineEventAdapter(
                new BetonDisplayQuestEvent(name, description, objectivesBase, cancelEvent, questPackage),
                loggerFactory.create(BetonExperienceEvent.class),
                instruction.getPackage()
        ), data);
    }
}

