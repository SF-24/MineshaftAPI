package com.mineshaft.mineshaftapi.text;

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
