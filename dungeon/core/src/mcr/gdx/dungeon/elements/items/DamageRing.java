package mcr.gdx.dungeon.elements.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.weapons.handlers.DamageModifierHandler;

public class DamageRing extends ItemTile {

    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("magicItems.png"), 16, 48, Constants.TILE_SIZE, Constants.TILE_SIZE);

    public DamageRing(Vector2 position) {
        super(position, TEXTURE);
    }

    @Override
    public GenericHandler handler() {
        return new DamageModifierHandler(2);
    }
}