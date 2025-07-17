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

package com.mineshaft.mineshaftapi.manager.event.executor;

import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.PlayerEventExecutor;
import com.mineshaft.mineshaftapi.manager.event.event_subclass.PlayerWandEvent;
import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class WandEventExecutor extends PlayerEventExecutor {

    public WandEventExecutor(Event event, Player player) {
        super(event,player);
    }

    @Override
    public void executeEvent(UUID casterId) {
        super.executeEvent();
        if(event instanceof PlayerWandEvent) {
            if (event.getEventType().equals(EventType.PLAYER_WAND_LIGHT)) {
                ((PlayerWandEvent) event).wandLightEvent(Objects.requireNonNull(player));
            } else if (event.getEventType().equals(EventType.PLAYER_WAND_EXTINGUISH)) {
                ((PlayerWandEvent) event).wandExtinguishEvent(Objects.requireNonNull(player));
            } else {
                player.sendMessage("unknown event type");
            }
        }
    }
}
