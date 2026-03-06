/*
 * Copyright (c) 2026. Sebastian Frynas
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

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.dependency.beton_quest.events.BetonDisplayQuestEventFactory;
import com.mineshaft.mineshaftapi.dependency.beton_quest.events.BetonExperienceEventFactory;
import com.mineshaft.mineshaftapi.dependency.beton_quest.events.BetonRemoveQuestEventFactory;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;

public class BetonQuestDependencyInit {

    public static void init() {
        BetonQuestLoggerFactory loggerFactory = MineshaftApi.getInstance().getServer().getServicesManager().load(BetonQuestLoggerFactory.class);

        BetonQuest.getInstance().getBetonQuestApi().actions().registry().register("mineshaft_xp", new BetonExperienceEventFactory(loggerFactory));
        BetonQuest.getInstance().getBetonQuestApi().actions().registry().register("add_quest", new BetonDisplayQuestEventFactory(loggerFactory));
        BetonQuest.getInstance().getBetonQuestApi().actions().registry().register("rem_quest", new BetonRemoveQuestEventFactory(loggerFactory));

    }

}
