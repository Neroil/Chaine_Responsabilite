package mcr.gdx.dungeon.ChainOfResponsibility.weapons.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.weapons.AttackRequest;

public class DamageModifierHandler extends AttackHandler{
    private final double modifier;

    public DamageModifierHandler(double modifier){
        this.modifier = modifier;
    }

    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        request.modifyWeaponDamage(modifier);
        return invokeSuccessor(request);
    }

}
