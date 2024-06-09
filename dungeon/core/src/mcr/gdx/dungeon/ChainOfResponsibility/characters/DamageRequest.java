package mcr.gdx.dungeon.ChainOfResponsibility.characters;

import com.badlogic.gdx.math.Vector2;

import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.elements.CharacterTile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DamageRequest implements Request {
    private int damage;

    private final LinkedList<CharacterTile> targets;
    private final LinkedList<CharacterTile> collidableEntities;
    private final LinkedList<Vector2> positionsAttacked;

    public DamageRequest(int damage, LinkedList<Vector2> positionsAttacked, LinkedList<CharacterTile> collidableEntities) {
        this.damage = damage;
        this.positionsAttacked = positionsAttacked;
        this.collidableEntities = collidableEntities;
        this.targets = new LinkedList<>();
    }

    public void modifyDamage(int factor) {
        damage *= factor;
    }

    public int getDamage() {
        return damage;
    }

    public List<CharacterTile> getCollidableEntities() {
        return Collections.unmodifiableList(collidableEntities);
    }

    public List<Vector2> getPositionsAttacked() {
        return Collections.unmodifiableList(positionsAttacked);
    }

    public void addTarget(CharacterTile target) {
        targets.add(target);
    }

    public List<CharacterTile> getTargets() {
        return Collections.unmodifiableList(targets);
    }
}
