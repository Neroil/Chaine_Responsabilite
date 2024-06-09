package mcr.gdx.dungeon.elements.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.WeaponTile;
import mcr.gdx.dungeon.weapons.handlers.MagicalRessourceHandler;

public class MagicScepter extends WeaponTile {

    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 96, 112, Constants.TILE_SIZE, Constants.TILE_SIZE);

    public MagicScepter(Vector2 position) {
        super(position, TEXTURE, "Magical Sceptre", 1, 400, 10, 30);
    }

    @Override
    public GenericHandler handler() { return new MagicalRessourceHandler(); }

}
