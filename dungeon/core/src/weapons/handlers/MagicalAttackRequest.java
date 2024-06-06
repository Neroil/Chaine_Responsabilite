package weapons.handlers;

import characters.*;
import weapons.PhysicalWeapon;

public class MagicalAttackRequest extends AttackRequest {

  public MagicalAttackRequest(Player player, PhysicalWeapon weapon, Enemy enemy) {
    super(player, weapon, enemy);
  }

}
