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

package com.mineshaft.mineshaftapi.events;

import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemCategory;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemSubcategory;
import com.mineshaft.mineshaftapi.manager.player.ActionType;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class MineshaftUseItemEvent extends Event implements Cancellable {

    protected boolean cancelled = false;

    @Getter
    private final ArrayList<String> events;
    @Getter
    private final ActionType clickType;
    @Getter
    private final ItemCategory itemCategory;
    @Getter
    private final ItemSubcategory itemSubcategory;
    @Getter
    private final ItemStack itemStack;
    @Getter
    private final UUID itemUUID;
    @Getter
    protected final Player player;

    public MineshaftUseItemEvent(Player player, UUID uuid, ArrayList<String> events, ItemStack itemStack, ActionType clickType) {
        this.player = player;
        this.itemCategory = ItemManager.getItemCategory(uuid);
        this.itemSubcategory = ItemManager.getItemSubcategory(itemStack);
        this.itemStack=itemStack;
        this.clickType=clickType;
        this.itemUUID = uuid;
        this.events=events;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }
}
