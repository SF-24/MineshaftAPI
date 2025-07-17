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

package com.mineshaft.mineshaftapi.manager.player.spells;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpellClass {

    public SpellClass () {
        this.level=1;
        this.progress=0;
        this.learned=false;
    }

    public SpellClass(int level) {
        this.level=level;
        this.progress=0;
        this.learned=false;
    }

    public SpellClass(int level, int learningProgress) {
        this.level=level;
        this.progress=learningProgress;
        this.learned =false;
    }

    public SpellClass(int level, int progress, boolean hasLearned) {
        this.level=level;
        this.progress=progress;
        this.learned =hasLearned;
    }

    public SpellClass(int level, boolean hasLearned) {
        this.level=level;
        this.progress=0;
        this.learned =hasLearned;
    }

    public SpellClass(boolean hasLearned) {
        this.level=1;
        this.progress=0;
        this.learned =hasLearned;
    }

    boolean learned;
    int level;
    int progress;

    public void addProgress(int progress) {
        this.progress+=progress;
    }

    public void addLevel(int level) {
        this.level+=level;
    }
}
