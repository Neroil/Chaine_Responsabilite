package weapons.handlers;

import characters.*;
import weapons.PhysicalWeapon;

public class PhysicalAttackRequest extends AttackRequest {

  public PhysicalAttackRequest(Player player, PhysicalWeapon weapon, Enemy enemy) {
    super(player, weapon, enemy);
  }

}
