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
import com.mineshaft.mineshaftapi.manager.event.PendingAbilities;
import com.mineshaft.mineshaftapi.manager.item.ItemManager;
import com.mineshaft.mineshaftapi.manager.item.ItemStats;
import com.mineshaft.mineshaftapi.manager.item.fields.ItemSubcategoryProperty;
import com.mineshaft.mineshaftapi.manager.player.PlayerStatManager;
import com.mineshaft.mineshaftapi.manager.player.combat.BlockingType;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.mineshaftapi.util.maths.VectorUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        defendableDamage.add(EntityDamageEvent.DamageCause.SONIC_BOOM);
        defendableDamage.add(EntityDamageEvent.DamageCause.FALLING_BLOCK);
        // ? not sure
        if(e.getEntity() instanceof Player) {
            // If a player is damaged
            if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
                e.setCancelled(true);
            }

            if(defendableDamage.contains(e.getCause()) && e.getDamage()>0.0001 ) {
                Player player = (Player) e.getEntity();

                // If user is not blocking

                double dirDegrees = 0;
                if(e.getDamageSource().getSourceLocation()!=null) {
                    dirDegrees = VectorUtil.getAngleDegrees(VectorUtil.constructTopDownVector(e.getEntity().getLocation().getDirection()),
                        VectorUtil.getTopdownRelativeVector(
                            VectorUtil.constructTopDownVector((e.getEntity().getLocation())),VectorUtil.constructTopDownVector(e.getDamageSource().getSourceLocation())
                        )
                    );
                }

                if(MineshaftApi.getInstance().getActionManager().getBlockingType(player.getUniqueId())==null || dirDegrees>140) {
                    // Update damage depending on armour class stat
                    Logger.logDebug("Attack angle in degrees: " + dirDegrees);
                    e.setDamage(DamageManager.calculateNewDamage(e.getDamage(),PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS, player)));

                    // On parry
                } else if (MineshaftApi.getInstance().getActionManager().getBlockingType(player.getUniqueId()).equals(BlockingType.PARRY)) {
                    e.setCancelled(true);
                    float pitch = 0.725f + new Random().nextFloat(0.35f);
                    player.getWorld().playSound(player.getLocation(), "minecraft:sword.parry", 2.0f, pitch);
                    MineshaftApi.getInstance().getActionManager().removePlayerParry(player.getUniqueId());

                // On block //TODO: add better damage modifier
                } else if (MineshaftApi.getInstance().getActionManager().getBlockingType(player.getUniqueId()).equals(BlockingType.BLOCK)) {
                    e.setDamage(DamageManager.calculateNewDamage(e.getDamage() / 2,PlayerStatManager.getPlayerStat(ItemStats.ARMOUR_CLASS, player)));
                }
            }

        } else {

            // If a player is doing the attacking:
            if(defendableDamage.contains(e.getCause()) && e.getDamage()>0.0001) {
                int hitBonus = 0;
                double damageMultiplier = 1.0;

                if (e instanceof EntityDamageByEntityEvent entityDamageByEntityEvent && (e.getDamageSource().getCausingEntity() instanceof Player || entityDamageByEntityEvent.getDamager() instanceof Player)) {
                    Player player = null;

                    // Get player.
                    if (e.getDamageSource().getCausingEntity() instanceof Player) {
                        player = (Player) e.getDamageSource().getCausingEntity();
                    } else if (entityDamageByEntityEvent.getDamager() instanceof Player) {
                        player = (Player) (entityDamageByEntityEvent).getDamager();
                    }

                    // Check for weapon in hand and get hit bonus
                    hitBonus+= (int) getHitBonus(player, e);

                    // Test pending abilities
                    damageMultiplier = testPendingAbilitiesAndGetDamageMultiplier(player, e);

                    // Display rounded value as a damage indicator
                    double roundOff = Math.round(DamageManager.calculateNewDamage(e.getDamage()*damageMultiplier,ArmourManager.getArmourClass(e.getEntity())-hitBonus)* 100.0) / 100.0;
                    player.sendActionBar(Component.text(roundOff, NamedTextColor.DARK_RED, TextDecoration.BOLD));
                    Logger.logDebug("New AC: " + (ArmourManager.getArmourClass(e.getEntity())-hitBonus) + ", old AC: "  + ArmourManager.getArmourClass(e.getEntity()) + ", hit bonus: " + hitBonus);
                }

                // Calculate damage and apply it
                if(ArmourManager.getArmourClass(e.getEntity())>0) {
                    e.setDamage(DamageManager.calculateNewDamage(e.getDamage()*damageMultiplier,Math.max(ArmourManager.getArmourClass(e.getEntity())-hitBonus, -10)));
                }
            }
        }
    }


    public static Double testPendingAbilitiesAndGetDamageMultiplier(Player player, EntityDamageEvent e) {
        if(MineshaftApi.getInstance().getPendingAbilities(player.getUniqueId())!=null && MineshaftApi.getInstance().getPendingAbilities(player.getUniqueId()).getPendingAbility(PendingAbilities.PendingAbilityType.STRONG_ATTACK)!=null) {
            Logger.logDebug("Detected!");
            PendingAbilities.PendingAbility pendingAbility = MineshaftApi.getInstance().getPendingAbilities(player.getUniqueId()).getPendingAbility(PendingAbilities.PendingAbilityType.STRONG_ATTACK);
            if(pendingAbility!=null) {
                // Remove the pending ability
                MineshaftApi.getInstance().removePendingAility(player.getUniqueId(),PendingAbilities.PendingAbilityType.STRONG_ATTACK);

                double knockbackMultiplier;

                // Mechanics
                knockbackMultiplier = Objects.requireNonNullElse(pendingAbility.doubleParams.get("KnockbackPower"), 1.0);
                boolean particles = Objects.requireNonNullElse(pendingAbility.stringParams.get("Particles"), "true").equalsIgnoreCase("true");
                String attackSound = pendingAbility.stringParams.get("AttackSound");
                if (attackSound != null) {
                    player.getWorld().playSound(player.getLocation(), attackSound, 1.0f, 1.0f);
                }
                if (particles) {
                    player.getWorld().spawnParticle(Particle.SMOKE, e.getEntity().getLocation(), 40, 1.0, 1.0, 1.0, 0);
                }
                Vector playerDir = e.getEntity().getLocation().getDirection();
                playerDir.multiply(-1);
                playerDir.multiply(0.75 * knockbackMultiplier);// lowered velocity
                playerDir.setY(0.25);
                e.getEntity().setVelocity(e.getEntity().getVelocity().multiply(playerDir));
                return Objects.requireNonNullElse(pendingAbility.doubleParams.get("DamageMultiplier"), 1.0);
            }
        }
        return 1d;
    }

    public double getHitBonus(Player player, EntityDamageEvent e) {
        double hitBonus=0d;
        ItemStack item = ((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInMainHand();
        if (!item.getType().equals(Material.AIR)) {

            // If the player is proficient with the item.
            if (JsonPlayerBridge.getWeaponProficiencies(player).contains(ItemManager.getItemSubcategory(item).name().toLowerCase())) {
                Logger.logDebug("Player is proficient. Adding proficiency bonus");
                // Make variable
                hitBonus += PlayerStatManager.getProficiencyBonus(JsonPlayerBridge.getLevel(player));
            }
            // The player is proficient with the given item
            if (ItemManager.getItemSubcategory(item).getPropertyList().contains(ItemSubcategoryProperty.FINESSE) || (
                    (JsonPlayerBridge.getAbilities(player).containsKey("weapon_finesse")||JsonPlayerBridge.getAbilities(player).containsKey("natural_finesse")) &&
                    (ItemManager.getItemSubcategory(item).getPropertyList().contains(ItemSubcategoryProperty.LIGHT) || ItemManager.getItemSubcategory(item).getPropertyList().contains(ItemSubcategoryProperty.TRAINED_FINESSE))
            )) {
                Logger.logDebug("Adding finesse properties...");
                hitBonus += Math.max(JsonPlayerBridge.getAttribute(player, "DEX"), JsonPlayerBridge.getAttribute(player, "STR"));
            } else {
                hitBonus += JsonPlayerBridge.getAttribute(player, "STR");
            }
        }
        return hitBonus;
    }
}
