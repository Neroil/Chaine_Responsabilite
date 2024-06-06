package weapons;

import weapons.handlers.RessourceHandler;
import weapons.handlers.MagicalRessourceHandler;
import weapons.handlers.PhysicalRessourceHandler;

public class Chain {

  public Chain() {
    RessourceHandler magicalHandler = new MagicalRessourceHandler();
    RessourceHandler physicalHandler = new PhysicalRessourceHandler();

    magicalHandler.setSuccessor(physicalHandler);
  }

}
