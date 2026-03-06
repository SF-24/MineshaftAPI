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

import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import org.betonquest.betonquest.api.QuestException;
import org.betonquest.betonquest.api.instruction.Argument;
import org.betonquest.betonquest.api.profile.OnlineProfile;
import org.betonquest.betonquest.api.quest.action.OnlineAction;

public class BetonExperienceEvent implements OnlineAction {
    /**
     * The amount of exp to apply.
     */
    private final Argument<Number> amount;

    public BetonExperienceEvent(final Argument<Number> amount) {
        this.amount = amount;
    }

    @Override
    public void execute(final OnlineProfile profile) throws QuestException {
        JsonPlayerBridge.addXp(profile.getPlayer(), amount.getValue(profile).intValue());
    }
}
