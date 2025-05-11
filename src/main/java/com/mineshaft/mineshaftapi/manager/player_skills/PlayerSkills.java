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

package com.mineshaft.mineshaftapi.manager.player_skills;

public enum PlayerSkills {
    ATHLETICS("Athletics", "STR"),

    ACROBATICS("Acrobatics", "DEX"),
    SLEIGHT_OF_HAND("Sleight of Hand", "DEX"),
    STEALTH("Stealth", "DEX"),

    HISTORY("History", "DEX"),
    INVESTIGATION("Investigation", "DEX"),
    NATURE("Nature", "DEX"),

    LORE("Lore", "INT"),
    SHADOW_LORE("Shadow Lore", "INT"),
    TRADITIONS("Traditions", "INT"),
    //RIDDLES("Riddles", "INT")

    ANIMAL_HANDLING("Animal Handling", "WIS"),
    INSIGHT("Insight", "WIS"),
    MEDICINE("Medicine", "WIS"),
    PERCEPTION("Perception", "WIS"),
    SURVIVAL("Survival", "WIS"),

    DECEPTION("Deception", "CHA"),
    INTIMIDATION("Intimidation", "CHA"),
    PERFORMANCE("Performance", "CHA"),
    PERSUASION("Persuasion", "CHA")

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
