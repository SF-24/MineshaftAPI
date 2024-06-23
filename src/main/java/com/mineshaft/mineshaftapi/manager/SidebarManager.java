package com.mineshaft.mineshaftapi.manager;

import com.mineshaft.mineshaftapi.manager.json.JsonPlayerBridge;
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

        Objective obj = board.registerNewObjective("skyblock","dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "SKYBLOCK");


        int coins = JsonPlayerBridge.getCoins(player);
        String location = "Village";

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
        coinsTeam.setPrefix(ChatColor.WHITE + " Purse: " + ChatColor.GOLD);
        coinsTeam.setSuffix(String.valueOf(coins));

        locationTeam.addEntry(ChatColor.DARK_PURPLE.toString());
        locationTeam.setPrefix(ChatColor.WHITE + " ⏣ " + ChatColor.AQUA);
        locationTeam.setSuffix(location);

        objectiveTeam.addEntry(ChatColor.BLUE.toString());
        objectiveTeam.setPrefix(ChatColor.YELLOW + " ");
        objectiveTeam.setSuffix(objective);

        ArrayList<String> sidebar = new ArrayList<>();

        // Scoreboard
        sidebar.add( ChatColor.GRAY + dateToString + ChatColor.DARK_GRAY + " mini69420A");
        sidebar.add("");
        sidebar.add(ChatColor.WHITE + " Early spring 1st");
        sidebar.add(ChatColor.YELLOW + " ☀ " + ChatColor.GRAY + "9:30am");

        // Location entry
        sidebar.add(ChatColor.DARK_PURPLE.toString());
        sidebar.add(" ");

        // Purse -> coin display
        sidebar.add(ChatColor.GOLD.toString());

        if(objective!=null && !objective.equals("")) {
            sidebar.add("  ");
            sidebar.add(ChatColor.WHITE + " Current quest");
            sidebar.add(ChatColor.BLUE.toString());
        }
        sidebar.add("   ");

        // Website link
        sidebar.add(ChatColor.YELLOW + "kc.shockbyte.pro");

        int lines = sidebar.size();

        int s = 0;
        for(int i=lines-1; lines>=0; lines--) {
            Score score = obj.getScore(sidebar.get(i));
            score.setScore(s);
            player.sendMessage(sidebar.get(i));
            player.sendMessage(String.valueOf(i));
            s++;
        }



        player.setScoreboard(board);
    }

    public static void updateCoins(Player player) {
        setTeamSuffix(player, "coins", String.valueOf(JsonPlayerBridge.getCoins(player)));
    }

    public static void updateLocation(Player player, String location) {
        setTeamSuffix(player, "location", location);
    }

    public static void updateObjective(Player player, String objective) {
        setTeamSuffix(player, "objective", objective);
    }

    public static void setTeamSuffix(Player player, String team, String value) {
        player.getScoreboard().getTeam(team).setSuffix(value);
    }


}
