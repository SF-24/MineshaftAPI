package com.mineshaft.mineshaftapi.manager;

import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.text.NumericFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SidebarManager {

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

        if(!objective.equals("")) {
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
