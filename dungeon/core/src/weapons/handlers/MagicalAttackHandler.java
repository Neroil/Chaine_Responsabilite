package weapons.handlers;

public class MagicalAttackHandler extends AttackHandler {

  @Override
  public boolean handleRequest(AttackRequest request) {
    return handleRequestGeneric(request, request.getPlayer().getMana());
  }

}
