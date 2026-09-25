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

package com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.config.quest.QuestPackage;

public class QuestEventsObject {

    protected String questPackage;
    protected String cancelEvent;

    public QuestEventsObject(QuestPackage questPackage, String cancelEvent) {
        this.cancelEvent=cancelEvent;
        this.questPackage=questPackage.getSourcePath();
    }

    public QuestPackage getQuestPackage() { return BetonQuest.getInstance().getBetonQuestApi().packages().getPackage(questPackage); }
    public String getCancelEvent() { return cancelEvent; }



}
