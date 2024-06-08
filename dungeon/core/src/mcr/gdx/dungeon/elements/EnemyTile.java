package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.SpatialHashMap;

import java.util.LinkedList;

public class EnemyTile extends CharacterTile{

    private final CharacterTile player;

    public EnemyTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities, CharacterTile player) {
        super(position, texture, collidableEntities);
        this.player = player;
    }

    public void move(SpatialHashMap spatialHashMap){
        super.move(calculateDirection(), spatialHashMap);
    }

    private float getRange(){
        return 10 * Constants.TILE_SIZE;
    }

    private Vector2 calculateDirection() {
        float distanceToPlayer = position.dst(player.position);

        if (distanceToPlayer <= getRange()) {
            float dx = player.position.x - position.x;
            float dy = player.position.y - position.y;

            if (Math.abs(dx) > Math.abs(dy)) {
                // Move horizontally (left or right)
                return new Vector2(Math.signum(dx), 0);
            } else {
                // Move vertically (up or down)
                return new Vector2(0, Math.signum(dy));
            }
        } else {
            // Move randomly
            return getRandomDirection();
        }
    }

    public Vector2 getRandomDirection(){
        int random = (int) (Math.random() * 4);
        switch (random){
            case 0:
                return new Vector2(0, 1);
            case 1:
                return new Vector2(0, -1);
            case 2:
                return new Vector2(1, 0);
            case 3:
                return new Vector2(-1, 0);
            default:
                return new Vector2(0, 0);
        }
    }
}
