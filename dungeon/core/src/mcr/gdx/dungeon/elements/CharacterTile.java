package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class CharacterTile {
    public Vector2 position;
    private final Sprite sprite;

    public CharacterTile(Vector2 position, TextureRegion texture) {
        this.position = position;
        this.sprite = new Sprite(texture);
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    public Rectangle getBoundingBox(){
        return sprite.getBoundingRectangle();
    }
}