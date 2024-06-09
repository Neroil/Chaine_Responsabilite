package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;

public abstract class ItemTile extends SpriteTile {
    public ItemTile(Vector2 position, TextureRegion texture) {
        super(position, texture);
    }

    public abstract void pickUp(PlayerTile player);

    public abstract GenericHandler handler();
}
