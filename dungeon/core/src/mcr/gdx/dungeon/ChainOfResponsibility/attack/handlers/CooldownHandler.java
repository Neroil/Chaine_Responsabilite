package mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;

/**
 * The handler that check the cooldown of the weapon we use to attack.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class CooldownHandler extends AttackHandler {

    /**
     * Check if we can attack based on the in game time (steps) since the last attack and the cooldown of the weapon.
     *
     * @param request   we get the cooldown and the last attack from this AttackRequest
     * @return          false and break the chain if the weapon is still on cooldown or the further result of the chain
     */
    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        long weaponLastAttack = request.getWeaponLastAttack();
        int cooldown = request.getWeaponCooldown();

        if (weaponLastAttack == -1 || (request.getTimeAttack() - weaponLastAttack) > cooldown)
            return invokeSuccessor(request);
        else {
            System.out.println("Weapon is on cooldown!");
            return false;
        }
    }

}
