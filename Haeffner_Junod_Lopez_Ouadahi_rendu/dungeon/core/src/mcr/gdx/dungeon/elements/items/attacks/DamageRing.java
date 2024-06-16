package mcr.gdx.dungeon.elements.items.attacks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.items.AttackItem;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers.DamageModifierHandler;

/**
 * Represent the damage booster ring we can find in our game, it will double the damage of the attack.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class DamageRing extends AttackItem {

    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("magicItems.png"), 16, 48, Constants.TILE_SIZE, Constants.TILE_SIZE);

    /**
     * Constructs a new DamageRing with the specified position.
     *
     * @param position  The position of the damage ring
     */
    public DamageRing(Vector2 position) {
        super(position, TEXTURE);
    }

    /**
     * Method to get the handler for the damage ring.
     * This method returns a new instance of DamageModifierHandler.
     *
     * @return Handler  The handler for the damage ring.
     */
    @Override
    public Handler handler() {
        return new DamageModifierHandler(2);
    }
}
