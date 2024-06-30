package com.mineshaft.mineshaftapi.manager.item.fields;

import com.mineshaft.mineshaftapi.manager.VariableTypeEnum;
import org.bukkit.Material;

import java.util.Locale;
import java.util.UUID;

public enum ItemFields {

    name(VariableTypeEnum.STRING, "Cleaver"),
    rarity(VariableTypeEnum.ITEM_RARITY, ItemRarity.EXOTIC.name().toLowerCase(Locale.ROOT)),
    item_category(VariableTypeEnum.ITEM_CATEGORY, ItemCategory.WEAPON_MELEE.name().toLowerCase(Locale.ROOT)),
    material(VariableTypeEnum.MATERIAL, Material.GOLDEN_SWORD.name()),
    id(VariableTypeEnum.UUID, UUID.randomUUID().toString()),
    custom_model_data(VariableTypeEnum.INTEGER, 0),
    parent(VariableTypeEnum.STRING, "null"),
    attributes(VariableTypeEnum.LIST, "");

    private final VariableTypeEnum variableType;
    private final Object defaultValue;

    ItemFields(VariableTypeEnum variableType, Object defaultValue) {
        this.variableType=variableType;
        this.defaultValue=defaultValue;
    }

    public VariableTypeEnum getVariableType() {return variableType;}

    public Object getDefaultValue() {return defaultValue;}
}
