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

package com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management;

import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.item.item_properties.ItemAmmunitionManager;
import lombok.Getter;
import lombok.Setter;
import org.betonquest.betonquest.conversation.ChatConvIO;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class QuestObject {

    protected boolean usePlaceholderApi = true;

    protected String name = "";
    protected String description = "";

    protected List<String> objectives = new ArrayList<>();

    @Setter
    protected QuestStatus status = QuestStatus.ACTIVE;
    private final QuestEventsObject eventObject;

    public QuestObject(String name, String description, List<String> objectives, QuestEventsObject eventObject) {
        this.name=name;
        this.description=description;
        this.objectives=objectives;
        this.eventObject=eventObject;
    }

    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(status.getColour() + name);
        itemMeta.setLore(Collections.singletonList(ChatColor.GRAY + description));
        return itemStack;
    }
}
