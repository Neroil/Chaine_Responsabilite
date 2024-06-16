package mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;
import mcr.gdx.dungeon.elements.PlayerTile;

/**
 * Derived class of the RessourceHandler that check if the player has enough mana and may reduce it.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class ManaHandler extends RessourceHandler {

    /**
     * Check if we have enough mana to attack.
     *
     * @param request   the AttackRequest containing the cost and the player we want to check
     * @return          true if we have enough mana, false otherwise
     */
    @Override
    protected boolean checkResources(AttackRequest request) {
        int mana = request.getPlayer().getMana();
        int cost = request.getWeaponCost();

        return mana >= cost;
    }

    /**
     * Reduce the mana by the amount of the cost of the attack
     *
     * @param request   the attackRequest from which we get the cost of the attack
     */
    @Override
    protected void updateResources(AttackRequest request) {
        PlayerTile player = request.getPlayer();
        int cost = request.getWeaponCost();

        player.reduceMana(cost);
    }

}
