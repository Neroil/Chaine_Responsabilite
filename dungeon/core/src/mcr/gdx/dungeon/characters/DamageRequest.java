package mcr.gdx.dungeon.characters;

import com.badlogic.gdx.math.Vector2;

import mcr.gdx.dungeon.elements.CharacterTile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DamageRequest {
    private int damage;
    private final LinkedList<Vector2> attackedPositions;
    private final LinkedList<CharacterTile> targets;

    public DamageRequest(int damage, LinkedList<Vector2> attackedPositions) {
        this.damage = damage;
        this.attackedPositions = attackedPositions;
        targets = new LinkedList<CharacterTile>();
    }

    public void addTarget(CharacterTile target){
        targets.add(target);
    }

    public List<CharacterTile> getTargets(){
        return Collections.unmodifiableList(targets);
    }

    public List<Vector2> getAttackedPositions(){
        return Collections.unmodifiableList(attackedPositions);
    }

    public void modifyDamage(int factor) {
        damage *= factor;
    }
}
