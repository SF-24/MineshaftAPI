package com.mineshaft.mineshaftapi.dependency;

import com.mineshaft.mineshaftapi.MineshaftApi;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class MineshaftPlaceholderExpansion extends PlaceholderExpansion {

    private final MineshaftApi plugin;

    public MineshaftPlaceholderExpansion(MineshaftApi plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "mineshaft";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    // If not set to true, expansion will be disabled on plugin reload
    @Override
    public boolean persist() {
        return true;
    }

    // Request placeholders
    // TODO: Make code actually do something
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("placeholder1")) {
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }

        if (params.equalsIgnoreCase("placeholder2")) {
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }
        return null; //
    }

}
