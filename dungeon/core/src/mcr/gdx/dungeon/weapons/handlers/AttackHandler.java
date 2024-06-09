package mcr.gdx.dungeon.weapons.handlers;


import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.weapons.AttackRequest;

abstract public class AttackHandler extends GenericHandler {

  @Override
  public boolean handleRequest(Request request) {
    return handleAttackRequest((AttackRequest) request);
  }

  protected abstract boolean handleAttackRequest(AttackRequest request);
}
