package mcr.gdx.dungeon.elements.items.weapons.physical;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.items.weapons.PhysicalWeapon;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers.VigorHandler;

/**
 * The Sword class represents a sword weapon in the game. It extends the PhysicalWeapon class and provides a texture for the sword.
 * The sword has a damage of 3, a cooldown of 0, a range of 3, and a cost of 10.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class Sword extends PhysicalWeapon {

    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 160, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);

    /**
     * Constructs a new Sword with the specified position.
     * @param position  The position of the sword
     */
    public Sword(Vector2 position) {
        super(position, TEXTURE, 3, 0, 3, 10);
    }

    /**
     * Method to get the handler for the sword.
     * This method returns a new instance of VigorHandler.
     *
     * @return Handler  The handler for the sword.
     */
    @Override
    public Handler handler() {
        return new VigorHandler();
    }
}
