package mcr.gdx.dungeon.elements.items.weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.elements.items.WeaponTile;

/**
 * Abstract class representing a physical weapon in the game.
 * This class extends the WeaponTile class and provides a method for getting the attack type, which is physical.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public abstract class PhysicalWeapon extends WeaponTile {

    /**
     * Constructor for the PhysicalWeapon class.
     *
     * @param position  The position of the weapon on the game map.
     * @param texture   The texture used to represent the weapon on the game map.
     * @param damage    The damage the weapon can inflict.
     * @param cooldown  The cooldown period for the weapon after it is used.
     * @param range     The range of the weapon.
     * @param cost      The cost of the weapon.
     */
    public PhysicalWeapon(Vector2 position, TextureRegion texture, int damage, int cooldown, int range, int cost) {
        super(position, texture, damage, cooldown, range, cost);
    }

    /**
     * Method to get the attack type of the weapon.
     * This method returns the attack type as PHYSICAL.
     *
     * @return AttackType.PHYSICAL  The attack type of the weapon.
     */
    @Override
    public AttackType getAttackType() {
        return AttackType.PHYSICAL;
    }
}
