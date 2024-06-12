package mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;

public class DamageModifierHandler extends AttackHandler {
    private final double modifier;

    public DamageModifierHandler(double modifier) {
        this.modifier = modifier;
    }

    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        System.out.println("Attack damage modified!!");
        request.modifyWeaponDamage(modifier);
        return invokeSuccessor(request);
    }

}
