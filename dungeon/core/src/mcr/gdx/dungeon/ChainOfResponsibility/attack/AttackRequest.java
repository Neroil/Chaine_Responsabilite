package mcr.gdx.dungeon.ChainOfResponsibility.attack;

import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.elements.PlayerTile;
import mcr.gdx.dungeon.elements.items.WeaponTile;

/**
 * The AttackRequest class represents a request for an attack in the game. It implements the Request interface and
 * contains information about the player making the request, the weapon used for the attack, and the time of the attack.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class AttackRequest implements Request {

    private final PlayerTile player;
    private int weaponCost;
    private int weaponDamage;
    private final int weaponCooldown;
    private final long weaponLastAttack;
    private final WeaponTile.AttackType attackType;
    private final int timeAttack;

    /**
     * Constructs a new AttackRequest with the specified player, weapon, and time of attack.
     * @param player        The player making the request
     * @param weapon        The weapon used for the attack
     * @param timeAttack    The time of the attack (in game step).
     */
    public AttackRequest(PlayerTile player, WeaponTile weapon, int timeAttack) {
        this.player = player;
        this.weaponCost = weapon.getCost();
        this.weaponDamage = weapon.getDamage();
        this.weaponCooldown = weapon.getCooldown();
        this.weaponLastAttack = weapon.getLastAttack();
        this.attackType = weapon.getAttackType();
        this.timeAttack = timeAttack;
    }

    /**
     * Returns the player making the request.
     * @return  The player
     */
    public PlayerTile getPlayer() {
        return player;
    }

    /**
     * Returns the cost of the weapon used for the attack (in vigor or mana).
     * @return  The weapon cost
     */
    public int getWeaponCost() {
        return weaponCost;
    }

    /**
     * Returns the damage of the weapon used for the attack.
     * @return  The weapon damage
     */
    public int getWeaponDamage() {
        return weaponDamage;
    }

    /**
     * Returns the cooldown of the weapon used for the attack.
     * @return  The weapon cooldown
     */
    public int getWeaponCooldown() {
        return weaponCooldown;
    }

    /**
     * Returns the time of the last attack made with the weapon.
     * @return  The time of the last attack
     */
    public long getWeaponLastAttack() {
        return weaponLastAttack;
    }

    /**
     * Modifies the cost of the attack by the specified factor, only for this request.
     * @param factor  The factor to modify the attack's cost by
     */
    public void modifyWeaponCost(double factor) {
        weaponCost = (int) (weaponCost * factor);
    }

    /**
     * Modifies the damage of the attack by the specified factor, only for this request.
     * @param factor  The factor to modify the attack's damage by
     */
    public void modifyWeaponDamage(double factor) {
        weaponDamage = (int) (weaponDamage * factor);
    }

    /**
     * Returns the attack type of the weapon used for the attack, will influence which resource is used during the attack.
     * @return  The weapon's attack type
     */
    public WeaponTile.AttackType getAttackType() {
        return attackType;
    }

    /**
     * Returns the time of the attack (in game step).
     * @return  The time of the attack
     */
    public int getTimeAttack() {
        return timeAttack;
    }
}
