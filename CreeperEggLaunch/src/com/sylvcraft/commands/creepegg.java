package com.sylvcraft.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.sylvcraft.CreeperEggLaunch;
import net.md_5.bungee.api.ChatColor;

public class creepegg implements TabExecutor {
  CreeperEggLaunch plugin;
  
  public creepegg(CreeperEggLaunch instance) {
    plugin = instance;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    Map<String, String> localization = plugin.getLocalization();
    List<String> options = new ArrayList<>();
    for (String feature : Arrays.asList("ignited","charged","health","fuse","radius")) {
      if (sender.hasPermission("creeperegglaunch.options." + feature)) options.add(localization.get(feature + ".arg"));
    }

    for (String arg : args) {
      Iterator<String> i = options.iterator();
      while (i.hasNext()) {
        String opt = i.next();
        if (arg.length() >= opt.length() && arg.substring(0, opt.length()).equalsIgnoreCase(opt)) i.remove();
      }
    }
    
    return getMatchedAsType(args.length == 0?"":args[args.length-1], options);
  }
  
  List<String> getMatchedAsType(String typed, List<String> values) {
    List<String> ret = new ArrayList<String>();
    for (String element : values) if (element.startsWith(typed)) ret.add(element);
    return ret;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      plugin.msg("players-only", sender);
      return true;
    }
    
    if (!sender.hasPermission("creeperegglaunch.creepegg.create")) {
      plugin.msg("access-denied", sender);
      return true;
    }
    
    Map<String, String> localization = plugin.getLocalization();
    List<String> eggLore = new ArrayList<>(Arrays.asList(ChatColor.translateAlternateColorCodes('&', localization.get("loreTitle")), ""));

    for (String arg : args) {
      if (sender.hasPermission("creeperegglaunch.options.charged") && arg.equalsIgnoreCase(localization.get("charged.arg"))) eggLore.add(localization.get("charged.lore"));
      if (sender.hasPermission("creeperegglaunch.options.ignited") && arg.equalsIgnoreCase(localization.get("ignited.arg"))) eggLore.add(localization.get("ignited.lore"));
      if (!arg.contains(":")) continue;
      
      String[] opt = arg.toLowerCase().split(":");
      if (opt.length != 2) continue;
      for (String feature : Arrays.asList("fuse","radius","health")) {
        if (!opt[0].equals(localization.get(feature + ".arg"))) continue;
        if (!sender.hasPermission("creeperegglaunch.options." + feature)) continue;

        if (plugin.getDouble(opt[1]) == -1) {
          plugin.msg("invalid-" + feature + "-value", sender);
          continue;
        }
        eggLore.add(localization.get(feature + ".lore") + (int)plugin.getDouble(opt[1]));
      }
    }
    
    ItemStack i = new ItemStack(Material.CREEPER_SPAWN_EGG);
    ItemMeta im = i.getItemMeta();
    if (!localization.get("displayName").equals("")) im.setDisplayName(ChatColor.translateAlternateColorCodes('&', localization.get("displayName")));
    im.setLore(eggLore);
    i.setItemMeta(im);
    Player p = (Player)sender;
    p.getInventory().addItem(i);
    return true;
  }
}
