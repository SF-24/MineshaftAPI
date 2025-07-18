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

package com.mineshaft.mineshaftapi.manager.event.event_subclass;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.item.RangedItemStats;
import com.mineshaft.mineshaftapi.util.formatter.ColourFormatter;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.MechanicUtil;
import com.mineshaft.mineshaftapi.util.maths.PlanarVectorBounds;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.damage.DamageType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EventLoader {

    public static Event loadDamageEvent(ConfigurationSection section, Event eventClass, ItemStack executingItem) {
        EntityDamageEvent loadedEvent = new EntityDamageEvent(EventType.ENTITY_DAMAGE);
        eventClass.clone(loadedEvent);

        for (String key : section.getKeys(false)) {
            switch (key) {
                case "damage" -> loadedEvent.damage=section.getDouble(key);
                case "damage_type" -> {
                    if (section.getString(key) == null) {
                        loadedEvent.setDamageType(DamageType.GENERIC);
                    } else {
                        loadedEvent.setDamageType(MechanicUtil.getDamageType(section.getString(key)));
                    }
                }
            }
        }
        return loadedEvent;
    }

    public static Event loadVectorEvent(ConfigurationSection section, Event eventClass, ItemStack executingItem) {
        PlayerVectorEvent loadedEvent = new PlayerVectorEvent(eventClass.getEventType());
        eventClass.clone(loadedEvent);
        PlanarVectorBounds bounds = new PlanarVectorBounds();
        if(section.contains("vector_bounds")) {
            for(String key : section.getConfigurationSection("vector_bounds").getKeys(false)) {
                switch (key) {
                    case "y_max" -> bounds.setYMax(section.getDouble(key));
                    case "y_min" -> bounds.setYMin(section.getDouble(key));
                    case "planar_max" -> bounds.setPlaneMax(section.getDouble(key));
                    case "planar_min" -> bounds.setPlaneMin(section.getDouble(key));
                }
            }
        }
        if(section.contains("is_legacy_event")) {
            loadedEvent.setLegacy(section.getBoolean("is_legacy_event"));
        } else if(section.contains("legacy")) {
            loadedEvent.setLegacy(section.getBoolean("legacy"));
        } else {
            loadedEvent.setLegacy(false);
        }
        if(section.contains("allow_if_flying")) {
            loadedEvent.setAllowWhenFlying(section.getBoolean("allow_in_flight"));
        } else {
            loadedEvent.setAllowWhenFlying(false);
        }

        loadedEvent.setVectorBounds(bounds);
        return loadedEvent;
    }


    public static Event loadStrongAttackEvent(ConfigurationSection section, Event eventClass, ItemStack executingItem) {
        PrepareStrongAttackEntityEvent prepareStrongAttackEntityEvent = new PrepareStrongAttackEntityEvent(EventType.ENTITY_PREPARE_STRONG_ATTACK);
        prepareStrongAttackEntityEvent.setName(eventClass.getName());
        prepareStrongAttackEntityEvent.setEventType(EventType.ENTITY_PREPARE_STRONG_ATTACK);
        prepareStrongAttackEntityEvent.setOffset(eventClass.getOffset());
        prepareStrongAttackEntityEvent.setTarget(eventClass.getTarget());
        prepareStrongAttackEntityEvent.customParameters = eventClass.getParameters();

        for (String key : section.getKeys(false)) {
            switch (key) {
                case "damage_multiplier","knockback_power" -> prepareStrongAttackEntityEvent.damageMultiplier=section.getDouble(key);
                case "attack_sound" -> prepareStrongAttackEntityEvent.attackSound=section.getString(key);
                case "particles" -> prepareStrongAttackEntityEvent.particles=section.getBoolean(key);
            }
        }
        return prepareStrongAttackEntityEvent;
    }

    public static Event loadBeamEvent(ConfigurationSection section, Event eventClass, ItemStack executingItem) {
        BeamEvent beamEvent = new BeamEvent(EventType.BEAM);
        eventClass.clone(beamEvent);

        for (String key : section.getKeys(false)) {
            switch (key) {
                case "speed":
                    beamEvent.setSpeed(section.getInt(key));
                    break;
                case "size":
                    beamEvent.setSize(section.getInt(key));
                    break;
                case "colour":
                    if (section.getString("colour") != null) {
                        beamEvent.setColour(ColourFormatter.getColourFromString(section.getString("colour")));
                    }
                    break;
                case "particle_count":
                    beamEvent.setParticleCount(section.getInt(key));
                    break;
                case "particle_type":
                    beamEvent.setParticleType(Particle.valueOf(section.getString(key).toUpperCase()));
                    break;
                case "fly_distance":
                    beamEvent.setFlyDistance(section.getInt(key));
                    break;
                case "power":
                    beamEvent.setPower(section.getInt(key));
                    break;
                case "projectile":
                    beamEvent.setProjectile(true);
                    break;
            }
        }

        if (section.contains("on_hit")) {
//            Logger.logDebug("on hit detected");
            for (String subSection : section.getConfigurationSection("on_hit").getKeys(false)) {
//                Logger.logDebug("Subsection: " + subSection);
                for (String element : section.getConfigurationSection("on_hit." + subSection).getKeys(false)) {
                    String path = "on_hit." + subSection + "." + element;

//                    Logger.logDebug("element");

                    Event localEvent = null;
                    try {
//                        Logger.logDebug("Loading local event");
                        // localEvent = LocalEvent.valueOf(element.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT));
                        localEvent = MineshaftApi.getInstance().getEventManagerInstance().getEvent("local_event_" + UUID.randomUUID(), executingItem, section.getConfigurationSection("on_hit." + subSection + "." + element));
                    } catch (Exception e) {
                        Logger.logError("Could not load local event " + element + " for yaml event: " + beamEvent.getName());
                        break;
                    }

//                    Logger.logDebug(localEvent.toString());

                    switch (subSection) {
                        case "entity":
                            beamEvent.setOnHitEntity(localEvent);
                            break;
                        case "player":
                            beamEvent.setOnHitPlayer(localEvent);
                            break;
                        case "block":
                            beamEvent.setOnHitBlock(localEvent);
                            break;
                    }
                }
            }
        } else {
            beamEvent.setOnHitEntity(new EntityDamageEvent(EventType.ENTITY_DAMAGE));
        }

        if (executingItem != null) {
            // Override damage via weapon stat
            double damage = ItemManager.getItemNbtStat(executingItem, ItemStats.RANGED_DAMAGE);
            int range = (int) ItemManager.getItemNbtRangedStat(executingItem, RangedItemStats.FIRING_RANGE_CUSTOM);
            if (range > 0) {
                beamEvent.setFlyDistance(range);
            }
            if (damage > 0) {
                setBeamEventDamage(beamEvent, damage);
            }
        }
        return beamEvent;
    }

    public static void setBeamEventDamage(BeamEvent beamEvent, double damage) {
        // Override events
        for (Event eventIteration : beamEvent.getOnHitEntity()) {
            if (eventIteration instanceof EntityDamageEvent) {
                beamEvent.removeOnHitEntity(eventIteration);
                EntityDamageEvent damageEvent = new EntityDamageEvent(EventType.ENTITY_DAMAGE);
                damageEvent.damage=damage;
                damageEvent.setEventType(EventType.ENTITY_DAMAGE);
                beamEvent.setOnHitEntity(damageEvent);
            }
        }
        for (Event eventIteration : beamEvent.getOnHitPlayer()) {
            if (eventIteration instanceof EntityDamageEvent) {
                beamEvent.removeOnHitPlayer(eventIteration);
                EntityDamageEvent damageEvent = new EntityDamageEvent(EventType.ENTITY_DAMAGE);
                damageEvent.setEventType(EventType.ENTITY_DAMAGE);
                damageEvent.damage=damage;
                beamEvent.setOnHitEntity(damageEvent);
            }
        }
    }

    public static Event loadDisarmEvent(ConfigurationSection section, Event eventClass, ItemStack executingItem) {
        EntityDisarmEvent loadedEvent = new EntityDisarmEvent(eventClass.getEventType());
        eventClass.clone(loadedEvent);
        PlanarVectorBounds bounds = new PlanarVectorBounds();
        if(section.contains("use_disarm_roll")) {
            loadedEvent.setRollToDisarm(section.getBoolean("use_disarm_roll"));
        }
        if(section.contains("use_knockback")) {
            loadedEvent.setKnockback(section.getBoolean("use_knockback"));
        }
        if(section.contains("base_strength")) {
            loadedEvent.setBaseStrength(section.getInt("base_strength"));
        }
        if(section.contains("use_facing_item_vector")) {
            loadedEvent.setUseFacingItemVector(section.getBoolean("use_facing_item_vector"));
        }
        return loadedEvent;
    }

    public static Event loadKnockbackEvent(ConfigurationSection section, Event eventClass, ItemStack executingItem) {
        EntityKnockbackEvent loadedEvent = new EntityKnockbackEvent(eventClass.getEventType());
        eventClass.clone(loadedEvent);
        PlanarVectorBounds bounds = new PlanarVectorBounds();
        if(section.contains("knockback_multiplier")) {
            loadedEvent.setKnockbackMultiplier(section.getDouble("knockback_multiplier"));
        }
        if(section.contains("limit_vertical_knockback")) {
            loadedEvent.setLimitVerticalKnockback(section.getBoolean("limit_vertical_knockback"));
        }

        return loadedEvent;
    }

    public static Event loadWandEvent(ConfigurationSection section, Event eventClass, ItemStack executingItem) {
        PlayerWandEvent loadedEvent = new PlayerWandEvent(eventClass.getEventType());
        eventClass.clone(loadedEvent);
        return loadedEvent;
    }

}
