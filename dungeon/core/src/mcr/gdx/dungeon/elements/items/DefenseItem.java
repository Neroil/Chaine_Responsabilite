package mcr.gdx.dungeon.elements.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.elements.PlayerTile;

public abstract class DefenseItem extends ItemTile {
    public DefenseItem(Vector2 position, TextureRegion texture) {
        super(position, texture);
    }

    @Override
    public void pickUp(PlayerTile player) {
        player.addDefenseItem(this);
    }
}
