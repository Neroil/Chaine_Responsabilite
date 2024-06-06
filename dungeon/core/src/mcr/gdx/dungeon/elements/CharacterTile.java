package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public class CharacterTile extends SpriteTile{
    private int healthPoint;

    public CharacterTile(Vector2 position, TextureRegion texture){
        this(position, texture, 10); //By default, the character has 10 hp
    }
    public CharacterTile(Vector2 position, TextureRegion texture, int initialLife) {
        super(position, texture);
        this.healthPoint = initialLife;
    }

    public boolean isAlive() {
        return healthPoint > 0;
    }

    public void reduceLife(int amount) {
        healthPoint -= amount;
        if (healthPoint < 0) {
            healthPoint = 0;
        }
    }

    public int getHP() {
        return healthPoint;
    }

}