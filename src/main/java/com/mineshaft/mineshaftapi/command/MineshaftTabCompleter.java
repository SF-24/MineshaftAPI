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

import com.mineshaft.mineshaftapi.MineshaftApi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MineshaftTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length==1) {
            return StringUtil.copyPartialMatches(args[0],List.of("reload","event","events"),new ArrayList<>());
        } else if(args.length==2) {
            if(args[0].equals("reload")) {
                return StringUtil.copyPartialMatches(args[1],List.of("all","items","item","event","events","config","configs"),new ArrayList<>());
            } else if(args[0].equals("event")||args[0].equals("events")) {
                ArrayList<String> events = new ArrayList<>();
                events.add("list");
                events.add("trigger");
                return StringUtil.copyPartialMatches(args[1], events,new ArrayList<>());
            }
        } else if(args.length==3) {
            if(args[1].equals("trigger") && (args[0].equals("event")||args[0].equals("events"))) {
                ArrayList<String> events = MineshaftApi.getInstance().getEventManagerInstance().getEventList();
                return StringUtil.copyPartialMatches(args[2], events,new ArrayList<>());
            }
        }
        return null;
    }

}
