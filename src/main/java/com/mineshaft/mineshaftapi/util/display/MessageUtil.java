/*
 * Copyright (c) 2026. Sebastian Frynas
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

package com.mineshaft.mineshaftapi.util.display;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void sendColouredText(Player player, String text, NamedTextColor colour) {
        player.sendMessage(Component.text(text,colour));
    }

    public static void sendColouredText(CommandSender sender, String text, NamedTextColor colour) {
        sender.sendMessage(Component.text(text,colour));
    }

    public static void sendPlainText(Player player, String text) {
        player.sendMessage(Component.text(text,NamedTextColor.WHITE));
    }

    public static void sendPlainText(CommandSender sender, String text) {
        sender.sendMessage(Component.text(text,NamedTextColor.WHITE));
    }

    public static void sendErrorText(Player player, String text) {
        sendColouredText(player,text,NamedTextColor.RED);
    }

    public static void sendErrorText(CommandSender sender, String text) {
        sendColouredText(sender,text,NamedTextColor.RED);
    }
}
