package mcr.gdx.dungeon.elements.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.elements.PlayerTile;

/**
 * Abstract class representing an attack item in the game.
 * This class extends the ItemTile class and provides a method for picking up the item and adding it to the player's attack items.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public abstract class AttackItem extends ItemTile {

    /**
     * Constructor for the AttackItem class.
     *
     * @param position  The position of the item on the game map.
     * @param texture   The texture used to represent the item on the game map.
     */
    public AttackItem(Vector2 position, TextureRegion texture) {
        super(position, texture);
    }

    /**
     * Method to handle the player picking up the item.
     * This method adds the item to the player's attack items.
     *
     * @param player  The player picking up the item.
     */
    @Override
    public void pickUp(PlayerTile player) {
        player.addAttackItem(this);
    }
}
