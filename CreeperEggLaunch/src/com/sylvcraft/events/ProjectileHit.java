package com.sylvcraft.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import com.sylvcraft.CreeperEggLaunch;

public class ProjectileHit implements Listener {
  CreeperEggLaunch plugin;
  
  public ProjectileHit(CreeperEggLaunch instance) {
    plugin = instance;
  }

  @EventHandler
  public void onProjectileHit(ProjectileHitEvent e) {
    plugin.spawnCreeper(e.getEntity());
  }
}