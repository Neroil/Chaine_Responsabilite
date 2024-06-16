package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;

/**
 * The ItemTile class represents an item in the game. It extends the SpriteTile class and adds on
 * top of it the item's pick up method and handler. Depending on the type of item, the item pickup's logic will be different.
 *
 * @version 1.0
 * @author Edwin Haeffner
 * @author Esteban Logo
 * @author Junod Arthur
 * @author Yanis Ouadahi
 */
public abstract class ItemTile extends SpriteTile {
    public ItemTile(Vector2 position, TextureRegion texture) {
        super(position, texture);
    }

    public abstract void pickUp(PlayerTile player);

    public abstract Handler handler();
}
