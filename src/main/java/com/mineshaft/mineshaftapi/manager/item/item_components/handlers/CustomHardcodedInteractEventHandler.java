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

package com.mineshaft.mineshaftapi.manager.item.item_components.handlers;

import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.player.ActionType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class CustomHardcodedInteractEventHandler extends ComponentHandler {
    private final UUID uuid;

    public CustomHardcodedInteractEventHandler(UUID itemUniqueId) {
        super(null);
        this.uuid=itemUniqueId;
    }

    @Override
    public void handlePropertyPreMeta(ItemMeta itemMeta) {

    }

    @Override
    public void handlePropertyPostMeta(ItemStack item) {
        if(ItemManager.getInteractEventsFromItem(uuid, ActionType.RIGHT_CLICK).contains("parry")) {
            Consumable consumable = Consumable.consumable().consumeSeconds(72000).hasConsumeParticles(false).animation(ItemUseAnimation.BLOCK).build();
            item.setData(DataComponentTypes.CONSUMABLE, consumable);
        } else if(ItemManager.getInteractEventsFromItem(uuid,ActionType.RIGHT_CLICK).contains("power_attack")) {
            Consumable consumable = Consumable.consumable().consumeSeconds(72000).hasConsumeParticles(false).animation(ItemUseAnimation.TRIDENT).build();
            item.setData(DataComponentTypes.CONSUMABLE, consumable);
        } else if(ItemManager.getInteractEventsFromItem(uuid, ActionType.RIGHT_CLICK).contains("smoke_pipe") || ItemManager.getInteractEventsFromItem(uuid, ActionType.RIGHT_CLICK).contains("instrument")) {
            Consumable consumable = Consumable.consumable().consumeSeconds(72000).hasConsumeParticles(false).animation(ItemUseAnimation.TOOT_HORN).build();
            item.setData(DataComponentTypes.CONSUMABLE, consumable);
        } else if(ItemManager.getInteractEventsFromItem(uuid, ActionType.RIGHT_CLICK).contains("throw")) {
            Consumable consumable = Consumable.consumable().consumeSeconds(72000).hasConsumeParticles(false).animation(ItemUseAnimation.SPEAR).build();
            item.setData(DataComponentTypes.CONSUMABLE, consumable);
        }
    }
}
