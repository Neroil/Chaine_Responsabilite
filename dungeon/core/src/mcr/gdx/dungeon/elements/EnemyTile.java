package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.ChainOfResponsibility.characters.DamageRequest;
import mcr.gdx.dungeon.ChainOfResponsibility.weapons.AttackRequest;
import mcr.gdx.dungeon.ChainOfResponsibility.weapons.handlers.HitChanceHandler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.Game;
import mcr.gdx.dungeon.SpatialHashMap;
import mcr.gdx.dungeon.elements.items.WeaponTile;
import mcr.gdx.dungeon.elements.items.weapons.physical.Fist;

import java.util.LinkedList;

public class EnemyTile extends CharacterTile{

    private final GenericHandler attackChain;
    private final WeaponTile weapon;

    public EnemyTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities, Game game) {
        super(position, texture, collidableEntities, game);
        weapon = new Fist(new Vector2(0, 0));
        attackChain = new HitChanceHandler();
    }

    public void attack(){
        AttackRequest attack = new AttackRequest(null, weapon, -1);
        if(attackChain.handleRequest(attack)){
            LinkedList<Vector2> attackedPos = new LinkedList<>();
            attackedPos.add(new Vector2(position).mulAdd(getFacingDirection(), weapon.getRange() * Constants.TILE_SIZE));
            DamageRequest damageRequest = new DamageRequest(weapon.getDamage(), attackedPos, collidableEntities);
            requestDamage(damageRequest);
        }
    }

    public void move(SpatialHashMap spatialHashMap){
        float distanceToPlayer = position.dst(game.getPlayer().position);

        // Calculate tile coordinates of the enemy and the player
        int enemyTileX = (int)(position.x / Constants.TILE_SIZE);
        int enemyTileY = (int)(position.y / Constants.TILE_SIZE);
        int playerTileX = (int)(game.getPlayer().position.x / Constants.TILE_SIZE);
        int playerTileY = (int)(game.getPlayer().position.y / Constants.TILE_SIZE);


        if ((enemyTileX == playerTileX || enemyTileY == playerTileY) && position.dst(game.getPlayer().position) <= weapon.getRange() * Constants.TILE_SIZE) {
            // Attack the player
            setFacingDirection(new Vector2(game.getPlayer().position).sub(position));
            attack();
        } else {
            // Move towards the player
            super.move(calculateDirection(), spatialHashMap);
        }
    }

    private float getRange(){
        return 10 * Constants.TILE_SIZE;
    }



    private Vector2 calculateDirection() {
        float distanceToPlayer = position.dst(game.getPlayer().position);

        if (distanceToPlayer <= getRange()) {
            float dx = game.getPlayer().position.x - position.x;
            float dy = game.getPlayer().position.y - position.y;

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
