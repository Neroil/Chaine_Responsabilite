package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Constants;


public class CharacterTile extends SpriteTile{

    public CharacterTile(Vector2 position, TextureRegion texture) {
        super(position, texture);
    }

}