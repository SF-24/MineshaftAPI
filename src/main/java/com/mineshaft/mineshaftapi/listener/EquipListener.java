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
import com.mineshaft.mineshaftapi.manager.player.ArmourEquipManager;
import com.mineshaft.mineshaftapi.manager.player.json.JsonPlayerBridge;
import com.mineshaft.mineshaftapi.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class EquipListener implements Listener {


    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {

        try {
            Logger.logInfo("current: " + Objects.requireNonNull(e.getCurrentItem()).getType());
            Logger.logInfo("cursor: " + Objects.requireNonNull(e.getCursor()).getType());
            Logger.logInfo("action: " + e.getAction());
            Logger.logInfo("slot type: " + e.getSlotType());
            Logger.logInfo("click type: " + e.getClick());
            Logger.logInfo("hotbar button: " + e.getHotbarButton());
            Logger.logInfo("slot: " + e.getSlot());
//        if(e.getWhoClicked().getInventory().getItem(e.getHotbarButton())!=null) {
//            Logger.logInfo("hotbar item: " + e.getWhoClicked().getInventory().getItem(e.getHotbarButton()).getType());
//        }
        } catch (Exception npe) {
            Logger.logDebug("Could not execute interact debug.");
        }
        if (e.getInventory().getHolder() != null && e.getView().getType().equals(InventoryType.PLAYER)) {
            if(e.getClick().equals(ClickType.NUMBER_KEY)) {
                e.setCancelled(true);
            } else if (e.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                // if player clicks on armour slot
                ItemStack cursor = e.getCursor();
                if (ArmourEquipManager.tryEquip((Player) e.getWhoClicked(), e.getCursor())) e.setCancelled(true);
                JsonPlayerBridge.getTempArmourClass((Player) e.getWhoClicked());
            } else if (e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) {
                // if player shift clicks to equip armour
                //if(!hasEquippedItem((Player) e.getWhoClicked(),e.getCurrentItem()) && isArmour(e.getCurrentItem())) {
                if(ArmourEquipManager.tryEquip((Player) e.getWhoClicked(),e.getCurrentItem())) { //ArmourEquipManager.tryEquip((Player) e.getWhoClicked(),e.getCurrentItem())) {
                    e.setCancelled(true);
                    //e.setCancelled(ArmourEquipManager.tryEquip((Player) e.getWhoClicked(), e.getCurrentItem()));
                }
                JsonPlayerBridge.getTempArmourClass((Player) e.getWhoClicked());

                // If the player swaps with the hotbar slot to equip armour
            } else if(e.getAction().equals(InventoryAction.HOTBAR_SWAP) /*&& e.getSlotType().equals(InventoryType.SlotType.ARMOR)*/) {
                e.setCancelled(true);
                if(isArmour(e.getWhoClicked().getInventory().getItem(e.getHotbarButton()))) {
                    e.setCancelled(ArmourEquipManager.tryEquip((Player) e.getWhoClicked(), e.getWhoClicked().getInventory().getItem(e.getHotbarButton())));
                }
                JsonPlayerBridge.getTempArmourClass((Player) e.getWhoClicked());

            }
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(MineshaftApi.getInstance(),()->{
            // post equip armour check
            Player player = (Player) e.getWhoClicked();
            for(int i = 0; i<4; i++) {
                boolean forbidden=false;
                ItemStack item = new ItemStack(Material.AIR);
                switch (i) {
                    case 0 -> {
                        forbidden = ArmourEquipManager.tryEquip(player, player.getInventory().getBoots());
                        item=player.getInventory().getBoots();
                        break;
                    }
                    case 1 -> {
                        forbidden = ArmourEquipManager.tryEquip(player, player.getInventory().getLeggings());
                        item=player.getInventory().getLeggings();
                        break;
                    }
                    case 2 -> {
                        forbidden = ArmourEquipManager.tryEquip(player, player.getInventory().getChestplate());
                        item=player.getInventory().getChestplate();
                        break;
                    }
                    case 3 -> {
                        forbidden = ArmourEquipManager.tryEquip(player, player.getInventory().getHelmet());
                        item=player.getInventory().getHelmet();
                        break;
                    }
                }
                if(forbidden) {
                    switch(i) {
                        case 0 -> {
                            player.getInventory().setBoots(new ItemStack(Material.AIR));
                        }
                        case 1 -> {
                            player.getInventory().setLeggings(new ItemStack(Material.AIR));
                        }
                        case 2 -> {
                            player.getInventory().setChestplate(new ItemStack(Material.AIR));
                        }
                        case 3 -> {
                            player.getInventory().setHelmet(new ItemStack(Material.AIR));
                        }
                    }
                    if(hasInventorySpace(player) && item!=null) {player.getInventory().addItem(item);}
                    else if(item!=null) {player.getWorld().dropItem(player.getLocation(),item);}
                }
            }
            JsonPlayerBridge.setTempArmourClass((Player) e.getWhoClicked());
        },1/20);
    }

    @EventHandler
    void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (isArmour(e.getItem())) {
                e.setCancelled(ArmourEquipManager.tryEquip(e.getPlayer(), e.getItem()));
            }
        }
    }

    @EventHandler
    void onDispense(BlockDispenseArmorEvent e) {
        if (e.getTargetEntity() instanceof Player) {
            if (ArmourEquipManager.tryEquip((Player) e.getTargetEntity(), e.getItem())) {
                e.getTargetEntity().getWorld().dropItem(e.getTargetEntity().getLocation(), e.getItem());
                e.setItem(new ItemStack(Material.AIR));
            }
        }
    }

    public boolean hasEquippedItem(Player player, ItemStack item) {
        return compareItems(item, player.getInventory().getHelmet()) || compareItems(item, player.getInventory().getChestplate()) || compareItems(item, player.getInventory().getLeggings()) || compareItems(item, player.getInventory().getBoots());
    }

    public boolean compareItems(ItemStack item1, ItemStack item2) {
        if (item1 == null && item2 == null) return true;
        if (item1 == null || item2 == null) return false;
        return item1.serialize().equals(item2.serialize());
    }

    public boolean hasEquippedItemInArmourSlotOfGivenItem(Player player, ItemStack item) {
        final String typeNameString = item.getType().name();
        if(typeNameString.endsWith("_LEGGINGS")) {
            return player.getInventory().getLeggings() == null || player.getInventory().getLeggings().getType().equals(Material.AIR);
        } else if(typeNameString.endsWith("_CHESTPLATE")) {
            return player.getInventory().getChestplate() == null || player.getInventory().getChestplate().getType().equals(Material.AIR);
        } else if(typeNameString.endsWith("_BOOTS")) {
            return player.getInventory().getBoots() == null || player.getInventory().getBoots().getType().equals(Material.AIR);
        } else if(typeNameString.endsWith("_HELMET") || typeNameString.endsWith("_HEAD")) {
            return player.getInventory().getHelmet() == null || player.getInventory().getHelmet().getType().equals(Material.AIR);
        }
        return false;
    }

    public boolean hasArmourInSlot(Player player, int slot) {
        final String typeNameString = Objects.requireNonNull(player.getInventory().getItem(slot)).getType().name();
        if(slot==37&&typeNameString.endsWith("_LEGGINGS")) {
            return player.getInventory().getLeggings() == null || player.getInventory().getLeggings().getType().equals(Material.AIR);
        } else if(slot==38&&typeNameString.endsWith("_CHESTPLATE")) {
            return player.getInventory().getChestplate() == null || player.getInventory().getChestplate().getType().equals(Material.AIR);
        } else if(slot==36&&typeNameString.endsWith("_BOOTS")) {
            return player.getInventory().getBoots() == null || player.getInventory().getBoots().getType().equals(Material.AIR);
        } else if(slot==39&&(typeNameString.endsWith("_HELMET") || typeNameString.endsWith("_HEAD"))) {
            return player.getInventory().getHelmet() == null || player.getInventory().getHelmet().getType().equals(Material.AIR);
        }
        return false;
    }

    @EventHandler
    void closeInventory(InventoryCloseEvent e) {
//        for(int i=36; i<40; i++) {
//            if(e.getPlayer().getInventory().getItem(i)!=null && hasArmourInSlot((Player) e.getPlayer(),i)) {
//                if(ArmourEquipManager.tryEquip((Player) e.getPlayer(), e.getPlayer().getInventory().getItem(i))) {
//                    e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), (e.getPlayer().getInventory().getItem(i)));
//                    e.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
//                }
//            }
//        }


    }


    public boolean isArmour(ItemStack item) {
        if(item==null) return false;
        final String typeNameString = item.getType().name();
        if(item.getType().equals(Material.PLAYER_HEAD)) return true;
        return typeNameString.endsWith("_HELMET")
                || typeNameString.endsWith("_CHESTPLATE")
                || typeNameString.endsWith("_LEGGINGS")
                || typeNameString.endsWith("_BOOTS");
    }

    public static boolean hasInventorySpace(Player player) {
        for(int i = 0; i<36;i++) {
            if(player.getInventory().getItem(i)==null || player.getInventory().getItem(i).getType().equals(Material.AIR)) return true;
        }
        return false;
    }
}