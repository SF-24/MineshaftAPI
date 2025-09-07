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

package com.mineshaft.mineshaftapi.manager.player.json;

import com.mineshaft.mineshaftapi.dependency.beton_quest.quest_management.QuestObject;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class QuestDataClass {

    HashMap<String, QuestObject> quests = new HashMap<>();

    public QuestObject getQuest(String id) {
        return quests.get(id);
    }

    public void removeQuest(String questId) {
        quests.remove(questId);
    }

    public void addQuest(String questId, QuestObject questObject) {
        quests.put(questId, questObject);
    }

    public boolean hasQuest(String questId) {
        return quests.containsKey(questId);
    }

}
