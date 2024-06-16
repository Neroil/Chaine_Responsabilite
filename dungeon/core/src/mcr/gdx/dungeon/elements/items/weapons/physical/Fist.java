package mcr.gdx.dungeon.elements.items.weapons.physical;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.items.weapons.PhysicalWeapon;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers.VigorHandler;

/**
 * The Fist class represents a fist weapon in the game. It extends the PhysicalWeapon class and provides a texture for the fist.
 * The fist has a damage of 1, a cooldown of 0, a range of 1, and a cost of 5.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class Fist extends PhysicalWeapon {

    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 128, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);

    /**
     * Constructs a new Fist with the specified position.
     * @param position  The position of the fist
     */
    public Fist(Vector2 position) {
        super(position, TEXTURE, 1, 0, 1, 5);
    }

    /**
     * Method to get the handler for the fist.
     * This method returns a new instance of VigorHandler.
     *
     * @return Handler  The handler for the fist.
     */
    @Override
    public Handler handler() {
        return new VigorHandler();
    }
}
