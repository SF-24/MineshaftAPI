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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ConsumableHandler extends ComponentHandler {
    public ConsumableHandler(ConfigurationSection yamlConfiguration) {
        super(yamlConfiguration);
    }

    @Override
    public void handlePropertyPreMeta(ItemMeta itemMeta) {
    }

    @Override
    public void handlePropertyPostMeta(ItemStack item) {
        String path = "consumable.";
        ArrayList<ConsumeEffect> effects = new ArrayList<>();
        HashMap<PotionEffect, Float> potionEffects = new HashMap<>();
        ItemUseAnimation animation = ItemUseAnimation.EAT;
        float eatSeconds = 1.0f;
        boolean consumeParticles = true;
        for (String key : yamlConfiguration.getConfigurationSection("consumable").getKeys(false)) {
            switch (key) {
                case "consume_seconds":
                    eatSeconds = (float) yamlConfiguration.getDouble(path + "eat_seconds");
                    break;
                case "animation":
                    animation = ItemUseAnimation.valueOf(yamlConfiguration.getString(path + "animation"));
                    break;
                case "has_consume_particles":
                    consumeParticles = yamlConfiguration.getBoolean("has_consume_particles");
                    break;
                case "consume_sound":
                    // TODO: Add consume sound
                    break;
                case "potion_effects":
                    for (String effectName : yamlConfiguration.getConfigurationSection(path + "potion_effects").getKeys(false)) {
                        if(effectName.equalsIgnoreCase("clear")) {
                            effects.add(ConsumeEffect.clearAllStatusEffects());
                        }
                        String tempPath = path + "potion_effects." + effectName + ".";
                        PotionEffectType potionEffectType = PotionEffectType.getByName(effectName.toUpperCase());
                        int duration = 20 * 60;
                        int amplifier = 0;
                        float effectProbability = 1.0f;
                        boolean ambient = false;
                        boolean particles = false;
                        boolean icon = true;
                        for (String parameter : yamlConfiguration.getConfigurationSection(path + "potion_effects." + effectName).getKeys(false)) {
                            switch (parameter) {
                                case "probability":
                                    effectProbability = (float) yamlConfiguration.getDouble(tempPath + "probability");
                                    break;
                                case "duration":
                                    duration = yamlConfiguration.getInt(tempPath + "duration");
                                case "amplifier":
                                    amplifier = yamlConfiguration.getInt(tempPath + "amplifier");
                                case "ambient":
                                    ambient = yamlConfiguration.getBoolean(tempPath + "ambient");
                                case "particles":
                                    particles = yamlConfiguration.getBoolean(tempPath + "particles");
                                case "icon":
                                    icon = yamlConfiguration.getBoolean(tempPath + "icon");
                            }
                        }
                        potionEffects.put(new PotionEffect(potionEffectType,duration,amplifier,ambient,particles,icon),effectProbability);
                    }
                    for(PotionEffect eff : potionEffects.keySet()) {
                        effects.add(ConsumeEffect.applyStatusEffects(Collections.singletonList(eff),potionEffects.get(eff)));
                    }

                default:
                    break;
            }
        }

        // Add consumable
        Consumable consumable = Consumable.consumable().consumeSeconds(eatSeconds).hasConsumeParticles(consumeParticles).animation(animation).build();
        consumable.consumeEffects().addAll(effects);
        item.setData(DataComponentTypes.CONSUMABLE, consumable);
    }
}
