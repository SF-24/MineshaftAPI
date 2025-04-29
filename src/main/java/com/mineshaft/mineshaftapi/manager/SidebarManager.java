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

package com.mineshaft.mineshaftapi.manager;

import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.util.NumericFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SidebarManager {

    // TODO: to be updated with better system

    @Deprecated
    public static void displayScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = board.registerNewObjective("mineshaft","dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "MINESHAFT");

        String coinsLang = "Credits";

        int coins = JsonPlayerBridge.getCoins(player);
        String location = "Bespin";

        String objective = null;
        if(objective==null) objective = "";

        String dateToString = new SimpleDateFormat("dd/MM/yy").format(Calendar.getInstance().getTime());

        // Teams
        Team coinsTeam = board.registerNewTeam("coins");
        Team locationTeam = board.registerNewTeam("location");
        Team objectiveTeam = board.registerNewTeam("objective");
        Team inGameDateTeam = board.registerNewTeam("in_game_date");
        Team currentDate = board.registerNewTeam("current_date");

        coinsTeam.addEntry(ChatColor.GOLD.toString());
        coinsTeam.setPrefix(ChatColor.WHITE.toString() + " " + coinsLang + ": ");
        coinsTeam.setSuffix(ChatColor.GREEN+NumericFormatter.formatNumberAdvanced(coins));

        locationTeam.addEntry(ChatColor.DARK_PURPLE.toString());
        locationTeam.setPrefix(ChatColor.WHITE + " ⏣ ");
        locationTeam.setSuffix(ChatColor.AQUA + location);

        objectiveTeam.addEntry(ChatColor.BLUE.toString());
        objectiveTeam.setPrefix(" ");
        objectiveTeam.setSuffix(ChatColor.YELLOW + objective);

        ArrayList<String> sidebar = new ArrayList<>();

        // Scoreboard
        sidebar.add( ChatColor.GRAY + dateToString + ChatColor.DARK_GRAY + " mini69420A");
        sidebar.add("");



        sidebar.add(ChatColor.WHITE + " Early summer 1st");
        sidebar.add(ChatColor.YELLOW + " ☀ " + ChatColor.GRAY + "9:30am");

        // Location entry
        sidebar.add(ChatColor.DARK_PURPLE.toString());
        sidebar.add(" ");

        // Purse -> coin display
        sidebar.add(ChatColor.GOLD.toString());

        if(!objective.isEmpty()) {
            sidebar.add("  ");
            sidebar.add(ChatColor.WHITE + " Current quest");
            sidebar.add(ChatColor.BLUE.toString());
        }
        sidebar.add("   ");

        // Website link
        sidebar.add(ChatColor.YELLOW + "kc.shockbyte.pro");

        int lines = sidebar.size();

        int s = 0;
        for(int i=lines-1; i>=0; i--) {
            Score score = obj.getScore(sidebar.get(i));
            score.setScore(s);
            //player.sendMessage(sidebar.get(i));
            //player.sendMessage(s + " | " + i);
            s++;
        }

        player.setScoreboard(board);
    }

    public static void updateCoins(Player player) {
        setTeamSuffix(player, "coins", ChatColor.GREEN + NumericFormatter.formatNumberAdvanced(JsonPlayerBridge.getCoins(player)));
    }

    public static void updateLocation(Player player, String location) {
        setTeamSuffix(player, "location", ChatColor.AQUA + location);
    }

    public static void updateObjective(Player player, String objective) {
        setTeamSuffix(player, "objective", ChatColor.YELLOW + objective);
        displayScoreboard(player);
    }

    public static void setTeamSuffix(Player player, String team, String value) {
        player.getScoreboard().getTeam(team).setSuffix(value);
    }


}
