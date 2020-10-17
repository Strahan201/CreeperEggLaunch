package com.sylvcraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import com.sylvcraft.commands.creepegg;
import com.sylvcraft.commands.creepeggcfg;
import com.sylvcraft.events.PlayerInteract;
import com.sylvcraft.events.ProjectileHit;

public class CreeperEggLaunch extends JavaPlugin {
  List<CreeperEgg> creeperEggs = new ArrayList<>();
  
  @Override
  public void onEnable() {
    saveDefaultConfig();
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new ProjectileHit(this), this);
    pm.registerEvents(new PlayerInteract(this), this);
    getCommand("creepegg").setExecutor(new creepegg(this));
    getCommand("creepeggcfg").setExecutor(new creepeggcfg(this));
  }

  public CreeperEgg getCreeperEgg(Entity e) {
    CreeperEgg tmp = new CreeperEgg(e);
    if (!creeperEggs.contains(tmp)) return null;
    
    return creeperEggs.get(creeperEggs.indexOf(tmp));
  }
  
  public void spawnCreeper(Entity e) {
    CreeperEgg ce = getCreeperEgg(e);
    if (ce == null) return;
    
    Creeper c = (Creeper)e.getWorld().spawnEntity(e.getLocation(), EntityType.CREEPER);
    c.setPowered(ce.isCharged);
    c.setExplosionRadius(ce.explosionRadius);
    c.setMaxFuseTicks(ce.maxFuseTicks);
    c.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(ce.health);
    c.setHealth(ce.health);
    if (ce.isIgnited) c.ignite();
  }
  
  public void launchEgg(Player p) {
    Map<String, String> localization = getLocalization();
    ItemStack i = p.getInventory().getItemInMainHand();
    Snowball snowball = p.launchProjectile(Snowball.class);
    Vector velocity = p.getLocation().getDirection();
    velocity.multiply(getConfig().getDouble("config.velocityMultiplier", 1));
    snowball.setVelocity(velocity);
    
    CreeperEgg ce = new CreeperEgg(snowball);
    for (String s : i.getItemMeta().getLore()) {
      if (s.toLowerCase().contains(localization.get("charged.lore").toLowerCase())) ce.isCharged = true;
      if (s.toLowerCase().contains(localization.get("ignited.lore").toLowerCase())) ce.isIgnited = true;
      if (s.toLowerCase().contains(localization.get("fuse.lore").toLowerCase())) ce.maxFuseTicks = getInteger(s.replace(localization.get("fuse.lore"), ""));
      if (s.toLowerCase().contains(localization.get("radius.lore").toLowerCase())) ce.explosionRadius = getInteger(s.replace(localization.get("radius.lore"), ""));
      if (s.toLowerCase().contains(localization.get("health.lore").toLowerCase())) ce.health = getDouble(s.replace(localization.get("health.lore"), ""));
    }
    
    creeperEggs.add(ce);
  }
  
  public Map<String, String> getLocalization() {
    Map<String, String> ret = new HashMap<>();
    ret.put("fuse.lore", getConfig().getString("localization.components.fuse.lore", "Fuse ticks: "));
    ret.put("fuse.arg", getConfig().getString("localization.components.fuse.arg", "fuse"));
    ret.put("radius.lore", getConfig().getString("localization.components.radius.lore", "Explosion radius: "));
    ret.put("radius.arg", getConfig().getString("localization.components.radius.arg", "radius"));
    ret.put("health.lore", getConfig().getString("localization.components.health.lore", "Health: "));
    ret.put("health.arg", getConfig().getString("localization.components.health.arg", "health"));
    ret.put("charged.lore", getConfig().getString("localization.components.charged.lore", "Creeper will be charged"));
    ret.put("charged.arg", getConfig().getString("localization.components.charged.arg", "charged"));
    ret.put("ignited.lore", getConfig().getString("localization.components.ignited.lore", "Creeper will ignite"));
    ret.put("ignited.arg", getConfig().getString("localization.components.ignited.arg", "ignited"));
    ret.put("displayName", getConfig().getString("localization.components.displayName", ""));
    ret.put("loreTitle", getConfig().getString("localization.components.loreTitle", "&6Spawns a creeper where the egg lands"));
    return ret;
  }
  
  public void msg(String msgCode, CommandSender sender) {
  	if (getConfig().getString("localization.messages." + msgCode) == null) return;
  	msgTransmit(getConfig().getString("localization.messages." + msgCode), sender);
  }

  public void msg(String msgCode, CommandSender sender, Map<String, String> data) {
  	if (getConfig().getString("localization.messages." + msgCode) == null) return;
  	String tmp = getConfig().getString("localization.messages." + msgCode, msgCode);
  	for (Map.Entry<String, String> mapData : data.entrySet()) {
  	  tmp = tmp.replace(mapData.getKey(), mapData.getValue());
  	}
  	msgTransmit(tmp, sender);
  }
  
  public void msgTransmit(String msg, CommandSender sender) {
  	for (String m : (msg + " ").split("%br%")) {
  		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
  	}
  }

  public int getInteger(String val) {
    int ret = 0;
    try {
      ret = Integer.parseInt(val);
      return ret;
    } catch (NumberFormatException e) {
    }
    return -1;
  }
  
  public double getDouble(String val) {
    double ret = 0;
    try {
      ret = Double.parseDouble(val);
      return ret;
    } catch (NumberFormatException e) {
    }
    return -1;
  }
}