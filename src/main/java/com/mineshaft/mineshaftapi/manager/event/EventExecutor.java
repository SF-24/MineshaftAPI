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

package com.mineshaft.mineshaftapi.manager.event;

import org.bukkit.Location;

import java.util.UUID;

public abstract class EventExecutor {

    protected Event event;
    protected Location loc;

    public EventExecutor(Event event, Location loc) {
        this.event=event;
        this.loc=loc;
    }

    public void executeEvent() {
        // TODO:
    }

    public abstract void executeEvent(UUID casterId);
}
