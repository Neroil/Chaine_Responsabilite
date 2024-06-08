package mcr.gdx.dungeon.elements.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.WeaponTile;
import mcr.gdx.dungeon.weapons.handlers.PhysicalRessourceHandler;

public class Sword extends WeaponTile {

    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 160, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);

    public Sword(Vector2 position) { super(position, TEXTURE, "Sword", 2, 200, 3, 10); }

    @Override
    public GenericHandler handler() {
        return new PhysicalRessourceHandler();
    }
}
