package com.sylvcraft.events;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import com.sylvcraft.CreeperEggLaunch;

import net.md_5.bungee.api.ChatColor;

public class PlayerInteract implements Listener {
  CreeperEggLaunch plugin;
  
  public PlayerInteract(CreeperEggLaunch instance) {
    plugin = instance;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e) {
    if (e.getHand() != EquipmentSlot.HAND) return;
    
    ItemStack i = e.getPlayer().getInventory().getItemInMainHand();
    if (i.getType() != Material.CREEPER_SPAWN_EGG) return;
    if (!i.hasItemMeta() || !i.getItemMeta().hasLore()) return;

    Map<String, String> localization = plugin.getLocalization();
    if (!i.getItemMeta().getLore().contains(ChatColor.translateAlternateColorCodes('&', localization.get("loreTitle")))) return;
    
    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
      e.setCancelled(true);
      return;
    }
    
    if (!e.getPlayer().hasPermission("creeperegglaunch.creepegg.launch")) return;
    
    plugin.launchEgg(e.getPlayer());
  }
}