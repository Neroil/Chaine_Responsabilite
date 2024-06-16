package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Constants;

/**
 * The SpriteTile class represents a tile in the dungeon that has a sprite, this class is to be used with every entities
 * in the game.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public abstract class SpriteTile {
    public Vector2 position;
    private final Sprite sprite;
    TextureRegion texture;

    /**
     * Constructs a new SpriteTile with the specified position and texture.
     *
     * @param position the position of the sprite tile
     * @param texture the texture of the sprite tile
     */
    public SpriteTile(Vector2 position, TextureRegion texture) {
        this.position = position;
        this.sprite = new Sprite(texture);
        this.texture = texture;
    }

    /**
     * Updates the state of the SpriteTile. To use in the render loop as it's called
     * every frame.
     *
     * @param batch the SpriteBatch to draw on
     */
    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    /**
     * Returns the bounding box (the hitbox) of the SpriteTile.
     *
     * @return the bounding box of the SpriteTile
     */
    public Rectangle getBoundingBox() {
        return sprite.getBoundingRectangle();
    }

    /**
     * Snaps the SpriteTile to the center of the nearest tile, to use to make sure we're not in between tiles.
     */
    public void snapToTileCenter() {
        float snappedX = (float) Math.floor(position.x / Constants.TILE_SIZE) * Constants.TILE_SIZE;
        float snappedY = (float) Math.floor(position.y / Constants.TILE_SIZE) * Constants.TILE_SIZE;
        position.set(snappedX, snappedY);
    }

    /**
     * Get the texture of the SpriteTile.
     * @return the texture of the SpriteTile
     */
    public TextureRegion getTexture() {
        return texture;
    }
}
