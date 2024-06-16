package mcr.gdx.dungeon.elements.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.elements.PlayerTile;

/**
 * The WeaponTile class represents a weapon in the game. It extends the ItemTile class and adds on
 * top of it the weapon's damage, cooldown, last attack time, range, cost, and the attack's type.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public abstract class WeaponTile extends ItemTile {
    private final int damage;
    private final int cooldown;
    private int lastAttack;
    private final int range;
    private final int cost;

    /**
     * Enum for the two possible attack types a weapon can have, will be used in the handlers to know
     * whether to use the player's mana or vigor.
     */
    public enum AttackType {
        PHYSICAL,
        MAGICAL
    }

    /**
     * Constructs a new WeaponTile with the specified position, texture, damage, cooldown, range, and cost.
     * @param position  The position of the weapon
     * @param texture   The texture of the weapon
     * @param damage    The damage of the weapon
     * @param cooldown  The cooldown of the weapon
     * @param range     The range of the weapon
     * @param cost      The cost of the weapon
     */
    public WeaponTile(Vector2 position, TextureRegion texture, int damage, int cooldown, int range, int cost) {
        super(position, texture);
        this.damage = damage;
        this.cooldown = cooldown;
        this.range = range;
        this.cost = cost;
        this.lastAttack = -1;
    }

    /**
     * Makes the player pick up the weapon.
     * @param player  The player to pick up the weapon
     */
    @Override
    public void pickUp(PlayerTile player) {
        player.setWeapon(this);
    }

    /**
     * Returns the damage of the weapon.
     * @return  The damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the cooldown of the weapon.
     * @return  The cooldown
     */
    public int getCooldown() {
        return cooldown;
    }

    /**
     * Sets the last time the weapon was used to attack (in game steps).
     * @param step  The step to set the last attack time to
     */
    public void setLastAttack(int step) {
        lastAttack = step;
    }

    /**
     * Returns the last time the weapon was used to attack (in game steps).
     * @return  The last attack time
     */
    public int getLastAttack() {
        return lastAttack;
    }

    /**
     * Returns the range of the weapon.
     * @return  The range
     */
    public int getRange() {
        return range;
    }

    /**
     * Returns the cost of the weapon.
     * @return  The cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Returns the attack type of the weapon.
     * @return  The attack type
     */
    public abstract AttackType getAttackType();
}
