package mcr.gdx.dungeon.elements.items.weapons.physical;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.items.weapons.PhysicalWeapon;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers.VigorHandler;

/**
 * The Club class represents a club weapon in the game. It extends the PhysicalWeapon class and provides a texture for the club.
 * The club has a damage of 5, a cooldown of 1, a range of 1, and a cost of 20.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class Club extends PhysicalWeapon {

    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 80, 128, Constants.TILE_SIZE, Constants.TILE_SIZE);

    /**
     * Constructs a new Club with the specified position.
     * @param position  The position of the club
     */
    public Club(Vector2 position) {
        super(position, TEXTURE, 5, 1, 1, 20);
    }

    /**
     * Method to get the handler for the club.
     * This method returns a new instance of VigorHandler.
     *
     * @return Handler  The handler for the club.
     */
    @Override
    public Handler handler() {
        return new VigorHandler();
    }
}
