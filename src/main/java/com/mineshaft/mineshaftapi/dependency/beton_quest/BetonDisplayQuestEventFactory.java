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

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.event.Event;
import org.betonquest.betonquest.api.quest.event.EventFactory;
import org.betonquest.betonquest.api.quest.event.online.OnlineEventAdapter;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.PrimaryServerThreadData;
import org.betonquest.betonquest.quest.event.*;

import java.util.ArrayList;

public class BetonDisplayQuestEventFactory implements EventFactory {

    private final BetonQuestLoggerFactory loggerFactory;
    private final PrimaryServerThreadData data;

    public BetonDisplayQuestEventFactory(final BetonQuestLoggerFactory loggerFactory, PrimaryServerThreadData data) {
        this.loggerFactory = loggerFactory;
        this.data=data;
    }


    @Override
    public Event parseEvent(Instruction instruction) throws InstructionParseException {

        final BetonQuestLogger log = loggerFactory.create(BetonExperienceEvent.class);
        final NotificationSender notificationSender;

        QuestPackage questPackage = instruction.getPackage();

        if (instruction.hasArgument("notify")) {
            notificationSender = new IngameNotificationSender(log, instruction.getPackage(), instruction.getID().getFullID(), NotificationLevel.INFO, "xp_given");
        } else {
            notificationSender = new NoNotificationSender();
        }

        final String name = instruction.getOptional("name", "Unnamed Quest");
        final String description = instruction.getOptional("name", "");
        final String cancelEvent = instruction.getOptional("onCancel", "");

        ArrayList<String> objectivesBase = (ArrayList<String>) instruction.getList("objectives",instruction::getOptional);

        return new PrimaryServerThreadEvent(new OnlineEventAdapter(new BetonDisplayQuestEvent(name, description, objectivesBase, cancelEvent, questPackage.getConfig().getName(),notificationSender), log, questPackage), data);
    }
}

