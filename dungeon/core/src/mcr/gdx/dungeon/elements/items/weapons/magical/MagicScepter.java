package mcr.gdx.dungeon.elements.items.weapons.magical;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.items.weapons.MagicalWeapon;
import mcr.gdx.dungeon.ChainOfResponsibility.weapons.handlers.MagicalRessourceHandler;

public class MagicScepter extends MagicalWeapon {

    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 96, 112, Constants.TILE_SIZE, Constants.TILE_SIZE);

    public MagicScepter(Vector2 position) {
        super(position, TEXTURE, 3, 1, 10, 30);
    }

    @Override
    public GenericHandler handler() {
        return new MagicalRessourceHandler();
    }

}
