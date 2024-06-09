package mcr.gdx.dungeon.ChainOfResponsibility.weapons.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.weapons.AttackRequest;
import mcr.gdx.dungeon.elements.items.WeaponTile;

public class CostModifierHandler extends AttackHandler {

    private final double modifier;
    private final WeaponTile.AttackType appliedType;


    public CostModifierHandler(double modifier, WeaponTile.AttackType appliedType) {
        this.modifier = modifier;
        this.appliedType = appliedType;

    }

    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        if (request.getAttackType() == appliedType) {
            System.out.println("Cost of attack lowered!");
            request.modifyWeaponCost(modifier);
        }
        return invokeSuccessor(request);
    }

}
