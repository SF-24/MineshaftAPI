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

import com.mineshaft.mineshaftapi.manager.player.ProfileManager;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class JsonSettingsBridge {

    public static int hotbarCount=3;
    int slotPerHotbar = 8;

    public static String getPath(Player player) {return ProfileManager.getProfilePathOfPlayer(player.getUniqueId(),JsonProfileBridge.getCurrentProfile(player));}
    public static String getFileName() {return "player_settings";}
    public static Class<?> getClassName() {return SettingsDataClass.class;}

    public static void saveFile(Player player, SettingsDataClass data) {
        new JsonSaveLoader(player,getPath(player),getFileName(), getClassName()).saveFile(data);
    }

    public static SettingsDataClass getData(Player player) {
        if(new JsonSaveLoader(player,getPath(player),getFileName(), getClassName()).loadData(player) instanceof SettingsDataClass) {
            return (SettingsDataClass) new JsonSaveLoader(player, getPath(player), "player_settings", SettingsDataClass.class).loadData(player);
        }
        return null;
    }

    /*
    * Spells
    * */

    public static void addSpell(Player player, String spell, int hotbar, int slot) {
        SettingsDataClass data = getData(player);
        if(data == null) {return;}
        data.setSpell(hotbar, slot, spell);
        saveFile(player, data);
    }

    public static void removeSpell(Player player, int hotbar, int slot) {
        SettingsDataClass data = getData(player);
        if(data == null) {return;}
        data.removeSpell(hotbar, slot);
        saveFile(player, data);
    }

    public static void clearSpellHotbar(Player player, int hotbar) {
        SettingsDataClass data = getData(player);
        if(data == null) {return;}
        data.clearSpellHotbar(hotbar);
        saveFile(player, data);
    }

    public static String getSpell(Player player, int hotbar, int slot) {
        SettingsDataClass data = getData(player);
        if(data == null) {return null;}
        return data.getSpell(hotbar, slot);
    }

    public static HashMap<Integer, String> getSpellHotbars(Player player) {
        if (getData(player) != null) {
            return getData(player).getSpellHotbars();
        }
        return null;
    }

    public static int getCurrentSpellHotbar(Player player) {
        if (getData(player) != null) {
            return getData(player).getCurrentSpellHotbar();
        }
        return -1;
    }

    public static void setCurrentSpellHotbar(Player player, int currentSpellHotbar) {
        SettingsDataClass data = getData(player);
        if(data == null) {return;}
        data.setCurrentSpellHotbar(currentSpellHotbar);
        saveFile(player, data);
    }

    public static int getSpellSlot(Player player, String spell) {
        SettingsDataClass data = getData(player);
        if(data == null) {return -10;}
        return data.getSlot(spell);
    }

    public static int getSpellHotbar(Player player, String spell) {
        SettingsDataClass data = getData(player);
        if(data == null) {return -10;}
        return data.getHotbar(spell);
    }

    /*
    * Abilities
    * */

    public static void addAbility(Player player, String ability, String combo) {
        if(ability==null) {
            Logger.logError("JsonSettingsBridge has detected a null ability");
            return;
        } else if(combo==null || combo.isEmpty()) {
            Logger.logError("Detected empty combo for ability " + ability);
            return;
        }
        SettingsDataClass data = getData(player);
        if(data == null) {return;}
        data.setCombo(ability, combo);
        saveFile(player, data);
    }

    public static void removeAbility(Player player, String ability) {
        SettingsDataClass data = getData(player);
        if(data == null) {return;}
        data.removeCombo(ability);
        saveFile(player, data);
    }

    public static String getAbility(Player player, String combo) {
        SettingsDataClass data = getData(player);
        if(data == null) {return null;}
        return data.getAbility(combo);
    }

    public static HashMap<String, String> getAbilities(Player player) {
        if(getData(player) != null) {return getData(player).getCombos();}
        return null;
    }

}
