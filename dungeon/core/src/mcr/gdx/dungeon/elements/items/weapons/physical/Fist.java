package mcr.gdx.dungeon.elements.items.weapons.physical;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.items.weapons.PhysicalWeapon;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers.VigorHandler;

public class Fist extends PhysicalWeapon {

    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 128, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);

    public Fist(Vector2 position) {
        super(position, TEXTURE, 1, 0, 1, 5);
    }

    @Override
    public Handler handler() {
        return new VigorHandler();
    }
}
