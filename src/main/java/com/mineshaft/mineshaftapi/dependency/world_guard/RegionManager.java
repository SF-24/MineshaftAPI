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
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegionManager {

    YamlConfiguration config;
    File file;

    // Format: Wg Region ID | Town Name
    HashMap<String, String> worldGuardTownRegionList = new HashMap<>();

    // Format: Town ID | Town Class
    HashMap<String, Town> townClassCache = new HashMap<>();

    // Town cache in the form: "Region Name" : "Town Name"
    @Getter
    HashMap<String, ArrayList<String>> townIdCache = new HashMap<>();

    public RegionManager() {
        file = new File(MineshaftApi.getInstance().getDataFolder(), "regions.yml");
        if(!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                yamlConfiguration.set("Bree-land.towns.Bree.size",2);
                yamlConfiguration.set("Bree-land.towns.Bree.regions", List.of("bree"));
                yamlConfiguration.set("Bree-land.towns.Archet.size", 1);
                yamlConfiguration.set("Bree-land.towns.Bree.regions", List.of("archet"));
                yamlConfiguration.set("Bree-land.towns.Staddle.size", 1);
                yamlConfiguration.set("Bree-land.towns.Staddle.regions", List.of("staddle"));
                yamlConfiguration.save(file);
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
        townIdCache.clear();
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
            ArrayList<String> towns = new ArrayList<>();
            // Detects towns
            for(String townId : config.getConfigurationSection(region+".towns").getKeys(false)) {
                try {
                    int size = config.getInt(region + ".towns." + townId + ".size");
                    townClassCache.put(townId, new Town(townId,region,config.getStringList(region + ".towns." + townId + ".regions"),size));
                } catch (NumberFormatException e) {
                    townClassCache.put(townId, new Town(townId,region,config.getStringList(region + ".towns." + townId + ".regions")));
                }


                towns.add(townId);
                for(String wgRegionId : config.getStringList(region + ".towns." + townId)) {
                    worldGuardTownRegionList.put(wgRegionId, townId);
                }
            }
            townIdCache.put(region, towns);
        }
    }

    // Method to check if a location is in a town
    public boolean isInTown(Location loc) {
        if(loc.getWorld()==null || !DependencyInit.hasWorldGuard()) return false;
        if(worldGuardTownRegionList.isEmpty()) return false;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        ApplicableRegionSet applicableRegions = container.get(BukkitAdapter.adapt(loc.getWorld())).getApplicableRegions(BlockVector3.at((int) loc.getX(), (int) loc.getY(), (int) loc.getZ()), RegionQuery.QueryOption.NONE);
        for (ProtectedRegion region : applicableRegions.getRegions()) {
            return isRegionInTown(region);
        }
        return false;
    }

    public Town getTown(ProtectedRegion region) {
        if(!isRegionInTown(region)) return null;
        if(worldGuardTownRegionList.containsKey(region.getId())) {
            return townClassCache.get(worldGuardTownRegionList.get(region.getId()));
        }

        return null;
    }

    public boolean isRegionInTown(ProtectedRegion region) {
        return worldGuardTownRegionList.containsKey(region.getId());
    }

    public int getTownCount() {
        return townClassCache.size();
    }

}
