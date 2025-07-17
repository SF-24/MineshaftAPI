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

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class SettingsDataClass extends JsonSaveObject {

    // Ability combos
    private HashMap<String, String> combos = new HashMap<>();

    // Spell hot-bars
    @Getter
    private HashMap<Integer, String> spellHotbars = new HashMap<>();

    // 0,1,2 for 3 hotbars
    @Getter @Setter
    private int currentSpellHotbar = 0;

    // Hotbar width = 8

    // [0,1,2,3,4,5,6,7] [8,9,10,11,12,13,14,15]

    public void setSpell(int hotbar, int slot, String spell) {
        removeSpell(spell);
        spellHotbars.put((hotbar*8)+slot, spell);
    }

    public String getSpell(int hotbar, int slot) {
        return spellHotbars.get((hotbar*8)+slot);
    }

    public int getBindNumber(String spell) {
        for(int slot : spellHotbars.keySet()) {
            if(spellHotbars.get(slot).equals(spell)) {
                return slot;
            }
        }
        return -9999;
    }

    public int getSlot(String spell) {
        int slot = getBindNumber(spell);
        while(slot>8) slot=slot-8;
        return slot;
    }

    public int getHotbar(String spell) {
        if(getSlot(spell)<0) return -99999;
        return getBindNumber(spell)/8;
    }

    public void removeSpell(String spell) {
        spellHotbars.remove(getSlot(spell));
    }

    public void removeSpell(int hotbar, int slot) {
        spellHotbars.remove((hotbar*8)+slot);
    }

    public void clearSpellHotbar(int hotbar) {
        for(int i = 0; i < 8; i++) {
            spellHotbars.remove((hotbar*8)+i);
        }
    }

    // Click abilities

    public String getAbility(String clicks) {
        if(combos.containsKey(clicks)) {
            return combos.get(clicks);
        }
        return null;
    }

    public void setCombo(String ability, String clicks) {
        combos.put(clicks,ability);
    }

//    public ArrayList<ClickType> getCombo(String ability) {return combos.get(ability);}

    public void removeCombo(String ability) {
        for(String element : combos.keySet()) {
            if(combos.get(element).equalsIgnoreCase(ability)) {
                combos.remove(element);
            }
        }
    }

    public HashMap<String, String> getCombos() {
        HashMap<String, String> returnValue = new HashMap<>();
        for(String element : combos.keySet()) {
            returnValue.put(element, combos.get(element));
        }
        return returnValue;
    }

    public void clearCombos() {combos.clear();}
}
