package mcr.gdx.dungeon.weapons.handlers;

import mcr.gdx.dungeon.weapons.AttackRequest;

public class CooldownHandler extends AttackHandler {

  @Override
  protected boolean handleAttackRequest(AttackRequest request) {
    long weaponLastAttack = request.getWeaponLastAttack();
    int cooldown = request.getWeaponCooldown();

    if (weaponLastAttack == -1 || (System.currentTimeMillis() - weaponLastAttack) > cooldown)
      return invokeSuccessor(request);
    else
      return false;
  }
}
