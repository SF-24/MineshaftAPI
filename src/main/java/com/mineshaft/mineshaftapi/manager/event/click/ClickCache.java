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

package com.mineshaft.mineshaftapi.manager.event.click;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ClickCache {

    HashMap<UUID, ArrayList<ClickType>> clicks = new HashMap<>();
    private Cache<UUID, Long> activeTime = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build();
    private Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(100, TimeUnit.MILLISECONDS).build();

    private HashMap<UUID, ArrayList<ClickType>> setClicks = new HashMap<>();

    public boolean hasActiveClicks(Player player) {
        if(clicks.containsKey(player.getUniqueId()) && activeTime.asMap().containsKey(player.getUniqueId())) {
            return true;
        }

        return false;
    }

    // Combo click
    public void cacheClick(Player player, ClickType clickType) {
        UUID uuid = player.getUniqueId();
        ArrayList<ClickType> clickTypes = new ArrayList<>();
        if(hasActiveClicks(player)) {
            clickTypes = clicks.get(uuid);

            if(clickTypes.size() > 5) {
                clickTypes = new ArrayList<>();
            }

        }
        activeTime.put(player.getUniqueId(), System.currentTimeMillis() + 5000 );
        clickTypes.add(clickType);
        clicks.put(uuid,clickTypes);

        onUpdate(player);
    }

    public void onUpdate(Player player) {
        ArrayList<ClickType> clickTypes = new ArrayList<>();

        if(clicks.containsKey(player.getUniqueId())) {
            clickTypes = clicks.get(player.getUniqueId());
        }

        // Generate string
        StringBuilder actionBar = new StringBuilder();
        int elements = 0;

        for(ClickType click: clickTypes) {
            if(elements < 1) {
                actionBar = new StringBuilder(String.valueOf(click.getAbbreviation()));
            } else {
                actionBar.append("-").append(click.getAbbreviation());
            }
            elements++;
        }

        player.sendActionBar(Component.text(actionBar.toString(), NamedTextColor.GREEN));
        testAbilities(player);
    }

    // Get click list. Clean if necessary
    public ArrayList<ClickType> getKeys(Player player) {
        ArrayList<ClickType> clickTypes = new ArrayList<>();

        if(hasActiveClicks(player)) {
            clickTypes = clicks.get(player.getUniqueId());
            if(clickTypes.size() > 5) {
                clickTypes = new ArrayList<>();
            }
        }
        return clickTypes;
    }

    // Get the keys as a string
    public String getKeysAsString(Player player) {
        ArrayList<ClickType> clickTypes = getKeys(player);
        int elements = 0;
        StringBuilder clickList = new StringBuilder();

        for(ClickType click: clickTypes) {
            if(elements < 1) {
                clickList = new StringBuilder(String.valueOf(click.getAbbreviation()));
            } else {
                clickList.append(click.getAbbreviation());
            }
            elements++;
        }
        return clickList.toString();
    }



    public void testAbilities(Player player) {
        // Get the combos
        HashMap<String, String> combos = new HashMap<>();

        for(String key : combos.keySet()) {
            if(key.equalsIgnoreCase(getKeysAsString(player))) {
                // If combo is the one triggered, execute it and send a message
                // TODO:
                player.sendActionBar(Component.text(combos.get(key),NamedTextColor.WHITE, TextDecoration.BOLD));
            }
        }
    }

    public void clearClicks(Player player) {
        clicks.remove(player.getUniqueId());
        activeTime.asMap().remove(player.getUniqueId());
    }


    public void addSetLeftClick(Player player) {
        ArrayList<ClickType> clickList = getSetClicks(player);

        clickList.add(ClickType.LEFT);

        setClicks.put(player.getUniqueId(),clickList);
    }

    public void addSetRightClick(Player player) {
        ArrayList<ClickType> clickList = getSetClicks(player);

        clickList.add(ClickType.RIGHT);

        setClicks.put(player.getUniqueId(),clickList);
    }

    public void resetSetClicks(Player player) {
        setClicks.put(player.getUniqueId(),new ArrayList<>());
    }

    public boolean saveSetClicks(Player player, String ability) {
        ArrayList<ClickType> clicks = getSetClicks(player);

        // TODO:

        return false;
    }

    // Get the player click array
    public ArrayList<ClickType> getSetClicks(Player player) {
        if(setClicks.containsKey(player.getUniqueId())) {
            return setClicks.get(player.getUniqueId());
        }

        return new ArrayList<>();
    }

    // Get the player click array as a string
    public String getSetClicksAsString(Player player) {

        StringBuilder value = new StringBuilder();
        if(!getSetClicks(player).isEmpty()) {
            for(ClickType click : getSetClicks(player)) {
                if(click.equals(ClickType.LEFT)) {
                    value.append("L");
                } else if(click.equals(ClickType.RIGHT)) {
                    value.append("R");
                }
            }
            return value.toString();
        }

        return null;
    }

    // Get the player click array as a string list
    public ArrayList<String> getSetClicksAsStringList(Player player, String prefix) {
        ArrayList<String> clickStrings = new ArrayList<>();

        for(ClickType click : getSetClicks(player)) {
            if(click.equals(ClickType.LEFT)) {
                clickStrings.add(prefix + "Left");
            } else if(click.equals(ClickType.RIGHT)) {
                clickStrings.add(prefix + "Right");
            }
        }

        return clickStrings;
    }

    public ArrayList<String> getAbilityClicksAsString(Player player, String prefix, String ability) {
        // Get the clicks for a specific ability as a string
        // Used for UI
        return null;
    }

}
