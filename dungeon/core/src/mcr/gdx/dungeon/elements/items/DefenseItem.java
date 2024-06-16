package mcr.gdx.dungeon.elements.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.elements.PlayerTile;

/**
 * The DefenseItem class represents a defense item in the game. It extends the ItemTile class and overrides the pickUp method
 * to add the defense item to the player's defense items when it is picked up. This class is abstract and serves as a base for
 * all specific defense items in the game.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public abstract class DefenseItem extends ItemTile {

    /**
     * Constructs a new DefenseItem with the specified position and texture.
     * @param position  The position of the defense item
     * @param texture   The texture of the defense item
     */
    public DefenseItem(Vector2 position, TextureRegion texture) {
        super(position, texture);
    }

    /**
     * Makes the player pick up the defense item and add it to their defense items list.
     * @param player  The player to pick up the defense item
     */
    @Override
    public void pickUp(PlayerTile player) {
        player.addDefenseItem(this);
    }
}
