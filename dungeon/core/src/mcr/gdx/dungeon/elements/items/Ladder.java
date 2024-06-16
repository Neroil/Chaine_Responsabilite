package mcr.gdx.dungeon.elements.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.elements.PlayerTile;

/**
 * The Ladder class represents a ladder in the game. It extends the ItemTile class and overrides the pickUp method
 * to make the player exit the level when the ladder is picked up. The handler method returns null as the ladder
 * does not have any specific handlers.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class Ladder extends ItemTile {

    /**
     * Constructs a new Ladder with the specified position and texture.
     * @param position  The position of the ladder
     * @param texture   The texture of the ladder
     */
    public Ladder(Vector2 position, TextureRegion texture) {
        super(position, texture);
    }

    /**
     * Makes the player exit the level if the ladder is picked up.
     * @param player The player of the game
     */
    @Override
    public void pickUp(PlayerTile player) {
        player.exitLevel();
    }

    /**
     * Returns null as the ladder does not have any specific handlers.
     * @return null
     */
    @Override
    public Handler handler() {
        return null;
    }
}
