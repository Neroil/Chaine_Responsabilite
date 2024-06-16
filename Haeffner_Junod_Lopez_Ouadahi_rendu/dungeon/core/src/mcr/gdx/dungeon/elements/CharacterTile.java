package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.DamageNumber;
import mcr.gdx.dungeon.Game;
import mcr.gdx.dungeon.SpatialHashMap;
import mcr.gdx.dungeon.ChainOfResponsibility.damage.handlers.HitHandler;
import mcr.gdx.dungeon.ChainOfResponsibility.damage.handlers.TargetHandler;

import java.util.LinkedList;
import java.util.Set;

/**
 * The CharacterTile class represents a character in the game. It extends the SpriteTile class and adds on
 * top of it the character's health points, facing direction, and methods for moving and reducing life.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class CharacterTile extends SpriteTile {

    protected int healthPoint;
    protected LinkedList<CharacterTile> collidableEntities = new LinkedList<CharacterTile>();
    private Vector2 facingDirection = Direction.DOWN.getDirection();
    private final Handler requestDamageChain;
    protected final Game game;

    /**
     * Enum for the four possible directions a character can face.
     */
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

    /**
     * Returns the direction the character is facing.
     * @return  The facing direction
     */
    public Vector2 getFacingDirection() {
        return facingDirection;
    }

    /**
     * Constructs a new CharacterTile with the specified position, texture, collidable entities, and game, with 10 health points.
     * @param position              The position of the character
     * @param texture               The texture of the character
     * @param collidableEntities    The entities that the character can collide with
     * @param game                  The game that the character is in
     */
    public CharacterTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities, Game game) {
        this(position, texture, collidableEntities, game, 10); //By default, a character has 10 hp
    }

    /**
     * Constructs a new CharacterTile with the specified position, texture, collidable entities, game, and initial life.
     * @param position              The position of the character
     * @param texture               The texture of the character
     * @param collidableEntities    The entities that the character can collide with
     * @param game                  The game that the character is in
     * @param initialLife           The initial life of the character
     */
    public CharacterTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities, Game game, int initialLife) {
        super(position, texture);
        this.healthPoint = initialLife;
        this.collidableEntities = collidableEntities;
        this.game = game;
        requestDamageChain = new TargetHandler();
        requestDamageChain.setSuccessor(new HitHandler());
    }

    /**
     * Returns whether the character is alive.
     * @return  True if the character is alive, false otherwise
     */
    public boolean isAlive() {
        return healthPoint > 0;
    }

    /**
     * Reduces the character's life by the specified amount, will also add the damage number to the game to display them.
     * @param amount  The amount to reduce the life by
     */
    public void reduceLife(int amount) {
        healthPoint -= amount;
        if (healthPoint < 0) {
            healthPoint = 0;
        }

        // Create and add a new damage number to the Game class
        game.addDamageNumber(new DamageNumber(amount, position));
    }

    /**
     * Returns the character's health points.
     * @return  The health points
     */
    public int getHP() {
        return healthPoint;
    }

    /**
     * Moves the character in the specified direction and sets their facing direction.
     * @param direction         The direction to move in
     * @param spatialHashMap    The spatial hash map of the game
     */
    public void move(Vector2 direction, SpatialHashMap spatialHashMap) {
        Vector2 newPosition = new Vector2(position.x + direction.x * Constants.TILE_SIZE, position.y + direction.y * Constants.TILE_SIZE);
        if (!isCollision(newPosition, spatialHashMap)) {
            position.set(newPosition);
        }
        setFacingDirection(direction);
    }

    /**
     * Sets the direction the character is facing.
     * @param direction  The direction to face
     */
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

    /**
     * Draws the character.
     * @param batch  The sprite batch to draw the character with
     */
    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    /**
     * Returns whether there is a collision at the specified position.
     * @param newPosition       The position to check for a collision
     * @param spatialHashMap    The spatial hash map of the game
     * @return  True if there is a collision, false otherwise
     */
    private boolean isCollision(Vector2 newPosition, SpatialHashMap spatialHashMap) {
        Rectangle newBoundingBox = new Rectangle(newPosition.x, newPosition.y, getBoundingBox().width, getBoundingBox().height);
        Set<Rectangle> potentialColliders = spatialHashMap.getPotentialColliders(newBoundingBox);

        //Check for collision with the dungeon's walls
        for (Rectangle wallTile : potentialColliders) {
            if (newBoundingBox.overlaps(wallTile)) {
                return true;
            }
        }

        //Check for collision with other entities' position
        for (CharacterTile entity : collidableEntities) {
            if (entity != this && newPosition.equals(entity.position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the position of the character.
     * @return  The position
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Requests damage to be dealt to the character.
     * @param request  The damage request
     */
    protected void requestDamage(Request request) {
        requestDamageChain.handleRequest(request);
    }
}