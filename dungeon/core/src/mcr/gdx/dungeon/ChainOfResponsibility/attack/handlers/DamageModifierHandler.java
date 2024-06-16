package mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;


/**
 * This class allow us to modify the damage of the attack in our attack chain.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class DamageModifierHandler extends AttackHandler {
    private final double modifier;

    /**
     * Construct a new DamageModifierHandler
     *
     * @param modifier  the multiplicand we want to apply to our damage
     */
    public DamageModifierHandler(double modifier) {
        this.modifier = modifier;
    }

    /**
     * Change the damage of our request by multiplying it with our modifier.
     *
     * @param request   the AttackRequest we may have to handle
     * @return          the result of the chain
     */
    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        System.out.println("Attack damage modified!!");
        request.modifyWeaponDamage(modifier);
        return invokeSuccessor(request);
    }

}
