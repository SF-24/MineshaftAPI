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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class DamageListener implements Listener {

    @EventHandler
    void damageListener(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            List<EntityDamageEvent.DamageCause> defendableDamage = null;
            defendableDamage.add(EntityDamageEvent.DamageCause.PROJECTILE);
            defendableDamage.add(EntityDamageEvent.DamageCause.CONTACT);
            defendableDamage.add(EntityDamageEvent.DamageCause.THORNS);
            defendableDamage.add(EntityDamageEvent.DamageCause.ENTITY_ATTACK);
            defendableDamage.add(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
            defendableDamage.add(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK);

            if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
                e.setCancelled(true);
            }

            if(!e.isCancelled() && defendableDamage.contains(e.getCause())) {
                Player player = (Player) e.getEntity();

                // Update damage depending on defence stat
                double defence = PlayerStatManager.getPlayerStat(ItemStats.DEFENCE,player);
                double damageReduction = defence/(defence+35);
                double damage = e.getDamage();
                damage=damage*(100-damageReduction);
                e.setDamage(damage);
            }

        }
    }
}
