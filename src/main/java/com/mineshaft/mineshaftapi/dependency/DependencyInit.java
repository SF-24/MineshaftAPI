package com.mineshaft.mineshaftapi.dependency;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.text.Logger;
import org.bukkit.Bukkit;

public class DependencyInit {

    public void initialiseDependencies() {

        if (hasPlaceholderAPI()) {
            // Register placeholders
            new MineshaftPlaceholderExpansion(MineshaftApi.getInstance()).register();
        } else {
            // Log warning
            Logger.logWarning("PlaceholderAPI is not installed. While this plugin is not required, some functionality will be disabled");
        }

    }

    public static boolean hasPlaceholderAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

}
