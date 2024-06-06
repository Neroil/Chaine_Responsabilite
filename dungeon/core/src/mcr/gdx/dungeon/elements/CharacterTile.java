package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.SpatialHashMap;

import java.util.LinkedList;
import java.util.Set;


public class CharacterTile extends SpriteTile{
    private int healthPoint;
    private LinkedList<CharacterTile> collidableEntities = new LinkedList<CharacterTile>();
    public CharacterTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities){
        this(position, texture, collidableEntities,10); //By default, the character has 10 hp
    }
    public CharacterTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities, int initialLife) {
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

    public void move(Vector2 direction, SpatialHashMap spatialHashMap){
        Vector2 newPosition = new Vector2(position.x + direction.x * Constants.TILE_SIZE, position.y + direction.y * Constants.TILE_SIZE);
        if (!isCollision(newPosition, spatialHashMap)) {
            position.set(newPosition);
        }
    }

    private boolean isCollision(Vector2 newPosition, SpatialHashMap spatialHashMap) {
        Rectangle newBoundingBox = new Rectangle(newPosition.x, newPosition.y, getBoundingBox().width, getBoundingBox().height);
        Set<Rectangle> potentialColliders = spatialHashMap.getPotentialColliders(newBoundingBox);
        for (Rectangle wallTile : potentialColliders) {
            if (newBoundingBox.overlaps(wallTile)) {
                return true;
            }
        }
        for(CharacterTile entity : collidableEntities){
            if(newBoundingBox.overlaps(entity.getBoundingBox())){
                return true;
            }
        }
        return false;
    }

}