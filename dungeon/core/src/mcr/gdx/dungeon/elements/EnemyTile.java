package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.ChainOfResponsibility.damage.DamageRequest;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers.HitChanceHandler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.Game;
import mcr.gdx.dungeon.SpatialHashMap;
import mcr.gdx.dungeon.elements.items.WeaponTile;
import mcr.gdx.dungeon.elements.items.weapons.physical.Fist;

import java.util.LinkedList;

/**
 * The EnemyTile class represents an enemy in the game. It extends the CharacterTile class and adds on
 * top of it the enemy's attack and movement methods. The enemy will move towards the player and attack him if he is in range.
 * If the player isn't in range, the enemy will move around randomly.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class EnemyTile extends CharacterTile {

    private final Handler attackChain;
    private final WeaponTile weapon;

    /**
     * Constructs a new EnemyTile with the specified position, texture, collidable entities, and game.
     * @param position              The position of the enemy
     * @param texture               The texture of the enemy
     * @param collidableEntities    The entities that the enemy can collide with
     * @param game                  The game that the enemy is in
     */
    public EnemyTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities, Game game) {
        super(position, texture, collidableEntities, game);
        weapon = new Fist(new Vector2(0, 0));
        attackChain = new HitChanceHandler();
    }

    /**
     * Makes the enemy attack.
     */
    public void attack() {
        AttackRequest attack = new AttackRequest(null, weapon, -1);
        // If the attack is successful
        if (attackChain.handleRequest(attack)) {
            LinkedList<Vector2> attackedPos = new LinkedList<>();
            attackedPos.add(new Vector2(position).mulAdd(getFacingDirection(), weapon.getRange() * Constants.TILE_SIZE));
            DamageRequest damageRequest = new DamageRequest(weapon.getDamage(), attackedPos, collidableEntities);
            // Request damage to the entities in the attacked position
            requestDamage(damageRequest);
        }
    }

    /**
     * Makes the enemy move.
     * @param spatialHashMap    The spatial hash map for collision detection
     */
    public void move(SpatialHashMap spatialHashMap) {

        // Calculate tile coordinates of the enemy and the player
        int enemyTileX = (int) (position.x / Constants.TILE_SIZE);
        int enemyTileY = (int) (position.y / Constants.TILE_SIZE);
        int playerTileX = (int) (game.getPlayer().position.x / Constants.TILE_SIZE);
        int playerTileY = (int) (game.getPlayer().position.y / Constants.TILE_SIZE);

        if ((enemyTileX == playerTileX || enemyTileY == playerTileY) && position.dst(game.getPlayer().position) <= weapon.getRange() * Constants.TILE_SIZE) {
            // Attack the player
            setFacingDirection(new Vector2(game.getPlayer().position).sub(position));
            attack();
        } else {
            // Move towards the player
            super.move(calculateDirection(), spatialHashMap);
        }
    }

    /**
     * Returns the range of the enemy.
     * @return  The range of the enemy
     */
    private float getRange() {
        return 10 * Constants.TILE_SIZE;
    }

    /**
     * Calculates the direction for the enemy to move in based on the player's position. If the player is in range,
     * the enemy will move towards the player. Otherwise, the enemy will move randomly on the level.
     *
     * @return  The direction for the enemy to move in
     */
    private Vector2 calculateDirection() {
        float distanceToPlayer = position.dst(game.getPlayer().position);

        if (distanceToPlayer <= getRange()) {
            float dx = game.getPlayer().position.x - position.x;
            float dy = game.getPlayer().position.y - position.y;

            if (Math.abs(dx) > Math.abs(dy)) {
                // Move horizontally
                return new Vector2(Math.signum(dx), 0);
            } else {
                // Move vertically
                return new Vector2(0, Math.signum(dy));
            }
        } else {
            // Move randomly
            return getRandomDirection();
        }
    }

    /**
     * Returns a random direction for the enemy to move in.
     * @return  A random direction
     */
    public Vector2 getRandomDirection() {
        int random = (int) (Math.random() * 4);
        switch (random) {
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
