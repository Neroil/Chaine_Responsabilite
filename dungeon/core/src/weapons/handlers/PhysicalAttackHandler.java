package weapons.handlers;

public class PhysicalAttackHandler extends AttackHandler {

  @Override
  public boolean handleRequest(AttackRequest request) {
    return handleRequestGeneric(request, request.getPlayer().getVigor());
  }

}
