package weapons.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;

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
