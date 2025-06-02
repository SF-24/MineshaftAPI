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
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.combat.BlockingType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

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

            if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
                e.setCancelled(true);
            }

            if(defendableDamage.contains(e.getCause()) && e.getDamage()>0.0001) {
                Player player = (Player) e.getEntity();

                // If user is not blocking
                if(MineshaftApi.getInstance().getBlockingManager().getBlockingType(player.getUniqueId())==null) {
                    // Update damage depending on armour class stat
                    e.setDamage(DamageManager.calculateNewDamage(e.getDamage(),PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS, player)));

                // On parry
                } else if (MineshaftApi.getInstance().getBlockingManager().getBlockingType(player.getUniqueId()).equals(BlockingType.PARRY)) {
                    e.setCancelled(true);
                    float pitch = 0.725f + new Random().nextFloat(0.35f);
                    player.getWorld().playSound(player.getLocation(), "minecraft:sword.parry", 2.0f, pitch);
                    MineshaftApi.getInstance().getBlockingManager().removePlayerParry(player.getUniqueId());

                // On block //TODO: add armour class modifier
                } else if (MineshaftApi.getInstance().getBlockingManager().getBlockingType(player.getUniqueId()).equals(BlockingType.BLOCK)) {
                    e.setDamage(DamageManager.calculateNewDamage(e.getDamage() / 2,PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS, player)));
                }
            }

        } else {
            if(defendableDamage.contains(e.getCause()) && e.getDamage()>0.0001) {
                if(ArmourManager.getArmourClass(e.getEntity())>0) {
                    e.setDamage(DamageManager.calculateNewDamage(e.getDamage(),ArmourManager.getArmourClass(e.getEntity())));
                }
            }

        }
    }
}
