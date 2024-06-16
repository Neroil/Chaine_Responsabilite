package mcr.gdx.dungeon.elements.items.weapons.magical;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.items.weapons.MagicalWeapon;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers.ManaHandler;


/**
 * The MagicScepter class represents a magic scepter weapon in the game. It extends the MagicalWeapon class and provides a texture for the magic scepter.
 * The magic scepter has a damage of 3, a cooldown of 1, a range of 10, and a cost of 30.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class MagicScepter extends MagicalWeapon {

    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 96, 112, Constants.TILE_SIZE, Constants.TILE_SIZE);

    /**
     * Constructs a new MagicScepter with the specified position.
     * @param position  The position of the magic scepter
     */
    public MagicScepter(Vector2 position) {
        super(position, TEXTURE, 3, 1, 10, 30);
    }

    /**
     * Method to get the handler for the magic scepter.
     * This method returns a new instance of ManaHandler.
     *
     * @return Handler  The handler for the magic scepter.
     */
    @Override
    public Handler handler() {
        return new ManaHandler();
    }

}
