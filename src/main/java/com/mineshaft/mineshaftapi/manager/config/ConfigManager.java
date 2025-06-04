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

package com.mineshaft.mineshaftapi.manager.config;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Objects;

public class ConfigManager {

    public ConfigManager(MineshaftApi mineshaftApi) {
        mineshaftApi.getConfig().options().copyDefaults();;
        mineshaftApi.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return MineshaftApi.getInstance().getConfig();
    }

    public boolean getSidebarEnabled() {
        return getConfig().getBoolean("enable-sidebar");
    }

    public boolean disableDependencyWarnings() {
        return getConfig().getBoolean("disable-dependency-warnings");
    }

    public HashMap<Material, Integer> getItemConstituents(Material material) {
        HashMap<Material, Integer> itemAliases = new HashMap<>();

        if(!getConfig().contains("materials."+material.name().toLowerCase())) {
            return new HashMap<>();
        }

        // If exists, get the values
        for(String mat : getConfig().getConfigurationSection("materials." + material.name().toLowerCase()).getKeys(false)) {
            if(mat.equalsIgnoreCase(material.name())) {
                 try {
                    int number = Integer.parseInt(Objects.requireNonNull(getConfig().getString("materials." + material.name().toLowerCase() + mat)));
                    Material alias = Material.valueOf(mat.toUpperCase());
                    itemAliases.put(alias, number);
                 } catch (NumberFormatException ex) {
                     Logger.logError("Invalid number key declared in configuration for material: " + mat);
                 } catch (IllegalArgumentException e) {
                    Logger.logError("Invalid material key declared in configuration for material: " + mat);
                }

            }
        }
        return itemAliases;
    }


    public int getDefaultUnsmeltingTime() {
        return getConfig().getInt("default-unsmelting-time");
    }

    public boolean enableFurnaceMelting() {
        return getConfig().getBoolean("add-unsmelting-recipes-to-furnace");
    }
    public boolean enableBlastFurnaceMelting() {
        return getConfig().getBoolean("add-unsmelting-recipes-to-blast-furnace");
    }
    public boolean enableSmokerMelting() {
        return getConfig().getBoolean("add-unsmelting-recipes-to-smoker");
    }
    public boolean enableCampfireMelting() {
        return getConfig().getBoolean("add-unsmelting-recipes-to-campfire");
    }

    public boolean useVault() {
        return getConfig().getBoolean("enable-vault");
    }

    public boolean useItalicItemRarity() {
        return getConfig().getBoolean("italic-item-rarity");
    }

    public boolean enableItemCooldownAnimation() {
        return getConfig().getBoolean("enable-cooldown-animation");
    }

    public void reloadConfigs() {
        MineshaftApi.getInstance().reloadConfig();
    }


}
