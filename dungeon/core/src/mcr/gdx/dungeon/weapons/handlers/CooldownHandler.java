package mcr.gdx.dungeon.weapons.handlers;

public class CooldownHandler extends AttackHandler {

    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        if(request.getWeapon().isAvailable()){
            return invokeSuccessor(request);
        }else {
            return false;
        }
    }
}
