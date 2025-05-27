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

public enum ItemSubcategory {

    // W.I.P -> more coming soon...

    // TOOLS

    TOOL /*generic*/ (ItemSubcategoryType.TOOL),

    AXE(ItemSubcategoryType.TOOL),
    PICKAXE(ItemSubcategoryType.TOOL),
    SHOVEL(ItemSubcategoryType.TOOL),
    HOE(ItemSubcategoryType.TOOL),
    DRILL(ItemSubcategoryType.TOOL),
    PIPE(ItemSubcategoryType.TOOL),

    MEDKIT(ItemSubcategoryType.TOOL),
    CONSUMABLE(ItemSubcategoryType.CONSUMABLE),

    // WEAPONS
    DAGGER(ItemSubcategoryType.WEAPON_SIMPLE),
    STAFF(ItemSubcategoryType.WEAPON_SIMPLE),

    SHORTSWORD(ItemSubcategoryType.WEAPON_MARTIAL),
    LONGSWORD(ItemSubcategoryType.WEAPON_MARTIAL),
    GLAIVE(ItemSubcategoryType.WEAPON_MARTIAL),

    // RANGED WEAPONS
    LIGHT_CROSSBOW(ItemSubcategoryType.WEAPON_SIMPLE),
    HEAVY_CROSSBOW(ItemSubcategoryType.WEAPON_MARTIAL),
    SHORTBOW(ItemSubcategoryType.WEAPON_SIMPLE),
    LONGBOW(ItemSubcategoryType.WEAPON_MARTIAL),

    // OTHER WEAPONS
    LIGHTSABER(ItemSubcategoryType.WEAPON_LIGHT),
    VIBROWEAPON(ItemSubcategoryType.WEAPON_LIGHT),
    ELECTROSTAFF(ItemSubcategoryType.WEAPON_LIGHT),

    // GUNS
    GUN(ItemSubcategoryType.WEAPON_BLASTER),
    BLASTER(ItemSubcategoryType.WEAPON_BLASTER),
    RIFLE(ItemSubcategoryType.WEAPON_BLASTER),
    LASGUN(ItemSubcategoryType.WEAPON_BLASTER),

    // ARMOUR
    HELMET(ItemSubcategoryType.ARMOUR),
    HAT(ItemSubcategoryType.ARMOUR),
    HOOD(ItemSubcategoryType.ARMOUR),
    TUNIC(ItemSubcategoryType.ARMOUR),
    CHESTPLATE(ItemSubcategoryType.ARMOUR),
    TROUSERS(ItemSubcategoryType.ARMOUR),
    LEGGINGS(ItemSubcategoryType.ARMOUR),
    BOOTS(ItemSubcategoryType.ARMOUR),

    DEFAULT(ItemSubcategoryType.NONE);

    ;
    private final ItemSubcategoryType type;

    ItemSubcategory(ItemSubcategoryType type) {
        this.type=type;
    }

    public ItemSubcategoryType getType() {return type;}

}
