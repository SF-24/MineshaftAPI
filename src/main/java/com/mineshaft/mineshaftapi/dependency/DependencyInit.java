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

package com.mineshaft.mineshaftapi.dependency;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.dependency.beton_quest.events.BetonDisplayQuestEventFactory;
import com.mineshaft.mineshaftapi.dependency.beton_quest.events.BetonExperienceEventFactory;
import com.mineshaft.mineshaftapi.dependency.beton_quest.events.BetonRemoveQuestEventFactory;
import com.mineshaft.mineshaftapi.dependency.mythic_mob.MythicListener;
import com.mineshaft.mineshaftapi.util.Logger;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.quest.PrimaryServerThreadData;
import org.bukkit.Bukkit;

public class DependencyInit {

//    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DependencyInit.class);
    VaultDependency vaultDependency = null;

    public void initialiseDependencies() {
        if(hasBetonQuest()) {
            BetonQuestLoggerFactory loggerFactory = MineshaftApi.getInstance().getServer().getServicesManager().load(BetonQuestLoggerFactory.class);
            PrimaryServerThreadData data = new PrimaryServerThreadData(Bukkit.getServer(), Bukkit.getScheduler(), BetonQuest.getInstance());

            BetonQuest.getInstance().getQuestRegistries().event().register("mineshaftxp", new BetonExperienceEventFactory(loggerFactory,data));
            BetonQuest.getInstance().getQuestRegistries().event().register("addquest", new BetonDisplayQuestEventFactory(loggerFactory, data));
            BetonQuest.getInstance().getQuestRegistries().event().register("remquest", new BetonRemoveQuestEventFactory(loggerFactory, data));

        } else {
            Logger.logWarning("BetonQuest is not enabled. Plugin compatibility features have been disabled.");
        }
        if (hasPlaceholderAPI()) {
            // Register placeholders
            new MineshaftPlaceholderExpansion(MineshaftApi.getInstance()).register();
        } else {
            // Log warning
            Logger.logWarning("PlaceholderAPI is not installed. While this plugin is not required, however some functionality will be disabled");
        }
        if (MineshaftApi.getInstance().getConfigManager().useVault() && hasVault()) {
            vaultDependency = new VaultDependency();
        } else {
            if (MineshaftApi.getInstance().getConfigManager().disableDependencyWarnings() || !MineshaftApi.getInstance().getConfigManager().useVault()) {
                Logger.logInfo("Vault has been disabled in the config. Due to this, some functionality and compatibility features will be disabled");
            } else {
                // Log warning
                Logger.logWarning("Vault is not installed. While this plugin is not required, some functionality and compatibility features will be disabled");
            }
        }
        if (DependencyInit.hasMythicMobs()) {
            // Register placeholders
            Logger.logInfo("Loaded MythicMobs integration");
            Bukkit.getPluginManager().registerEvents(new MythicListener(), MineshaftApi.getInstance());
        } else {
            // Log warning
            Logger.logWarning("MythicMobs is not installed. Integration has not been enabled");
        }
    }

    public static boolean hasPlaceholderAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static boolean hasAriKeys() {
        return Bukkit.getPluginManager().getPlugin("AriKeys") != null && Bukkit.getPluginManager().isPluginEnabled("AriKeys");
    }

    public static boolean hasVault() {
        return Bukkit.getPluginManager().getPlugin("Vault") != null && Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    public static boolean hasMythicMobs() {
        return Bukkit.getPluginManager().getPlugin("MythicMobs") != null || Bukkit.getPluginManager().isPluginEnabled("MythicMobs");
    }

    public static boolean hasBetonQuest() {
        return Bukkit.getPluginManager().getPlugin("BetonQuest") != null || Bukkit.getPluginManager().isPluginEnabled("BetonQuest");
    }

    public static boolean hasWorldGuard() {
        return Bukkit.getPluginManager().getPlugin("WorldGuard") != null || Bukkit.getPluginManager().isPluginEnabled("WorldGuard");
    }

    public static boolean hasLeadWires() {
        return Bukkit.getPluginManager().getPlugin("LeadWires") != null || Bukkit.getPluginManager().isPluginEnabled("LeadWires");
    }

    public VaultDependency getVault() {return vaultDependency;}
}
