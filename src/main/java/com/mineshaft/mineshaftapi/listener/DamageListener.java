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

package com.mineshaft.mineshaftapi.listener;

import com.mineshaft.mineshaftapi.MineshaftApi;
import com.mineshaft.mineshaftapi.manager.DamageManager;
import com.mineshaft.mineshaftapi.manager.entity.armour_class.ArmourManager;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemSubcategoryProperty;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.combat.BlockingType;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.util.maths.VectorUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DamageListener implements Listener {

    @EventHandler
    void damageListener(EntityDamageEvent e) {
        List<EntityDamageEvent.DamageCause> defendableDamage = new ArrayList<>();
        defendableDamage.add(EntityDamageEvent.DamageCause.PROJECTILE);
        defendableDamage.add(EntityDamageEvent.DamageCause.CONTACT);
        defendableDamage.add(EntityDamageEvent.DamageCause.THORNS);
        defendableDamage.add(EntityDamageEvent.DamageCause.ENTITY_ATTACK);
        defendableDamage.add(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
        defendableDamage.add(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK);
        defendableDamage.add(EntityDamageEvent.DamageCause.FLY_INTO_WALL);
        defendableDamage.add(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION);
        defendableDamage.add(EntityDamageEvent.DamageCause.HOT_FLOOR);
        defendableDamage.add(EntityDamageEvent.DamageCause.MAGIC);
        if(e.getEntity() instanceof Player) {
            // If a player is damaged

            if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
                e.setCancelled(true);
            }


            if(defendableDamage.contains(e.getCause()) && e.getDamage()>0.0001 ) {
                Player player = (Player) e.getEntity();

                // If user is not blocking
                if(MineshaftApi.getInstance().getActionManager().getBlockingType(player.getUniqueId())==null ||
                        ((e.getDamageSource().getSourceLocation()!=null) && (VectorUtil.getTopDownAngle(e.getEntity().getLocation().getDirection(),e.getDamageSource().getSourceLocation().getDirection())>Math.toRadians(60)))
                ) {
                    // Update damage depending on armour class stat
                    e.setDamage(DamageManager.calculateNewDamage(e.getDamage(),PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS, player)));

                // On parry

                } else if (MineshaftApi.getInstance().getActionManager().getBlockingType(player.getUniqueId()).equals(BlockingType.PARRY)) {
                    e.setCancelled(true);
                    float pitch = 0.725f + new Random().nextFloat(0.35f);
                    player.getWorld().playSound(player.getLocation(), "minecraft:sword.parry", 2.0f, pitch);
                    MineshaftApi.getInstance().getActionManager().removePlayerParry(player.getUniqueId());

                // On block //TODO: add armour class modifier
                } else if (MineshaftApi.getInstance().getActionManager().getBlockingType(player.getUniqueId()).equals(BlockingType.BLOCK)) {
                    e.setDamage(DamageManager.calculateNewDamage(e.getDamage() / 2,PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS, player)));
                }
            }

        } else {
            if(defendableDamage.contains(e.getCause()) && e.getDamage()>0.0001) {
                int hitBonus = 0;
                if(e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
                    // Check for weapon in hand
                    ItemStack item = ((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInMainHand();
                    if(!item.getType().equals(Material.AIR)) {
                        Player player = (Player) ((EntityDamageByEntityEvent) e).getDamager();

                        // If the player is proficient with the item.
                        if (JsonPlayerBridge.getWeaponProficiencies(player).contains(ItemManager.getItemSubcategory(item).name().toLowerCase())) {
                           // Make variable
                            hitBonus+=2;
                        }
                        // The player is proficient with the given item
                        if(ItemManager.getItemSubcategory(item).getPropertyList().contains(ItemSubcategoryProperty.FINESSE)) {
                            hitBonus+=Math.max(JsonPlayerBridge.getAttribute(player,"DEX"),JsonPlayerBridge.getAttribute(player,"STR"));
                        } else {
                            hitBonus+=JsonPlayerBridge.getAttribute(player,"STR");
                        }
                    }
                }
                if(ArmourManager.getArmourClass(e.getEntity())>0) {
                    e.setDamage(DamageManager.calculateNewDamage(e.getDamage(),(ArmourManager.getArmourClass(e.getEntity())-hitBonus)));
                }
            }
        }
    }
}
