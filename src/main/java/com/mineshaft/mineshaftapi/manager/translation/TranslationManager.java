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

package com.mineshaft.mineshaftapi.manager.translation;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.util.Language;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class TranslationManager {

    HashMap<String, HashMap<String,String>> translatedItemNames = new HashMap<>();

    public TranslationManager() {
        Logger.logInfo("Initialising language files.");
        initialiseTranslations();
    }

    public void initialiseTranslations() {
        translatedItemNames.clear();
        initialiseTranslations(new File(MineshaftApi.getTranslationPath()),0);
    }

    public void initialiseTranslations(File folder, int iteration) {
        if(!folder.exists()) {
            folder.mkdirs();
        }
        if(iteration==0&&folder.isDirectory()&&(folder.listFiles()==null|| Objects.requireNonNull(folder.listFiles()).length==0)) {
            makeExample();
        }

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if(!file.isDirectory()) {
                initialiseFile(file);
            } else {
                initialiseTranslations(file, iteration+1);
            }
        }
    }

    public void makeExample() {
        File file = new File(MineshaftApi.getTranslationPath(),"example.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set("language","pl");
        yamlConfiguration.set("items.iron-longsword","Żelazny Miecz Długi");
        yamlConfiguration.set("items.lightsaber","Miecz Świetlny");
        yamlConfiguration.set("items.mini-message","<white>Używam <red>Mini<blue>-Me<green>ssa<purple>ge");
        try {
            yamlConfiguration.save(file);
            Logger.logInfo("Successfully saved example language translation file");
        } catch (IOException e) {
            Logger.logError("Error saving example language file!");
        }
    }

    public void initialiseFile(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(!config.contains("language")) {
            config.createSection("language");
            config.set("language", "put-language-name-here");
            Logger.logError("No language key in language file " + file.getName());
            return;
        }
        // If the language String is found
        String languageString = config.getString("language");
        if(languageString==null||languageString.equals("put-language-name-here")) {
            Logger.logError("Invalid language definition in language file " + file.getName());
            return;
        }
        // The language string.
        Language language = Language.getLanguage(languageString.toLowerCase());
        Logger.logInfo("Logged language file " + file.getName() + " with language code: " + language.getSimplifiedCode());

        if(config.contains("item")) {
            parseItemNameConfigurationSection(language, config.getConfigurationSection("item"));
        }
        if(config.contains("item-names")) {
            parseItemNameConfigurationSection(language, config.getConfigurationSection("item-names"));
        }
        if(config.contains("item-name")) {
            parseItemNameConfigurationSection(language, config.getConfigurationSection("item-name"));
        }
        if(config.contains("items")) {
            parseItemNameConfigurationSection(language, config.getConfigurationSection("items"));
        }
        if(config.contains("itemnames")) {
            parseItemNameConfigurationSection(language, config.getConfigurationSection("itemnames"));
        }
        if(config.contains("itemname")) {
            parseItemNameConfigurationSection(language, config.getConfigurationSection("itemname"));
        }
    }

    // Cache lang keys
    public void parseItemNameConfigurationSection(Language language,ConfigurationSection section) {
        for(String langKey : section.getKeys(false)) {
            if(langKey==null) continue;
            if(section.getString(langKey)!=null&&!section.getString(langKey).isEmpty()) {
                cacheItemNameTranslation(language, langKey, section.getString(langKey));
                Logger.logDebug("cached: " + langKey + "->" +  section.getString(langKey) + " for " + language.name());
            }
        }
    }

    public void cacheItemNameTranslation(Language language, String originalName, String translatedName) {
        HashMap<String,String> translations;
        if(translatedItemNames.containsKey(language.getSimplifiedCode())) {
            translations=translatedItemNames.get(language.getSimplifiedCode());
        } else {
            translations=new HashMap<>();
        }
        translations.put(originalName, translatedName);
        translatedItemNames.put(language.getSimplifiedCode(), translations);
    }

    public HashMap<String,String> getItemNameTranslations(Language language) {
        return (translatedItemNames.get(language.getSimplifiedCode())!=null)?translatedItemNames.get(language.getSimplifiedCode()):new HashMap<>();
    }

    public String getItemNameTranslation(Language language, String originalName) {
//        Logger.logInfo("Parsing item name translation: " + originalName);

        if(translatedItemNames.get(language.getSimplifiedCode()) !=null && translatedItemNames.get(language.getSimplifiedCode()).containsKey(originalName)) {
            return translatedItemNames.get(language.getSimplifiedCode()).get(originalName);
        }
        return null;
    }

}
