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

package com.mineshaft.mineshaftapi.dependency.mythic_mob;

import com.mineshaft.mineshaftapi.util.Logger;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicEventListener implements Listener {

    @EventHandler
    public void onMythicMechanicLoad(MythicMechanicLoadEvent event)	{
        Logger.logInfo("MythicMechanicLoadEvent called for mechanic " + event.getMechanicName());

        if(event.getMechanicName().equalsIgnoreCase("mineshaftevent"))	{
            event.register(new EventMechanic(event.getConfig()));
            Logger.logInfo("-- Registered mineshaft mechanic!");
        } else if(event.getMechanicName().equalsIgnoreCase("targetedmineshaftevent"))	{
            event.register(new TargetEventMechanic(event.getConfig()));
            Logger.logInfo("-- Registered mineshaft targeting mechanic!");
        } else if(event.getMechanic().equals("mineshaftarmourclass") || event.getMechanic().equals("mythic_armour_class") || event.getMechanic().equals("mineshaftarmour") || event.getMechanic().equals("mineshaft_armour")) {
            event.register(new MythicSetArmourClassEvent(event.getConfig()));
        }
    }

}
