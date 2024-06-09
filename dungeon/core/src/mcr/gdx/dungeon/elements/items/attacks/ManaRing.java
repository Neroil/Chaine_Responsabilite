package mcr.gdx.dungeon.elements.items.attacks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.Assets;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.elements.items.AttackItem;
import mcr.gdx.dungeon.elements.items.WeaponTile;
import mcr.gdx.dungeon.ChainOfResponsibility.weapons.handlers.CostModifierHandler;

public class ManaRing extends AttackItem {


    private final static TextureRegion TEXTURE = new TextureRegion(Assets.get("magicItems.png"), 48, 48, Constants.TILE_SIZE, Constants.TILE_SIZE);

    public ManaRing(Vector2 position) {
        super(position, TEXTURE);
    }

    @Override
    public GenericHandler handler() {
        return new CostModifierHandler(Constants.MANA_RING_MODIFIER, WeaponTile.AttackType.MAGICAL);
    }
}
