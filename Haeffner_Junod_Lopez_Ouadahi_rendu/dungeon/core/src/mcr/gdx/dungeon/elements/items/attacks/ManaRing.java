package mcr.gdx.dungeon.elements.items.attacks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.items.AttackItem;
import mcr.gdx.dungeon.elements.items.WeaponTile;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers.CostModifierHandler;

/**
 * Represent the mana ring we can find in our game, it will reduce the cost of magical attacks by half.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class ManaRing extends AttackItem {


    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("magicItems.png"), 48, 48, Constants.TILE_SIZE, Constants.TILE_SIZE);

    /**
     * Constructs a new ManaRing with the specified position.
     *
     * @param position  The position of the mana ring
     */
    public ManaRing(Vector2 position) {
        super(position, TEXTURE);
    }

    /**
     * Method to get the handler for the mana ring.
     * This method returns a new instance of CostModifierHandler that applies only on magical attacks.
     *
     * @return Handler  The handler for the mana ring.
     */
    @Override
    public Handler handler() {
        return new CostModifierHandler(Constants.MANA_RING_MODIFIER, WeaponTile.AttackType.MAGICAL);
    }
}
