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

package com.mineshaft.mineshaftapi.listener;

import com.mineshaft.mineshaftapi.MineshaftApi;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;

public class WorldListener implements Listener {

    @EventHandler
    void onChunkUnloaded(ChunkUnloadEvent e) {
        for(@NotNull Entity en : e.getChunk().getEntities()) {
            if(en instanceof LivingEntity && MineshaftApi.getInstance().getPendingAbilities(en.getUniqueId())!=null) {
                MineshaftApi.getInstance().clearPendingAbilities(en.getUniqueId());
            }
        }
    }

}
