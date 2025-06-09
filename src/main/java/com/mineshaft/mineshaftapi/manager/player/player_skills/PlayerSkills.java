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

public enum PlayerSkills {

    // ??? Decreases cost of strength related abilities?
    ATHLETICS("Athletics", "STR"),

    // Decreases dodge, dash and leap skill cost. Decreases fall damage
    ACROBATICS("Acrobatics", "DEX"),

    // TODO: Implement stealth mechanics
    STEALTH("Stealth", "DEX"),

    // Will work with upcoming discovery system.
    // Double lore discovery EXP
    LORE("Lore", "INT"),

    // ??? Some form of discoveries
    // Double shadow-lore discovery EXP
    SHADOW_LORE("Shadow Lore", "INT"),

    // Will work with upcoming discovery system
    // Double nature discovery EXP
    NATURE("Nature", "INT"),

    // ??? To do with finding stuff?
    PERCEPTION("Perception", "WIS"),

    // Will somehow work with discoveries and crafting
    SURVIVAL("Survival", "WIS"),

    // Will work with npc interactions
    PERSUASION("Persuasion", "CHA"),

//    MEDICINE("Medicine", "WIS"),


//    TRADITIONS("Traditions", "INT"),
//
//    ANIMAL_HANDLING("Animal Handling", "WIS"),
//    INSIGHT("Insight", "WIS"),
//
//    DECEPTION("Deception", "CHA"),
//    INTIMIDATION("Intimidation", "CHA"),
//    PERFORMANCE("Performance", "CHA")

//    SLEIGHT_OF_HAND("Sleight of Hand", "DEX"),
//    RIDDLES("Riddles", "INT")
//    HISTORY("History", "DEX"),
//    INVESTIGATION("Investigation", "DEX"),

    ;

    final String name;
    final String baseAbilityScore;

    PlayerSkills(String name, String baseAbilityScore) {
        this.name=name;
        this.baseAbilityScore=baseAbilityScore;
    }

    public String getName() {return this.name;}
    public String getBaseAbilityScore() {return this.baseAbilityScore;}
}
