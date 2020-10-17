package com.sylvcraft.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import com.sylvcraft.CreeperEggLaunch;

public class creepeggcfg implements TabExecutor {
  CreeperEggLaunch plugin;
  
  public creepeggcfg(CreeperEggLaunch instance) {
    plugin = instance;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    List<String> options = new ArrayList<>();    

    switch (args.length) {
    case 1:
      if (sender.hasPermission("creeperegglaunch.admin.velocity")) options.add("velocity");
      if (sender.hasPermission("creeperegglaunch.admin.loretitle")) options.add("loretitle");
      break;
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
    if (args.length == 0) {
      showHelp(sender);
      return true;
    }
    
    Map<String, String> data = new HashMap<>();
    switch (args[0].toLowerCase()) {
    case "velocity":
      if (!sender.hasPermission("creeperegglaunch.admin.velocity")) {
        plugin.msg("access-denied", sender);
        return true;
      }
      if (args.length < 2) {
        showHelp(sender);
        return true;
      }
      
      double velocity = plugin.getDouble(args[1]);
      if (velocity == -1 || velocity == 0) {
        plugin.msg("invalid-velocity-value", sender);
        return true;
      }

      plugin.getConfig().set("config.velocityMultiplier", velocity);
      plugin.saveConfig();
      data.put("%value%", args[1]);
      plugin.msg("velocity-set", sender, data);
      break;
      
    case "loretitle":
      if (!sender.hasPermission("creeperegglaunch.admin.loretitle")) {
        plugin.msg("access-denied", sender);
        return true;
      }
      if (args.length < 2) {
        showHelp(sender);
        return true;
      }
      
      String lore = StringUtils.join(args, " ", 1, args.length);
      plugin.getConfig().set("localization.components.loreTitle", lore);
      plugin.saveConfig();
      data.put("%value%", lore);
      plugin.msg("lore-set", sender, data);
      break;
      
    default:
      showHelp(sender);
    }
    return true;
  }
  
  private void showHelp(CommandSender sender) {
    int displayed = 0;
    for (String option : Arrays.asList("velocity","loretitle")) {
      if (sender.hasPermission("creeperegglaunch.admin." + option)) {
        plugin.msg("help-" + option, sender); 
        displayed++;
      }
    }
    if (displayed == 0) plugin.msg("access-denied", sender);
  }
}
