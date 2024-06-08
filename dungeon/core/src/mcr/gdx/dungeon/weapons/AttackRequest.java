package mcr.gdx.dungeon.weapons;

import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.elements.PlayerTile;
import mcr.gdx.dungeon.elements.WeaponTile;

public class AttackRequest implements Request {

  private final PlayerTile player;
  private int weaponCost;
  private int weaponDamage;
  private final int weaponCooldown;
  private final long weaponLastAttack;

  public AttackRequest(PlayerTile player, WeaponTile weapon) {
    this.player = player;
    this.weaponCost = weapon.getCost();
    this.weaponDamage = weapon.getDamage();
    this.weaponCooldown = weapon.getCooldown();
    this.weaponLastAttack = weapon.getLastAttack();
  }

  public PlayerTile getPlayer() {
    return player;
  }

  public int getWeaponCost() { return weaponCost; }

  public int getWeaponDamage() { return weaponDamage; }

  public int getWeaponCooldown() { return weaponCooldown; }

  public long getWeaponLastAttack() { return weaponLastAttack; }

  public void modifyWeaponCost(int factor) { weaponCost *= factor; }

  public void modifyWeaponDamage(int factor) { weaponDamage *= factor; }

}
