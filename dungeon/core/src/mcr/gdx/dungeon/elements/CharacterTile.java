package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.DamageNumber;
import mcr.gdx.dungeon.Game;
import mcr.gdx.dungeon.SpatialHashMap;
import mcr.gdx.dungeon.ChainOfResponsibility.characters.handlers.HitHandler;
import mcr.gdx.dungeon.ChainOfResponsibility.characters.handlers.TargetHandler;

import java.util.LinkedList;
import java.util.Set;


public class CharacterTile extends SpriteTile {

    protected int healthPoint;
    protected LinkedList<CharacterTile> collidableEntities = new LinkedList<CharacterTile>();
    private Vector2 facingDirection = Direction.DOWN.getDirection();
    private final GenericHandler requestDamageChain;
    protected final Game game;

    public enum Direction {
        UP(new Vector2(0, 1)),
        DOWN(new Vector2(0, -1)),
        LEFT(new Vector2(-1, 0)),
        RIGHT(new Vector2(1, 0));

        private final Vector2 direction;

        Direction(Vector2 direction) {
            this.direction = direction;
        }

        public Vector2 getDirection() {
            return direction;
        }
    }

    public Vector2 getFacingDirection() {
        return facingDirection;
    }

    public CharacterTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities, Game game) {
        this(position, texture, collidableEntities, game, 10); //By default, the character has 10 hp
    }

    public CharacterTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities, Game game, int initialLife) {
        super(position, texture);
        this.healthPoint = initialLife;
        this.collidableEntities = collidableEntities;
        this.game = game;
        requestDamageChain = new TargetHandler();
        requestDamageChain.setSuccessor(new HitHandler());
    }

    public boolean isAlive() {
        return healthPoint > 0;
    }

    public void reduceLife(int amount) {
        healthPoint -= amount;
        if (healthPoint < 0) {
            healthPoint = 0;
        }

        // Create and add a new damage number to the Game class
        game.addDamageNumber(new DamageNumber(amount, position));
    }

    public int getHP() {
        return healthPoint;
    }

    public void move(Vector2 direction, SpatialHashMap spatialHashMap) {
        Vector2 newPosition = new Vector2(position.x + direction.x * Constants.TILE_SIZE, position.y + direction.y * Constants.TILE_SIZE);
        if (!isCollision(newPosition, spatialHashMap)) {
            //spatialHashMap.remove(getBoundingBox());
            position.set(newPosition);
            // Updating this entity's position in the spatial hash map
            //spatialHashMap.insert(getBoundingBox());
        }
        setFacingDirection(direction);

    }

    protected void setFacingDirection(Vector2 direction) {
        // Update facing direction
        if (direction.x > 0) {
            facingDirection = Direction.RIGHT.getDirection();
        } else if (direction.x < 0) {
            facingDirection = Direction.LEFT.getDirection();
        } else if (direction.y > 0) {
            facingDirection = Direction.UP.getDirection();
        } else {
            facingDirection = Direction.DOWN.getDirection();
        }
    }

//    public void update(float delta) {
//
//        for (int i = damageNumbers.size() - 1; i >= 0; i--) {
//            DamageNumber number = damageNumbers.get(i);
//            number.update(delta);
//            if (number.isExpired()) {
//                damageNumbers.remove(i);
//            }
//        }
//    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    private boolean isCollision(Vector2 newPosition, SpatialHashMap spatialHashMap) {
        Rectangle newBoundingBox = new Rectangle(newPosition.x, newPosition.y, getBoundingBox().width, getBoundingBox().height);
        Set<Rectangle> potentialColliders = spatialHashMap.getPotentialColliders(newBoundingBox);

        //Check for collision with the dungeon's walls
        for (Rectangle wallTile : potentialColliders) {
            if (newBoundingBox.overlaps(wallTile)) {
                return true;
            }
        }

        //Check for collision with other's position
        for (CharacterTile entity : collidableEntities) {
            if (entity != this && newPosition.equals(entity.position)) {
                return true;
            }
        }
        return false;
    }

    public Vector2 getPosition() {
        return position;
    }

    protected void requestDamage(Request request) {
        requestDamageChain.handleRequest(request);
    }
}