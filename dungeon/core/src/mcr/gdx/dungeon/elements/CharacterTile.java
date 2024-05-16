package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CharacterTile {
    private Vector2 position;
    private Texture texture;

    public CharacterTile(Vector2 position, Texture texture) {
        this.position = position;
        this.texture = texture;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
}