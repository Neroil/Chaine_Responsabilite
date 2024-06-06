package weapons.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.characters.Enemy;
import mcr.gdx.dungeon.characters.Player;
import weapons.Weapon;

public abstract class AttackHandler {

  private AttackHandler successor;

  public AttackHandler setSuccessor(AttackHandler successor) {
    this.successor = successor;
    return successor;
  }

  protected abstract boolean checkResources(AttackRequest request);

  protected abstract void updateResources(AttackRequest request);

  @Override
  protected boolean handleAttackRequest(AttackRequest request) {
    if (checkResources(request)) {
      updateResources(request);
      return invokeSuccessor(request);
    } else {
      System.out.println("Not enough ressource!");
      return false; // Break the chain
    }
  }
}
