package com.sylvcraft;

import java.util.UUID;
import org.bukkit.entity.Entity;

public class CreeperEgg {
  public UUID uuid;
  public Boolean isCharged = false;
  public Boolean isIgnited = false;
  public Boolean isInvulnerable = false;
  public int explosionRadius = 3;
  public int maxFuseTicks = 30;
  public double health = 20;
  
  public CreeperEgg(Entity e) {
    uuid = e.getUniqueId();
  }
  
  public CreeperEgg(Entity e, Boolean isCharged, Boolean isIgnited) {
    uuid = e.getUniqueId();
    this.isCharged = isCharged;
    this.isIgnited = isIgnited;
  }
  
  @Override
  public boolean equals(Object checkValue) {
    if (!(checkValue instanceof CreeperEgg)) return false;
    return (((CreeperEgg)checkValue).uuid == this.uuid);
  }
}
