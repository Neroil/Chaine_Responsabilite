package mcr.gdx.dungeon.weapons;

import mcr.gdx.dungeon.weapons.handlers.MagicalRessourceHandler;
import mcr.gdx.dungeon.weapons.handlers.PhysicalRessourceHandler;
import mcr.gdx.dungeon.weapons.handlers.RessourceHandler;

public class Chain {

  public Chain() {
    RessourceHandler magicalHandler = new MagicalRessourceHandler();
    RessourceHandler physicalHandler = new PhysicalRessourceHandler();

    magicalHandler.setSuccessor(physicalHandler);
  }

}
