package mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;

import java.util.Random;

/**
 * This handler makes an attack fail 20% of the time purely randomly.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class HitChanceHandler extends AttackHandler {
    private static final int HIT_CHANCE = 80;           // Chances of an attack to succeed
    private static final Random random = new Random();

    /**
     * Randomly check if the attack will succeed by randomly picking a number between 0 and 100,
     * if it's under or equal to 80 the attack succeed.
     *
     * @param request   the AttackRequest we may have to handle
     * @return          false if the attack failed, the further result of the chain otherwise
     */
    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        int chance = random.nextInt(0, 101);

        // 80% chances to hit the enemy
        if (chance <= HIT_CHANCE) {
            return invokeSuccessor(request);
        } else {
            System.out.println("The attack missed!");
            return false;
        }
    }
}
