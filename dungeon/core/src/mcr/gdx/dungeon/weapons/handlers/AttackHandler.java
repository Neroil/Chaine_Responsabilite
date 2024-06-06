package mcr.gdx.dungeon.weapons.handlers;


import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;

abstract public class AttackHandler extends GenericHandler {

  @Override
  protected boolean handleRequest(Request request) {
    return handleAttackRequest((AttackRequest) request);
  }

  protected abstract boolean handleAttackRequest(AttackRequest request);
}
