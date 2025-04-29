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
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    public ConfigManager(MineshaftApi mineshaftApi) {
        mineshaftApi.getConfig().options().copyDefaults();
        mineshaftApi.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return MineshaftApi.getInstance().getConfig();
    }

    public boolean getSidebarEnabled() {
        return getConfig().getBoolean("enable-sidebar");
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
