package mcr.gdx.dungeon.ChainOfResponsibility.weapons.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.weapons.AttackRequest;

public class CooldownHandler extends AttackHandler {

  @Override
  protected boolean handleAttackRequest(AttackRequest request) {
    long weaponLastAttack = request.getWeaponLastAttack();
    int cooldown = request.getWeaponCooldown();

    if (weaponLastAttack == -1 || (request.getTimeAttack() - weaponLastAttack) > cooldown)
      return invokeSuccessor(request);
    else {
      System.out.println("Weapon is on cooldown!");
      return false;
    }
  }

}
