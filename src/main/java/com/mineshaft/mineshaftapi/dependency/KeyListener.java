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

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.event.click.ClickType;
import eu.asangarin.arikeys.api.AriKeyPressEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class KeyListener implements Listener {

    @EventHandler
    public void onKey(AriKeyPressEvent e) {

        // Currently useless check
//        boolean isAbilityKey = e.getId().equals(NamespacedKey.fromString("ab_key:ab_left")) || e.getId().equals(NamespacedKey.fromString("ab_key:ab_right"));

        boolean isLeft = e.getId().equals(NamespacedKey.fromString("ab_key:ab_left"));
        boolean isRight = e.getId().equals(NamespacedKey.fromString("ab_key:ab_right"));

        // Record clicks
        if(isRight) {
            MineshaftApi.getClickCache().cacheClick(e.getPlayer(), ClickType.RIGHT);
        } else if(isLeft) {
            MineshaftApi.getClickCache().cacheClick(e.getPlayer(), ClickType.LEFT);
        }
    }
}
