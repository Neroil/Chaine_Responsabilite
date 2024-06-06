package mcr.gdx.dungeon.weapons.handlers;

public abstract class RessourceHandler extends AttackHandler {

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
