package weapons;

import weapons.handlers.AttackHandler;
import weapons.handlers.MagicalAttackHandler;
import weapons.handlers.PhysicalAttackHandler;

public class Chain {

  public Chain() {
    AttackHandler magicalHandler = new MagicalAttackHandler();
    AttackHandler physicalHandler = new PhysicalAttackHandler();

    magicalHandler.setSuccessor(physicalHandler);
  }

}
