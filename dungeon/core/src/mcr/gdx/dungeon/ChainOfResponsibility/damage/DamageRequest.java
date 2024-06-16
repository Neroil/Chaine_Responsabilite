package mcr.gdx.dungeon.ChainOfResponsibility.damage;

import com.badlogic.gdx.math.Vector2;

import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.elements.CharacterTile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The DamageRequest class represents a request for damage in the game. It implements the Request interface and
 * contains information about the damage, the positions attacked, the collidable entities, and the targets.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class DamageRequest implements Request {
    private int damage;

    private final LinkedList<CharacterTile> targets;
    private final LinkedList<CharacterTile> collidableEntities;
    private final LinkedList<Vector2> positionsAttacked;

    /**
     * Constructs a new DamageRequest with the specified damage, positions attacked, and the list of collidable entities.
     * @param damage                The damage of the request
     * @param positionsAttacked     The positions attacked in the request
     * @param collidableEntities    The list of collidable entities in the game
     */
    public DamageRequest(int damage, LinkedList<Vector2> positionsAttacked, LinkedList<CharacterTile> collidableEntities) {
        this.damage = damage;
        this.positionsAttacked = positionsAttacked;
        this.collidableEntities = collidableEntities;
        this.targets = new LinkedList<>();
    }

    /**
     * Modifies the damage of the request by the specified factor.
     * @param factor  The factor to modify the damage by
     */
    public void modifyDamage(int factor) {
        damage *= factor;
    }

    /**
     * Returns the damage of the request.
     * @return  The damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the list of collidable entities of the request.
     * @return  The list of collidable entities
     */
    public List<CharacterTile> getCollidableEntities() {
        return Collections.unmodifiableList(collidableEntities);
    }

    /**
     * Returns the positions attacked in the request.
     * @return  The positions attacked
     */
    public List<Vector2> getPositionsAttacked() {
        return Collections.unmodifiableList(positionsAttacked);
    }

    /**
     * Adds a target to the request.
     * @param target  The target to add
     */
    public void addTarget(CharacterTile target) {
        targets.add(target);
    }

    /**
     * Returns the targets of the request, this will be populated by the TargetHandler.
     * @return  The targets
     */
    public List<CharacterTile> getTargets() {
        return Collections.unmodifiableList(targets);
    }
}
