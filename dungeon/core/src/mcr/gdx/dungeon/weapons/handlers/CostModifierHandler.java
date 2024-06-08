package mcr.gdx.dungeon.weapons.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.weapons.AttackRequest;

public class CostModifierHandler extends AttackHandler{

    private final int modifier;

    public CostModifierHandler(int modifier){
        this.modifier = modifier;
    }

    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        request.modifyWeaponCost(modifier);
        return invokeSuccessor(request);
    }

}
