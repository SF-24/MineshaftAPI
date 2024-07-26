/*
 *     This program is a Minecraft plugin developed in Java for the Spigot API.
 *     It adds multiple RPG features intended for Multiplayer gameplay.
 *
 *     Copyright (C) 2024  Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.mineshaft.mineshaftapi.manager.event;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.VariableTypeEnum;
import com.mineshaft.mineshaftapi.manager.event.event_subclass.BeamEvent;
import com.mineshaft.mineshaftapi.manager.event.fields.EventFields;
import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import com.mineshaft.mineshaftapi.manager.event.fields.LocalEvent;
import com.mineshaft.mineshaftapi.manager.event.fields.UniqueEventFields;
import com.mineshaft.mineshaftapi.util.ColourFormatter;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EventManager {

    ArrayList<String> events = new ArrayList<>();

    public ArrayList<String> getEventList() {
        return events;
    }

    String path = MineshaftApi.getInstance().getEventPath();

    public void initialiseEvents() {
        events =new ArrayList<>();

        File folder = new File(path);
        if(!folder.exists()) {
            folder.mkdirs();
        }

        if(folder.listFiles()==null || folder.listFiles().length==0) {
            createDemoEvent();
        }

        for(File file : Objects.requireNonNull(folder.listFiles())) {
            initialiseEvent(file.getName());
        }
    }

    public void initialiseEvent(String fileName) {
        File fileYaml = new File(path, fileName);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        String name = fileName.substring(0, fileName.lastIndexOf('.'));

        events.add(name);
        Logger.logInfo("Initialised item '" + name + "' with UUID '" + yamlConfiguration.getString("id") + "'");
    }

    private void createDemoEvent() {
        String path = MineshaftApi.getInstance().getEventPath();
        File fileYaml = new File(path, "example-event" + ".yml");

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        Logger.logInfo("Yaml path for demo item: " + path + " example-event.yml");

        if (!fileYaml.exists()) {
            try {
                // Create demo item
                File yamlDir = fileYaml.getParentFile();
                if (!yamlDir.exists()) {
                    yamlDir.mkdir();
                }
                fileYaml.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(EventFields eventFields : EventFields.values()) {
                yamlConfiguration.createSection(eventFields.name().toLowerCase(Locale.ROOT));
                if (!eventFields.getVariableType().equals(VariableTypeEnum.LIST)) {
                    yamlConfiguration.set(eventFields.name().toLowerCase(Locale.ROOT), eventFields.getDefaultValue());
                }
            }

            // Create location section
            yamlConfiguration.createSection("offset.x");
            yamlConfiguration.createSection("offset.y");
            yamlConfiguration.createSection("offset.z");
            yamlConfiguration.set("offset.x",0);
            yamlConfiguration.set("offset.y",1);
            yamlConfiguration.set("offset.z",0);

            // Create on hit event section
            yamlConfiguration.createSection("on_hit.entity.damage");
            yamlConfiguration.set("on_hit.entity.damage",8);

            // Save demo event
            try {
                yamlConfiguration.save(fileYaml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean runEvent(Event event, Location loc) {
        switch (event.getEventType()) {
            case BEAM:

                break;
            case PLAY_SOUND:
                if(loc.getWorld()==null) return false;
                String sound = "";
                float volume = 1.0f;
                float pitch = 1.0f;
                for(Object param : event.getParameters().keySet()) {
                    if (param.equals("sound")) {
                        sound = (String) event.getParameter("sound");
                    } else if (param.equals("volume")) {
                        volume = (float) event.getParameter("volume");
                    } else if (param.equals("pitch")) {
                        pitch = (float) event.getParameter("pitch");
                    }
                }
                loc.getWorld().playSound(loc,sound,volume,pitch);
                break;
            default:
                Logger.logWarning("Unexpected value in YAML event!");
        }
        return false;
    }

    public Event getEvent(String eventName) {


        File fileYaml = new File(path, eventName +".yml");

        // return null if file does not exist
        if(!fileYaml.exists()) return null;

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        // Whether the item has a parent item
        boolean hasParent=false;

        Event eventClass = new Event();
        eventClass.setName(eventName);
        EventType eventType = eventClass.getEventType();

        List<String> uniqueFields = new ArrayList<>();

        for(UniqueEventFields field : UniqueEventFields.values()) {
            uniqueFields.add(field.name().toLowerCase());
        }

        if(yamlConfiguration.contains("parent")) {
            String parentName = yamlConfiguration.getString("parent");
            if(parentName!=null && !parentName.equalsIgnoreCase("null") && !parentName.equalsIgnoreCase("nil")) {
                eventClass = getEvent(parentName);
                hasParent=true;
            }
        }

        if(yamlConfiguration.contains("event_type")) {
            try {
                eventType = EventType.valueOf(yamlConfiguration.getString("event_type"));
            } catch (Exception e) {
                if(!hasParent) {
                    Logger.logError("ERROR! Could not load event '" + eventName + "");
                    return null;
                }
                Logger.logWarning("ERROR! Could not load event type for event: '" + eventName + "'. Invalid event type. Using parent event type instead");
            }
        }

        boolean isEventsList = false;
        ArrayList<String> nestedEvents = new ArrayList<>();

        // get keys
        for(String field : yamlConfiguration.getKeys(false)) {
            if(uniqueFields.contains(field)) {
                eventClass.addParameter(field, yamlConfiguration.get(field));
            } else if(field.equals("offset")) {
                // Set offset vector
                int x = 0;
                int y = 0;
                int z = 0;

                if(yamlConfiguration.contains("offset.x")) {
                    x = yamlConfiguration.getInt("offset.x");
                }
                if(yamlConfiguration.contains("offset.y")) {
                    y = yamlConfiguration.getInt("offset.y");
                }
                if(yamlConfiguration.contains("offset.z")) {
                    z = yamlConfiguration.getInt("offset.z");
                }
                eventClass.setOffset(x,y,z);
            } else if(field.equals("list_events")) {
                nestedEvents.addAll(yamlConfiguration.getStringList("list_events"));
            }
        }

        if(eventType.equals(EventType.BEAM)) {
            BeamEvent beamEvent = new BeamEvent();
            beamEvent.setName(eventClass.getName());
            beamEvent.setEventType(eventClass.getEventType());
            beamEvent.setOffset(eventClass.getOffset());
            beamEvent.setTarget(eventClass.getTarget());
            beamEvent.customParameters=eventClass.getParameters();

            for(String key : yamlConfiguration.getKeys(false)) {
                switch (key) {
                    case "speed":
                        beamEvent.setSpeed(yamlConfiguration.getInt(key));
                        break;
                    case "size":
                        beamEvent.setSize(yamlConfiguration.getInt(key));
                        break;
                    case "colour":
                        if(yamlConfiguration.getString("colour")!=null) {
                            beamEvent.setColour(ColourFormatter.getColourFromString(yamlConfiguration.getString("colour")));
                        }
                        break;
                    case "particle_count":
                        beamEvent.setParticleCount(yamlConfiguration.getInt(key));
                        break;
                    case "particle_type":
                        beamEvent.setParticleType(Particle.valueOf(yamlConfiguration.getString(key)));
                        break;
                    case "fly_distance":
                        beamEvent.setFlyDistance(yamlConfiguration.getInt(key));
                        break;
                    case "power":
                        beamEvent.setPower(yamlConfiguration.getInt(key));
                        break;
                }
            }

            if(yamlConfiguration.contains("on_hit")) {

                for(String section : yamlConfiguration.getConfigurationSection("on_hit").getKeys(false)) {
                    for(String element : yamlConfiguration.getConfigurationSection("on_hit." + section).getKeys(false)) {
                        String path = "on_hit."+section+"."+element;

                        LocalEvent localEvent = null;
                        try {
                            localEvent = LocalEvent.valueOf(element.toUpperCase(Locale.ROOT));
                        } catch (Exception e) {
                            Logger.logError("Could not load local event " + element  + " for yaml event: " + eventName);
                            break;
                        }

                        Object object = null;

                        switch (localEvent) {
                            case DAMAGE:
                            case EXPLODE:
                                object = yamlConfiguration.getInt(path);
                                break;
                            case SET_BLOCK:
                                object = Material.valueOf(yamlConfiguration.getString(path).toUpperCase());
                                break;
                        }

                        if(object != null) {
                            switch (section) {
                                case "entity":
                                    beamEvent.setOnHitEntity(localEvent,object);
                                    break;
                                case "player":
                                    beamEvent.setOnHitPlayer(localEvent,object);
                                    break;
                                case "block":
                                    beamEvent.setOnHitBlock(localEvent,object);
                                    break;

                            }
                        }
                    }
                }


            } else {
                beamEvent.setOnHitEntity(LocalEvent.DAMAGE, 10);
            }
            return beamEvent;
        }

        return eventClass;
    }

}
