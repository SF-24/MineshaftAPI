/*
 * Copyright (c) 2026. Sebastian Frynas
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

package com.mineshaft.mineshaftapi.manager.item.item_components.handlers;

import com.mineshaft.mineshaftapi.util.item.ToolRuleExtended;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ToolHandler extends ComponentHandler {
    public ToolHandler(ConfigurationSection yamlConfiguration) {
        super(yamlConfiguration);
    }

    @Override
    public void handlePropertyPreMeta(ItemMeta meta) {
        ToolComponent toolComponent = meta.getTool();
        for (String field : yamlConfiguration.getConfigurationSection("tool").getKeys(false)) {
            switch (field) {
                case "damage_per_block":
                    toolComponent.setDamagePerBlock(yamlConfiguration.getInt("damage_per_block"));
                case "mining_speed":
                    toolComponent.setDefaultMiningSpeed((float) yamlConfiguration.getDouble("mining_speed"));
                case "block_rules":
                    for(ToolComponent.ToolRule rule : toolComponent.getRules()) {
                        toolComponent.removeRule(rule);
                    }

                    for (String key : yamlConfiguration.getConfigurationSection("block_list").getKeys(false)) {
                        String tempPath = "tool." + field + "." + "block_list." + key;
                        // Each block rule
                        List<ToolComponent.ToolRule> rules = List.of();
                        for(String f : yamlConfiguration.getConfigurationSection(tempPath).getKeys(false)) {
                            ToolComponent.ToolRule toolRule = new ToolRuleExtended();

                            // Get block rule parameters
                            Tag<Tag> tag = Bukkit.getTag("minecraft", NamespacedKey.fromString(f.toUpperCase()),Tag.class);
                            @NotNull Set<Tag> mat = tag.getValues();
                            List<Material> materials = Collections.EMPTY_LIST;
                            mat.stream().map(t -> Material.valueOf(String.valueOf(t))).forEach(materials::add);
                            toolRule.setBlocks(materials);

                            if(yamlConfiguration.contains(tempPath + "." + f + ".blocks")) {

                            }
                            if(yamlConfiguration.contains(tempPath + "." + f + ".correct_for_drops")) {
                                toolRule.setCorrectForDrops(yamlConfiguration.getBoolean((tempPath + "." + f + ".correct_for_drops")));
                            }
                            if(yamlConfiguration.contains(tempPath + "." + f + ".mining_speed")) {
                                toolRule.setSpeed((float) yamlConfiguration.getDouble(tempPath + "." + f + ".mining_speed"));
                            }
                            rules.add(toolRule);
                        }
                        toolComponent.setRules(rules);
                    }
                case "tool_type":

            }
            meta.setTool(toolComponent);
        }
    }

    @Override
    public void handlePropertyPostMeta(ItemStack itemStack) {

    }
}
