package mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;

/**
 * An abstract handler that check if there's enough of a ressource is available to attack and
 * if it's the case, deduct the cost of the attack form it.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public abstract class RessourceHandler extends AttackHandler {

    /**
     * Check if there's enough ressource to attack
     *
     * @param request   the AttackRequest containing the cost and the player we want to check
     * @return          true if there's enough ressources, false otherwise
     */
    protected abstract boolean checkResources(AttackRequest request);

    /**
     * Update the ressource accordingly to the cost of the attack
     *
     * @param request   the attackRequest from which we get the cost of the attack
     */
    protected abstract void updateResources(AttackRequest request);

    /**
     * Check if there's enough ressource to attack and if there's reduce it by the amount the attack cost.
     *
     * @param request   the AttackRequest we may have to handle
     * @return          false if we don't have enough ressource and the result of the chain otherwise
     */
    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        if (checkResources(request)) {
            updateResources(request);
            return invokeSuccessor(request);
        } else {
            System.out.println("Not enough ressource!");
            return false; // Break the chain
        }
    }
}
