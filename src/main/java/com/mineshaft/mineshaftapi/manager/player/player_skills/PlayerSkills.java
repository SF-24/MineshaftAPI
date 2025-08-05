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

package com.mineshaft.mineshaftapi.manager.player.player_skills;

import lombok.Getter;

@Getter
public enum PlayerSkills {

    // ??? Decreases cost of strength related abilities?
    ATHLETICS("Athletics", "STR"),

    // Decreases dodge, dash and leap skill cost. Decreases fall damage
    ACROBATICS("Acrobatics", "DEX"),

    // TODO: Implement stealth mechanics
    STEALTH("Stealth", "DEX"),

    // Will work with upcoming discovery system.
    // Double lore discovery EXP
    LORE_GREOGRAPHY("Lore (Geography)", "INT"),
    LORE_HISTORY("Lore (History)", "INT"),

    // ??? Some form of discoveries
    // Double shadow-lore discovery EXP
    SHADOW_LORE("Shadow Lore", "INT"),

    // Will work with upcoming discovery system
    // Double nature discovery EXP
    NATURE("Nature", "INT"),

    // DYNAMIC?
    SPELL_CASTING("Spell-casting", "INT"),

    // ??? To do with finding stuff?
    PERCEPTION("Perception", "WIS"),

    // Will somehow work with discoveries and crafting
    SURVIVAL("Survival", "WIS"),

    // Will work with npc interactions,
    PERSUASION("Persuasion", "CHA"),


    ;

    final String name;
    final String baseAbilityScore;

    PlayerSkills(String name, String baseAbilityScore) {
        this.name=name;
        this.baseAbilityScore=baseAbilityScore;
    }

}
