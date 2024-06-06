package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import mcr.gdx.dungeon.Constants;
import com.badlogic.gdx.graphics.Texture;

public class WallCell {
    private final StaticTiledMapTile tile;
    private final TextureRegion textureRegion;

    public WallCell(Texture tilesetTexture, int x, int y) {
        this.textureRegion = new TextureRegion(tilesetTexture, x, y, Constants.TILE_SIZE, Constants.TILE_SIZE);
        this.tile = new StaticTiledMapTile(textureRegion);
    }

    public StaticTiledMapTile getTile() {
        return tile;
    }
}