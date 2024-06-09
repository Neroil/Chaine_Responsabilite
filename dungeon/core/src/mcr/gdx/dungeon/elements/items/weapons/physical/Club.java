package mcr.gdx.dungeon.elements.items.weapons.physical;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.items.WeaponTile;
import mcr.gdx.dungeon.elements.items.weapons.PhysicalWeapon;
import mcr.gdx.dungeon.weapons.handlers.PhysicalRessourceHandler;

public class Club extends PhysicalWeapon {

    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 80, 128, Constants.TILE_SIZE, Constants.TILE_SIZE);

    public Club(Vector2 position) {
        super(position, TEXTURE, 5, 1, 1, 20);
    }

    @Override
    public GenericHandler handler() { return new PhysicalRessourceHandler(); }
}
