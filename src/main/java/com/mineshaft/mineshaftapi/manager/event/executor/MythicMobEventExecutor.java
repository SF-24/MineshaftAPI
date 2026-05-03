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

package com.mineshaft.mineshaftapi.manager.event.executor;

import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.EventExecutor;
import com.mineshaft.mineshaftapi.manager.event.event_subclass.MythicMobEvent;
import org.bukkit.Location;

import java.util.UUID;

public class MythicMobEventExecutor extends EventExecutor {

    public MythicMobEventExecutor(Event event, Location loc) {
        super(event, loc);
    }

    @Override
    public void executeEvent(UUID casterId) {
        super.executeEvent();

        if(event instanceof MythicMobEvent) {

        }
    }
}
