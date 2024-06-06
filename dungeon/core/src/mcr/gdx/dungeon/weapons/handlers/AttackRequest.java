package mcr.gdx.dungeon.weapons.handlers;

import mcr.gdx.dungeon.characters.Player;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.weapons.Weapon;

import java.sql.Timestamp;

public abstract class AttackRequest implements Request {

  private final Player player;
  private int weaponCost;
  private int weaponDamage;
  private final int weaponCooldown;
  private final long weaponLastAttack;

  public AttackRequest(Player player, Weapon weapon) {
    this.player = player;
    this.weaponCost = weapon.getCost();
    this.weaponDamage = weapon.getDamage();
    this.weaponCooldown = weapon.getCooldown();
    this.weaponLastAttack = weapon.getLastAttack();
  }

  public Player getPlayer() {
    return player;
  }

  public int getWeaponCost() { return weaponCost; }

  public int getWeaponDamage() { return weaponDamage; }

  public int getWeaponCooldown() { return weaponCooldown; }

  public long getWeaponLastAttack() { return weaponLastAttack; }

  public void modifyWeaponCost(int factor) { weaponCost *= factor; }

  public void modifyWeaponDamage(int factor) { weaponDamage *= factor; }

}
