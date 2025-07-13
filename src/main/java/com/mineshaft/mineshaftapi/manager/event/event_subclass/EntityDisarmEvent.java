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

package com.mineshaft.mineshaftapi.manager.event.event_subclass;

import com.mineshaft.mineshaftapi.events.MineshaftEntityDisarmEvent;
import com.mineshaft.mineshaftapi.manager.event.Event;
import com.mineshaft.mineshaftapi.manager.event.fields.EventType;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

@Getter @Setter
public class EntityDisarmEvent extends Event {

    boolean rollToDisarm = true;
    boolean knockback = false;
    boolean useFacingItemVector = true;
    int baseStrength = 5;
    Vector itemVector = new Vector(0, 1, 0);
    LivingEntity caster = null;

    public EntityDisarmEvent(EventType type) {
        super(type);
    }

    public void disarmEntity(LivingEntity target) {
        boolean disarm = false;

        if(rollToDisarm) {
            Random random = new Random();
            int targetResist = 5;
            int targetModifier = 0;

            if(target instanceof Player playerTarget) {
                targetModifier += Math.max(JsonPlayerBridge.getAbilityScoreModifier(playerTarget,"str"),JsonPlayerBridge.getAbilityScoreModifier(playerTarget,"dex"));
            }

            int targetRoll = random.nextInt(6)+targetResist+targetModifier;

            // TODO: add base strength
            int casterRoll = baseStrength;

            if (casterRoll > targetRoll) {
                disarm = true;
            }
        } else {
            disarm = true;
        }

        if (disarm && target.getEquipment() != null) {
            ItemStack handItem = target.getEquipment().getItemInMainHand();
            ItemStack offHandItem = target.getEquipment().getItemInOffHand();

            ItemStack item = null;

            if (handItem.getType() != Material.AIR) {
                target.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
                item = handItem;
            } else if (offHandItem.getType() != Material.AIR) {
                target.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
                item = offHandItem;
            }

            // Call event
            MineshaftEntityDisarmEvent event = new MineshaftEntityDisarmEvent(target, item);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if(!event.isCancelled()) {
                if (item != null) {
                    Item dropItem = target.getWorld().dropItem(target.getLocation().add(0, 1, 0), item);
                    dropItem.setVelocity(itemVector.normalize());
                }
            }

            if (knockback && caster!=null) {
                target.setVelocity(caster.getLocation().getDirection().add(caster.getLocation().getDirection()));
            }
        }
    }
}
