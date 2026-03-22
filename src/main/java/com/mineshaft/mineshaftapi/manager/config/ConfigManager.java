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
import com.mineshaft.mineshaftapi.manager.item.fields.ItemRarity;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemSubcategory;
import com.mineshaft.mineshaftapi.util.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConfigManager {

    MiniMessage mm = MiniMessage.miniMessage();

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

    public boolean useBoldItemRarity() {return getConfig().getBoolean("bold-item-rarity");}
    public boolean useCapitalisedItemRarity() {return getConfig().getBoolean("capitalised-item-rarity");}
    public boolean useWhiteCommonItemRarity() {return getConfig().getBoolean("white-common-item-rarity");}

    public ItemRarity getDefaultCustomItemRarity() {
        if(getConfig().getString("default-custom-item-rarity")==null) return ItemRarity.STANDARD;
        return ItemRarity.valueOf(Objects.requireNonNull(getConfig().getString("default-custom-item-rarity")).toUpperCase());
    }

    public ItemRarity getVanillaItemRarity() {
        if(getConfig().getString("vanilla-item-rarity")==null) return ItemRarity.STANDARD;
        return ItemRarity.valueOf(Objects.requireNonNull(getConfig().getString("vanilla-item-rarity")).toUpperCase());
    }

    // Returns custom rarities for vanilla items.
    public Map<Material,ItemRarity> getVanillaItemRarityOverrides() {
        Map<Material, ItemRarity> itemRarityOverrides = new HashMap<>();
        // Loop through the materials
        if(getConfig().getConfigurationSection("vanilla-item-subcategories")==null) return itemRarityOverrides;
        try {
            for (String key : getConfig().getConfigurationSection("vanilla-item-overrides").getKeys(false)) {
                if (key == null && (getConfig().getString("vanilla-item-overrides." + key) != null)) continue;
                // Add the override to a map
                itemRarityOverrides.put(
                        Material.valueOf(key.toUpperCase()),
                        ItemRarity.valueOf(getConfig().getString("vanilla-item-overrides." + key).toUpperCase())
                );
            }
        } catch (Exception ignored) {return itemRarityOverrides;}
        return itemRarityOverrides;
    }

    // Returns custom subcategories for vanilla items.
    public Map<Material, ItemSubcategory> getVanillaItemSubcategoryOverrides() {
        Map<Material, ItemSubcategory> itemSubcategoryOverrides = new HashMap<>();
        // Loop through the materials
        if(getConfig().getConfigurationSection("vanilla-item-subcategories")==null) return itemSubcategoryOverrides;
        try {
            for (String key : getConfig().getConfigurationSection("vanilla-item-subcategories").getKeys(false)) {
                if (key == null) continue;
                // Add the override to a map
                itemSubcategoryOverrides.put(
                        Material.valueOf(key.toUpperCase()),
                        ItemSubcategory.valueOf(getConfig().getString("vanilla-item-subcategories." + key))
                );
            }
        } catch (Exception ignored) {return itemSubcategoryOverrides;}
        return itemSubcategoryOverrides;
    }

    // Get singular and plural currency names and colour
    public @NotNull Component getCurrencyNameSingular() {
        if(getConfig().getString("currency-name-singular")==null) return Component.text("Credit");
        return mm.deserialize(getConfig().getString("currency-name-singular"));
    }

    public @NotNull Component getCurrencyNamePlural() {
        if(getConfig().getString("currency-name-plural")==null) return Component.text("Credits");
        return mm.deserialize(getConfig().getString("currency-name-plural"));
    }

    public @NotNull NamedTextColor getCurrencyNameFormatting() {
        if(getConfig().getString("currency-colour")==null) return NamedTextColor.GREEN;
        return NamedTextColor.NAMES.value(getConfig().getString("currency-colour"));
    }

    public String getCurrencyNameSingularString() {
        if(getConfig().getString("currency-name-singular")==null) return ("Credit");
        return mm.stripTags(getConfig().getString("currency-name-singular"));
    }

    public String getCurrencyNamePluralString() {
        if(getConfig().getString("currency-name-plural")==null) return ("Credits");
        return mm.stripTags(getConfig().getString("currency-name-plural"));
    }

    public boolean enableItemCooldownAnimation() {
        return getConfig().getBoolean("enable-cooldown-animation");
    }

    public boolean enableExperienceNotification() {
        return getConfig().getBoolean("send-exp-actionbar");
    }

    public int getArmourConstant() {return getConfig().getInt("armour-constant");}

    public void reloadConfigs() {
        MineshaftApi.getInstance().reloadConfig();
    }

}
