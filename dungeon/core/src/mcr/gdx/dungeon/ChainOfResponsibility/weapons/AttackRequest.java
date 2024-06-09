package mcr.gdx.dungeon.ChainOfResponsibility.weapons;

import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.elements.PlayerTile;
import mcr.gdx.dungeon.elements.items.WeaponTile;

public class AttackRequest implements Request {

  private final PlayerTile player;
  private int weaponCost;
  private int weaponDamage;
  private final int weaponCooldown;
  private final long weaponLastAttack;
  private final WeaponTile.AttackType attackType;

  public AttackRequest(PlayerTile player, WeaponTile weapon) {
    this.player = player;
    this.weaponCost = weapon.getCost();
    this.weaponDamage = weapon.getDamage();
    this.weaponCooldown = weapon.getCooldown();
    this.weaponLastAttack = weapon.getLastAttack();
    this.attackType = weapon.getAttackType();
    this.timeAttack = timeAttack;
  }

  public PlayerTile getPlayer() {
    return player;
  }

  public int getWeaponCost() { return weaponCost; }

  public int getWeaponDamage() { return weaponDamage; }

  public int getWeaponCooldown() { return weaponCooldown; }

  public long getWeaponLastAttack() { return weaponLastAttack; }

  public void modifyWeaponCost(double factor) { weaponCost = (int)(weaponCost * factor); }

  public void modifyWeaponDamage(double factor) { weaponDamage *= (int)(weaponDamage * factor); }

  public WeaponTile.AttackType getAttackType() { return attackType; }
}
