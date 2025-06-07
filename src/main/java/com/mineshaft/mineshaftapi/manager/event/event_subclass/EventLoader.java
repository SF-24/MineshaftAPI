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
import com.mineshaft.mineshaftapi.util.ColourFormatter;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.MechanicUtil;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.damage.DamageType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EventLoader {

    public static Event loadDamageEvent(ConfigurationSection section, Event eventClass, ItemStack executingItem) {
        EntityDamageEvent loadedEvent = new EntityDamageEvent();
        loadedEvent.setName(eventClass.getName());
        loadedEvent.setEventType(EventType.DAMAGE);
        loadedEvent.setOffset(eventClass.getOffset());
        loadedEvent.setTarget(eventClass.getTarget());
        loadedEvent.customParameters = eventClass.getParameters();

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


    public static Event loadStrongAttackEvent(ConfigurationSection section, Event eventClass, ItemStack executingItem) {
        PrepareStrongAttackEntityEvent prepareStrongAttackEntityEvent = new PrepareStrongAttackEntityEvent();
        prepareStrongAttackEntityEvent.setName(eventClass.getName());
        prepareStrongAttackEntityEvent.setEventType(EventType.PREPARE_STRONG_ATTACK);
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
        BeamEvent beamEvent = new BeamEvent();
        beamEvent.setName(eventClass.getName());
        beamEvent.setEventType(EventType.BEAM);
        beamEvent.setOffset(eventClass.getOffset());
        beamEvent.setTarget(eventClass.getTarget());
        beamEvent.customParameters = eventClass.getParameters();

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
            beamEvent.setOnHitEntity(new EntityDamageEvent());
        }

        if (executingItem != null) {
            // Override damage via weapon stat

            double damage = ItemManager.getItemNbtStat(executingItem, ItemStats.RANGED_DAMAGE);
            int range = (int) ItemManager.getItemNbtRangedStat(executingItem, RangedItemStats.FIRING_RANGE_CUSTOM);
            if (range < 0) range = 0;
            beamEvent.setFlyDistance(range);
            if (damage <= 0) return beamEvent;
            setBeamEventDamage(beamEvent, damage);
        }
        return beamEvent;
    }

    public static void setBeamEventDamage(BeamEvent beamEvent, double damage) {
        // Override events
        for (Event eventIteration : beamEvent.getOnHitEntity()) {
            if (eventIteration instanceof EntityDamageEvent) {
                beamEvent.removeOnHitEntity(eventIteration);
                EntityDamageEvent damageEvent = new EntityDamageEvent();
                damageEvent.damage=damage;
                damageEvent.setEventType(EventType.DAMAGE);
                beamEvent.setOnHitEntity(damageEvent);
            }
        }
        for (Event eventIteration : beamEvent.getOnHitPlayer()) {
            if (eventIteration instanceof EntityDamageEvent) {
                beamEvent.removeOnHitPlayer(eventIteration);
                EntityDamageEvent damageEvent = new EntityDamageEvent();
                damageEvent.setEventType(EventType.DAMAGE);
                damageEvent.damage=damage;
                beamEvent.setOnHitEntity(damageEvent);
            }
        }
    }

}
