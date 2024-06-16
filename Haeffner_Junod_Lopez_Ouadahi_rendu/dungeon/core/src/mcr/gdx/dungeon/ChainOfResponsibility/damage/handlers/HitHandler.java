package mcr.gdx.dungeon.ChainOfResponsibility.damage.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.damage.DamageRequest;
import mcr.gdx.dungeon.elements.CharacterTile;

/**
 * Allow us to apply the damage of the attack to all the targets of our request by calling their corresponding function.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class HitHandler extends DamageHandler {

    /**
     * Call the damage function of all the targets with the damage of our attack
     *
     * @param request   the DamageRequest we may have to handle
     * @return          the result of the chain
     */
    @Override
    protected boolean handleDamageRequest(DamageRequest request) {
        System.out.println("Trying to hit");
        for(CharacterTile target : request.getTargets()){
            System.out.println("Target hit!!");
            target.reduceLife(request.getDamage());
        }
        return invokeSuccessor(request);
    }
}
