package mcr.gdx.dungeon.weapons.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.elements.items.WeaponTile;
import mcr.gdx.dungeon.weapons.AttackRequest;

public class CostModifierHandler extends AttackHandler{

    private final double modifier;
    private final WeaponTile.AttackType appliedType;


    public CostModifierHandler(double modifier, WeaponTile.AttackType appliedType){
        this.modifier = modifier;
        this.appliedType = appliedType;

    }

    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        if(request.getAttackType() == appliedType){
            request.modifyWeaponCost(modifier);
        }
        return invokeSuccessor(request);
    }

}
