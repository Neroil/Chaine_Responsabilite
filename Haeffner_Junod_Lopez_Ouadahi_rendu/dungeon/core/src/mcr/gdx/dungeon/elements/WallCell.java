package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import mcr.gdx.dungeon.Constants;
import com.badlogic.gdx.graphics.Texture;

/**
 * The WallCell class represents a cell in the dungeon that contains a wall.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class WallCell {
    private final StaticTiledMapTile tile;
    private final TextureRegion textureRegion;

    /**
     * Constructs a new WallCell with the specified texture and position.
     *
     * @param tilesetTexture the texture to use for the wall cell
     * @param x the x position of the wall cell in the texture
     * @param y the y position of the wall cell in the texture
     */
    public WallCell(Texture tilesetTexture, int x, int y) {
        this.textureRegion = new TextureRegion(tilesetTexture, x, y, Constants.TILE_SIZE, Constants.TILE_SIZE);
        this.tile = new StaticTiledMapTile(textureRegion);
    }

    /**
     * Returns the tile of the WallCell.
     *
     * @return the tile of the WallCell
     */
    public StaticTiledMapTile getTile() {
        return tile;
    }
}