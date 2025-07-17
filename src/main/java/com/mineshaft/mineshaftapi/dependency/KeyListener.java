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

package com.mineshaft.mineshaftapi.dependency;

import com.mineshaft.mineshaftapi.events.MineshaftClickTypeEvent;
import com.mineshaft.mineshaftapi.manager.event.click.ClickType;
import eu.asangarin.arikeys.api.AriKeyPressEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class KeyListener implements Listener {

    @EventHandler
    public void onKey(AriKeyPressEvent e) {

        // Currently useless variable, but may be useful later: (currently commented out)
//        boolean isAbilityKey = e.getId().equals(NamespacedKey.fromString("ab_key:ab_left")) || e.getId().equals(NamespacedKey.fromString("ab_key:ab_right"));

        boolean isLeft = e.getId().getKey().equals("primary")&&e.getId().getNamespace().equals("abilities");
        boolean isRight = e.getId().getKey().equals("secondary")&&e.getId().getNamespace().equals("abilities");
        boolean isCentre = e.getId().getKey().equals("tertiary")&&e.getId().getNamespace().equals("abilities");
        boolean isClear = e.getId().getKey().equals("clear")&&e.getId().getNamespace().equals("abilities");

        // Record clicks - parse event
        MineshaftClickTypeEvent clickEvent;
        if(isRight) {
            clickEvent = new MineshaftClickTypeEvent(e.getPlayer(), ClickType.RIGHT);
        } else if(isLeft) {
            clickEvent = new MineshaftClickTypeEvent(e.getPlayer(), ClickType.LEFT);
        } else if(isCentre) {
            clickEvent = new MineshaftClickTypeEvent(e.getPlayer(), ClickType.MIDDLE);
        } else if(isClear) {
            clickEvent = new MineshaftClickTypeEvent(e.getPlayer(), ClickType.CLEAR);
        } else if(e.getId().getKey().equals("hotbar_up")) {
            clickEvent = new MineshaftClickTypeEvent(e.getPlayer(), ClickType.HOTBAR_UP);
        } else if(e.getId().getKey().equals("hotbar_down")) {
            clickEvent = new MineshaftClickTypeEvent(e.getPlayer(), ClickType.HOTBAR_DOWN);
        } else {
            return;
        }
        Bukkit.getPluginManager().callEvent(clickEvent);
    }
}
