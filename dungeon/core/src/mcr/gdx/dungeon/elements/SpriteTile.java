package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Constants;

public abstract class SpriteTile {
    public Vector2 position;
    private final Sprite sprite;
    TextureRegion texture;

    public SpriteTile(Vector2 position, TextureRegion texture) {
        this.position = position;
        this.sprite = new Sprite(texture);
        this.texture = texture;
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    public Rectangle getBoundingBox() {
        return sprite.getBoundingRectangle();
    }

    public void snapToTileCenter() {
        float snappedX = (float) Math.floor(position.x / Constants.TILE_SIZE) * Constants.TILE_SIZE;
        float snappedY = (float) Math.floor(position.y / Constants.TILE_SIZE) * Constants.TILE_SIZE;
        position.set(snappedX, snappedY);
    }

    public TextureRegion getTexture() {
        return texture;
    }
}
