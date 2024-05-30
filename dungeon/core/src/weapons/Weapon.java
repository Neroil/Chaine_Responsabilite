package weapons;

import java.sql.Timestamp;

public abstract class Weapon {
  private final String name;
  private final int damage;
  private final int cooldown;
  private Timestamp lastAttack;
  private boolean available;
  private final int range;
  private final int cost;

  public Weapon(String name, int damage, int cooldown, int range, int cost) {
    this.name = name;
    this.damage = damage;
    this.cooldown = cooldown;
    this.available = true;
    this.range = range;
    this.cost = cost;
  }

  public String getName() { return name; }

  public int getDamage() { return damage; }

  public int getCooldown() { return cooldown; }

  public void setLastAttack() {
    lastAttack = new Timestamp(System.currentTimeMillis());
  }

  public boolean isAvailable() {
    return lastAttack == null || (System.currentTimeMillis() - lastAttack.getTime()) > cooldown;
  }

  public int getRange() { return range; }

  public int getCost() { return cost; }

}
