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

package com.mineshaft.mineshaftapi.dependency.world_guard;

import com.google.common.base.Charsets;
import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.dependency.DependencyInit;
import com.mineshaft.mineshaftapi.util.Logger;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class RegionManager {

    YamlConfiguration config;
    File file;

    ArrayList<String> townRegionList = new ArrayList<>();

    // Town cache in the form: "Region/Country" : "Town"
    HashMap<String, ArrayList<Town>> townCache = new HashMap<>();

    public RegionManager() {
        file = new File(MineshaftApi.getInstance().getDataFolder(), "regions.yml");
        if(!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                Logger.logError("Could not create file regions.yml");
            }
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(this.file);
        InputStream defConfigStream = MineshaftApi.getInstance().getResource("regions.yml");
        if (defConfigStream != null) {
            this.config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        }
        townCache.clear();
        initialiseRegions();
    }

    public void initialiseRegions() {
        if(!DependencyInit.hasWorldGuard()) {
            if(!MineshaftApi.getInstance().getConfigManager().disableDependencyWarnings()) {
                Logger.logWarning("WorldGuard has not been detected. Due to this, region compatibility features will be disabled. All other features will work as intended.");
            } return;
        }

        if(config.getDefaultSection()==null) {
            Logger.logError("Default section not detected in the region configuration file. Cannot load regions.");
            return;
        }

        // Detects main regions
        for(String region : config.getDefaultSection().getKeys(false)) {
            ArrayList<Town> towns = new ArrayList<>();
            // Detects towns
            for(String town : config.getConfigurationSection(region+".towns").getKeys(false)) {
                towns.add(new Town(town.replace("_","").replace("-",""),config.getStringList(region + ".towns." + town)));
                townRegionList.addAll(config.getStringList(region + ".towns." + town));
            }
            townCache.put(region, towns);
        }
    }

    // Method to check if a location is in a town
    public boolean isInTown(Location loc) {
        if(loc.getWorld()==null || !DependencyInit.hasWorldGuard()) return false;
        if(townRegionList.isEmpty()) return false;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        ApplicableRegionSet applicableRegions = container.get(BukkitAdapter.adapt(loc.getWorld())).getApplicableRegions(new BlockVector3((int) loc.getX(), (int) loc.getY(), (int) loc.getZ()), RegionQuery.QueryOption.NONE);
        for (ProtectedRegion region : applicableRegions.getRegions()) {
            if(townRegionList.contains(region.getId())) {
                return true;
            }
        }
        return false;
    }

}
