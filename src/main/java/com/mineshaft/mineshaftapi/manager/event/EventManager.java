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

package com.mineshaft.mineshaftapi.manager.event;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.VariableTypeEnum;
import com.mineshaft.mineshaftapi.manager.event.event_subclass.*;
import com.mineshaft.mineshaftapi.manager.event.executor.BeamExecutor;
import com.mineshaft.mineshaftapi.manager.event.executor.EntityDamageExecutor;
import com.mineshaft.mineshaftapi.manager.event.executor.PrepareStrongAttackEventExecutor;
import com.mineshaft.mineshaftapi.manager.event.executor.VectorEventExecutor;
import com.mineshaft.mineshaftapi.manager.event.fields.EventFields;
import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import com.mineshaft.mineshaftapi.manager.event.fields.UniqueEventFields;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        String name = fileName.substring(0, fileName.lastIndexOf('.'));

        events.add(name);
        Logger.logInfo("Initialised event '" + name + "'");
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

    public boolean runEvent(Event event, Location loc, UUID casterId, Object targetEntity) {
        switch (event.getEventType()) {
            case NULL:
                return false;
            case DAMAGE:
                if(!(event instanceof EntityDamageEvent) || !(targetEntity instanceof LivingEntity)) return false;
                new EntityDamageExecutor(event, (LivingEntity) targetEntity).executeEvent(casterId);
                return true;
            case PREPARE_STRONG_ATTACK:
                if(!(event instanceof PrepareStrongAttackEntityEvent) || !(targetEntity instanceof LivingEntity)) return false;
                new PrepareStrongAttackEventExecutor(event, (LivingEntity) targetEntity).executeEvent(casterId);
                return true;
            case BEAM:
                if(!(event instanceof BeamEvent)) return false;
                new BeamExecutor((BeamEvent) event,loc).executeEvent(casterId);
                return true;
            case PLAYER_VECTOR_DASH,PLAYER_VECTOR_LEAP:
                if(!(event instanceof VectorPlayerEvent) || !(targetEntity instanceof Player)) return false;
                new VectorEventExecutor(event, (Player) targetEntity).executeEvent(casterId);
                return true;
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
                return true;
            default:
                Logger.logWarning("Unexpected value in YAML event!");
        }
        return false;
    }

    public Event getEvent(String eventName) {
        // TODO: make this use configuration section
        return getEvent(eventName, null,null);
    }

    public Event getEvent(String eventName, ItemStack executingItem) {
        return getEvent(eventName, executingItem, null);
    }


    // parent bit not working
    public Event getEvent(String eventName, ItemStack executingItem, ConfigurationSection section) {

        if (section == null) {
            File fileYaml = new File(path, eventName + ".yml");

            // return null if file does not exist
            if (!fileYaml.exists()) return null;

            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);
            section = yamlConfiguration.getDefaultSection();
        }
        assert section != null;

        // Whether the item has a parent item
        boolean hasParent = false;

        Event eventClass = new Event();
        eventClass.setName(eventName);
        EventType eventType = EventType.NULL;

        List<String> uniqueFields = new ArrayList<>();

        for (UniqueEventFields field : UniqueEventFields.values()) {
            uniqueFields.add(field.name().toLowerCase());
        }

        if (section.contains("parent")) {
            // TODO: fix parent inheritance

            String parentName = section.getString("parent");
            if (parentName != null && !parentName.equalsIgnoreCase("null") && !parentName.equalsIgnoreCase("nil")) {
                eventClass = getEvent(parentName);
                hasParent = true;
            }
        }

        // Check the event type
        if (section.contains("event_type")) {
            eventType = EventType.valueOf(section.getString("event_type").toUpperCase(Locale.ROOT));
        }

        boolean isEventsList = false;
        ArrayList<String> nestedEvents = new ArrayList<>();

        // get keys
        for (String field : section.getKeys(false)) {

            // Get the accompanied sound
            if (field.equalsIgnoreCase("sound")) {
                eventClass.setSound(section.getString("sound"));

            // Load general fields
            } else if (uniqueFields.contains(field)) {
                eventClass.addParameter(field, section.get(field));

            // Get the event offset
            } else if (field.equals("offset")) {
                // Set offset vector
                int x = 0;
                int y = 0;
                int z = 0;

                if (section.contains("offset.x")) {
                    x = section.getInt("offset.x");
                }
                if (section.contains("offset.y")) {
                    y = section.getInt("offset.y");
                }
                if (section.contains("offset.z")) {
                    z = section.getInt("offset.z");
                }
                eventClass.setOffset(x, y, z);

            // Get a list of nested events
            } else if (field.equals("list_events")) {
                nestedEvents.addAll(section.getStringList("list_events"));
            }
        }

        switch (eventType) {
            case BEAM -> eventClass = EventLoader.loadBeamEvent(section,eventClass,executingItem);
            case PREPARE_STRONG_ATTACK -> eventClass = EventLoader.loadStrongAttackEvent(section,eventClass,executingItem);
            case DAMAGE -> eventClass = EventLoader.loadDamageEvent(section,eventClass,executingItem);
            case BETONQUEST -> {
                // TODO:
            }
        }
            return eventClass;
        }



    public static boolean isHardcoded (String event){
        return event.equalsIgnoreCase("parry");
    }

}
