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

package com.mineshaft.mineshaftapi.manager.item;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemRarity;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemSubcategory;
import com.mineshaft.mineshaftapi.util.Language;
import com.mineshaft.mineshaftapi.util.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Objects;

public class ClientItemManager {

    public static String formatName(String name) {
//        Logger.logDebug("Formatting name: " + name);
        return name.trim().replace(" ","-").toLowerCase();
    }

    public static boolean hasNameTranslation(Language language, ItemStack itemStack) {
        return getNameTranslation(language, itemStack) != null;
    }


    public static String getNameTranslation(Language language, ItemStack itemStack) {
        if(itemStack.hasItemMeta()) {
//            Logger.logInfo("Getting name translation: " + language + " " + getTranslationKey(itemStack));

            if(itemStack.getItemMeta().displayName()!=null) {
//                Logger.logInfo("Found display name: " + Objects.requireNonNull(itemStack.getItemMeta().displayName()).toString());
            }
        } else {
//            Logger.logInfo("Null stuff found.");
        }
        return (itemStack.getItemMeta() != null) ?getNameTranslation(language,getTranslationKey(itemStack)):null;
    }

    public static String getTranslationKey(ItemStack itemStack) {
        if(itemStack == null || itemStack.getItemMeta() == null) {
            return null;
        }
        if(itemStack.displayName() instanceof TranslatableComponent translatable && !translatable.args().isEmpty()) {
            return formatName(PlainTextComponentSerializer.plainText().serialize(
                    ((TranslatableComponent)itemStack.displayName()).args().getFirst()
            ));
        }
        return formatName(PlainTextComponentSerializer.plainText().serialize(itemStack.displayName()));
    }

    public static Component getSerialisedTranslation(Language language, ItemStack itemStack) {
        if(getNameTranslation(language,itemStack)==null) return null;

        return MiniMessage.miniMessage().deserialize(getNameTranslation(language,itemStack));
    }

    public static String getNameTranslation(Language language, String name) {
        return MineshaftApi.getInstance().getTranslationManager().getItemNameTranslation(language,name);
    }

    public static boolean isParsable(Language language, ItemStack itemStack) {
        return (itemStack.getType()!=Material.AIR && hasNameTranslation(language,itemStack)) ||
                (MineshaftApi.getInstance().getConfigManager().getVanillaItemRarity() != ItemRarity.STANDARD && ((itemStack.getItemMeta() == null || itemStack.getItemMeta().getLore() == null) && getMaterialItemRarity(itemStack.getType()) != ItemRarity.STANDARD));
    }

    public static ItemRarity getMaterialItemRarity(Material material) {
        ItemRarity itemRarity = MineshaftApi.getInstance().getConfigManager().getVanillaItemRarityOverrides().get(material);
        if(itemRarity==null) return MineshaftApi.getInstance().getConfigManager().getVanillaItemRarity();
        return itemRarity;
    }

    public static ItemSubcategory getMaterialItemSubcategory(Material material) {
        ItemSubcategory returnVal = MineshaftApi.getInstance().getConfigManager().getVanillaItemSubcategoryOverrides().get(material);
        if(returnVal==null) return ItemSubcategory.DEFAULT;
        return returnVal;
    }

    public static ItemStack parseItem(Language language, ItemStack itemStack) {
        if(itemStack==null) return new ItemStack(Material.AIR);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta==null || itemMeta.getLore() == null || itemMeta.getLore().isEmpty()) {
            ItemRarity itemRarity = getMaterialItemRarity(itemStack.getType());
            if(itemRarity!=ItemRarity.STANDARD && itemMeta!=null) {
                itemMeta.setLore(Collections.singletonList(LoreManager.getRarityString(itemRarity, LoreManager.getItemSubcategoryDisplay(itemRarity,getMaterialItemSubcategory(itemStack.getType())))));
            }
            itemStack.setItemMeta(itemMeta);
        }
        if(itemMeta != null && itemMeta.getDisplayName()!=null) {
            // If translation exists
            if(hasNameTranslation(language,itemStack)) {
                // Get the translation
                Component translation = getSerialisedTranslation(language, itemStack);
                if(translation!=null) {
                    // Apply the previous colour
//                    if (itemMeta.customName().color() != null && translation != null) translation.colorIfAbsent(itemMeta.customName().color());
                    // If it doesn't work, set it to white
                    translation = translation.colorIfAbsent(itemStack.displayName().color());
                    translation = translation.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
                    itemMeta.displayName(translation);
                    itemStack.setItemMeta(itemMeta);
                    Logger.logInfo("Translated name: " + translation);
                } else {
                    Logger.logWarning("Null translation component!");
                }
            }
        }
        return itemStack;
    }

}
