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

import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.api.quest.event.online.OnlineEvent;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.quest.event.NotificationSender;

public class BetonExperienceEvent implements OnlineEvent {
    final int amount;

    public BetonExperienceEvent(final int amount, final NotificationSender experienceSender) throws InstructionParseException {
        this.amount=amount;
    }


    @Override
    public void execute(OnlineProfile profile) throws QuestRuntimeException {
        JsonPlayerBridge.addXp(profile.getOnlineProfile().get().getPlayer(), amount);
    }
}
