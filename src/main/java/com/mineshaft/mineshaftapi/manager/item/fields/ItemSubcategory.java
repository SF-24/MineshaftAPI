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

package com.mineshaft.mineshaftapi.manager.item.fields;

import java.util.List;

public enum ItemSubcategory {

    // W.I.P -> more coming soon...

    // TOOLS

    TOOL /*generic*/ (ItemSubcategoryType.TOOL, List.of()),

    AXE(ItemSubcategoryType.TOOL, List.of()),
    PICKAXE(ItemSubcategoryType.TOOL,List.of()),
    SHOVEL(ItemSubcategoryType.TOOL,List.of()),
    HOE(ItemSubcategoryType.TOOL,List.of()),
    DRILL(ItemSubcategoryType.TOOL,List.of()),
    PIPE(ItemSubcategoryType.TOOL,List.of()),

    MEDKIT(ItemSubcategoryType.TOOL,List.of()),
    CONSUMABLE(ItemSubcategoryType.CONSUMABLE,List.of()),

    // WEAPONS
    WEAPON(ItemSubcategoryType.WEAPON_GENERIC,List.of()),

    CLUB(ItemSubcategoryType.WEAPON_SIMPLE,List.of(ItemSubcategoryProperty.LIGHT)),
    DAGGER(ItemSubcategoryType.WEAPON_SIMPLE,List.of(ItemSubcategoryProperty.FINESSE, ItemSubcategoryProperty.LIGHT)),
    GREAT_CLUB(ItemSubcategoryType.WEAPON_SIMPLE,List.of(ItemSubcategoryProperty.HEAVY)),
    HAND_AXE(ItemSubcategoryType.WEAPON_SIMPLE,List.of()),
    HAMMER(ItemSubcategoryType.WEAPON_SIMPLE,List.of()),
    MACE(ItemSubcategoryType.WEAPON_SIMPLE,List.of()),
    STAFF(ItemSubcategoryType.WEAPON_SIMPLE,List.of()),
    SPEAR(ItemSubcategoryType.WEAPON_SIMPLE,List.of()),

    GREAT_AXE(ItemSubcategoryType.WEAPON_MARTIAL,List.of(ItemSubcategoryProperty.HEAVY)),
    SCIMITAR(ItemSubcategoryType.WEAPON_MARTIAL,List.of()),
    SHORTSWORD(ItemSubcategoryType.WEAPON_MARTIAL,List.of(ItemSubcategoryProperty.LIGHT,ItemSubcategoryProperty.FINESSE)),
    LONGSWORD(ItemSubcategoryType.WEAPON_MARTIAL,List.of()),
    GLAIVE(ItemSubcategoryType.WEAPON_MARTIAL,List.of(ItemSubcategoryProperty.HEAVY)),
    MATTOCK(ItemSubcategoryType.WEAPON_MARTIAL,List.of(ItemSubcategoryProperty.HEAVY)),

    SWORD(ItemSubcategoryType.WEAPON_MARTIAL, List.of()),

    // RANGED WEAPONS
    LIGHT_CROSSBOW(ItemSubcategoryType.WEAPON_SIMPLE,List.of()),
    HEAVY_CROSSBOW(ItemSubcategoryType.WEAPON_MARTIAL,List.of(ItemSubcategoryProperty.HEAVY)),
    SHORTBOW(ItemSubcategoryType.WEAPON_SIMPLE,List.of()),
    LONGBOW(ItemSubcategoryType.WEAPON_MARTIAL,List.of()),
    SLINGSHOT(ItemSubcategoryType.WEAPON_SIMPLE,List.of(ItemSubcategoryProperty.LIGHT)),

    // OTHER WEAPONS
    LIGHTSABER(ItemSubcategoryType.WEAPON_LIGHT,List.of()),
    ELECTROSTAFF(ItemSubcategoryType.WEAPON_LIGHT,List.of()),
    VIBROWEAPON(ItemSubcategoryType.WEAPON_VIBROWEAPON,List.of()),

    // GUNS
    GUN(ItemSubcategoryType.WEAPON_BLASTER,List.of()),
    BLASTER(ItemSubcategoryType.WEAPON_BLASTER,List.of()),
    BLASTER_PISTOL(ItemSubcategoryType.WEAPON_BLASTER,List.of()),
    BLASTER_RIFLE(ItemSubcategoryType.WEAPON_BLASTER,List.of()),
    RIFLE(ItemSubcategoryType.WEAPON_BLASTER,List.of()),
    LASGUN(ItemSubcategoryType.WEAPON_BLASTER,List.of()),

    // ARMOUR
    HELMET(ItemSubcategoryType.ARMOUR,List.of()),
    HAT(ItemSubcategoryType.ARMOUR,List.of()),
    HOOD(ItemSubcategoryType.ARMOUR,List.of()),
    TUNIC(ItemSubcategoryType.ARMOUR,List.of()),
    CHESTPLATE(ItemSubcategoryType.ARMOUR,List.of()),
    TROUSERS(ItemSubcategoryType.ARMOUR,List.of()),
    LEGGINGS(ItemSubcategoryType.ARMOUR,List.of()),
    BOOTS(ItemSubcategoryType.ARMOUR,List.of()),

    WAND(ItemSubcategoryType.WAND,List.of()),

    // Slot items: TODO
    MODIFICATION(ItemSubcategoryType.NONE,List.of()),
    EMITTER(ItemSubcategoryType.NONE,List.of()),
    COLOUR_CRYSTAL(ItemSubcategoryType.NONE,List.of()),
    KYBER_CRYSTAL(ItemSubcategoryType.NONE,List.of()),

    DEFAULT(ItemSubcategoryType.NONE,List.of());

    ;
    private final ItemSubcategoryType type;
    private final List<ItemSubcategoryProperty> propertyList;

    ItemSubcategory(ItemSubcategoryType type, List<ItemSubcategoryProperty> propertyList) {
        this.type=type;
        this.propertyList=propertyList;
    }

    public ItemSubcategoryType getType() {return type;}
    public List<ItemSubcategoryProperty> getPropertyList() {return propertyList;}

}
