/*
 *     This program is a Minecraft plugin developed in Java for the Spigot API.
 *     It adds multiple RPG features intended for Multiplayer gameplay.
 *
 *     Copyright (C) 2024  Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.mineshaft.mineshaftapi.util;

import com.mineshaft.mineshaftapi.MineshaftApi;

import java.util.logging.Level;

public class Logger {

    public static void log(Level level, String text) {
        MineshaftApi.getPlugin(MineshaftApi.class).getLogger().log(level, text);
    }

    public static void logInfo(String text) {
        log(Level.INFO, text);
    }

    public static void logWarning(String text) {
        log(Level.WARNING, text);
    }

    public static void logConfig(String text) {
        log(Level.CONFIG, text);
    }

    public static void logError(String text) {
        log(Level.SEVERE, text);
    }
}
