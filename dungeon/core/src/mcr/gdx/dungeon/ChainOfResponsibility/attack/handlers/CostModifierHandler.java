package mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;
import mcr.gdx.dungeon.elements.items.WeaponTile;

/**
 * This handler allow us to modify the cost of the attack if the handler and the attack have the same type.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class CostModifierHandler extends AttackHandler {

    private final double modifier;
    private final WeaponTile.AttackType appliedType;

    /**
     * Construct a new CostModifierHandler
     *
     * @param modifier      the multiplicand we want to apply  to our cost
     * @param appliedType   the type of the attacks we want to modify
     */
    public CostModifierHandler(double modifier, WeaponTile.AttackType appliedType) {
        this.modifier = modifier;
        this.appliedType = appliedType;
    }

    /**
     * Check if the type of the attack matches and if they do, modify the cost by multiplying it with our modifier.
     *
     * @param request   the AttackRequest we may have to handle
     * @return          the result of the chain
     */
    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        if (request.getAttackType() == appliedType) {
            System.out.println("Cost of attack lowered!");
            request.modifyWeaponCost(modifier);
        }
        return invokeSuccessor(request);
    }

}
