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

import com.mineshaft.mineshaftapi.manager.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.util.Logger;
import de.unpixelt.armorchange.ArmorEquipEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class DamageListener implements Listener {

    @EventHandler
    void damageListener(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
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

            if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
                e.setCancelled(true);
            }

//            e.getEntity().sendMessage("executed " + e.getCause());
            if(defendableDamage.contains(e.getCause()) && e.getDamage()>0.0001) {

                Player player = (Player) e.getEntity();

                // Update damage depending on defence stat
                double defence = PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS,player);
                double damageReduction = defence/(defence+20);
                double damage = e.getDamage();
                damage=damage*(1-damageReduction);
                //player.sendMessage("Original damage " + e.getDamage());
                e.setDamage(damage);
                //player.sendMessage("New damage " + e.getDamage());
            }

        }
    }
}
