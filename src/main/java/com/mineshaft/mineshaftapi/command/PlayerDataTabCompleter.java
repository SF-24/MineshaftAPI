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

package com.mineshaft.mineshaftapi.command;

import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerDataTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if(args.length==0) return Collections.emptyList();

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null && args.length>1) {
            return Collections.emptyList();
        }

        if(args.length==2) {
            return StringUtil.copyPartialMatches(args[1],List.of("get","set","add","remove"),new ArrayList<>());
        } else if(args.length==3) {
            if(args[1].equalsIgnoreCase("get")) {
                return StringUtil.copyPartialMatches(args[2],List.of("armour_class","ac","experience","ability"),new ArrayList<>());
            } else if(args[1].equalsIgnoreCase("set")) {
                return StringUtil.copyPartialMatches(args[2],List.of("experience"),new ArrayList<>());
            } else if(args[1].equalsIgnoreCase("remove")) {
                return StringUtil.copyPartialMatches(args[2],List.of("ability"),new ArrayList<>());
            } else if(args[1].equalsIgnoreCase("add")) {
                return StringUtil.copyPartialMatches(args[2],List.of("experience","ability"),new ArrayList<>());
            }
        } else if(args.length==4) {
            if(args[2].equalsIgnoreCase("ability")) {
                if(args[1].equalsIgnoreCase("add")) {
                    return StringUtil.copyPartialMatches(args[3], JsonPlayerBridge.getUnknownAbilities(player),new ArrayList<>());
                } else if(args[1].equalsIgnoreCase("remove")) {
                    return StringUtil.copyPartialMatches(args[3], JsonPlayerBridge.getAbilities(player).keySet(),new ArrayList<>());
                } else {
                    return StringUtil.copyPartialMatches(args[3],List.of(),new ArrayList<>());
                }
            } else {
                return StringUtil.copyPartialMatches(args[3],List.of(),new ArrayList<>());
            }
        } else if(args.length>=5) {
            return StringUtil.copyPartialMatches(args[3],List.of(),new ArrayList<>());
        }

        return null;
    }
}
