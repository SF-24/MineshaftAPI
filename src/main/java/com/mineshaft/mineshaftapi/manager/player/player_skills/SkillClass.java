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

public class SkillClass {

    // variable
    int level;
    int xp;
    int proficiencyLevel;

    PlayerSkills skill;

    public SkillClass(PlayerSkills skill) {
        this.skill=skill;
    }

    public int getLevel() {
        return level;
    }
    public int getXp() {
        return xp;
    }
    public int getProficiencyLevel() {return proficiencyLevel;}
    public boolean isProficient() {return proficiencyLevel>0;}
    public boolean isMastery() {return proficiencyLevel>1;}
    public void setXp(int xp) {
        this.xp=xp;
    }
    public void setLevel(int level) {
        this.level=level;
    }
    public void setProficiencyLevel(int proficiencyLevel) {this.proficiencyLevel=proficiencyLevel;}
}
