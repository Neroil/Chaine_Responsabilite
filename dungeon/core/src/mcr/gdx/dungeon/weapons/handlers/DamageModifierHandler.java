package mcr.gdx.dungeon.weapons.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.characters.DamageRequest;
import mcr.gdx.dungeon.weapons.AttackRequest;

public class DamageModifierHandler extends AttackHandler{
    private final int modifier;


    public DamageModifierHandler(int modifier){
        this.modifier = modifier;
    }

    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        request.modifyWeaponDamage(modifier);
        return invokeSuccessor(request);
    }

}
