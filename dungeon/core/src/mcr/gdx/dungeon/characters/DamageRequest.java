package mcr.gdx.dungeon.characters;

import com.badlogic.gdx.math.Vector2;

import mcr.gdx.dungeon.elements.CharacterTile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DamageRequest {
    private int damage;

    public DamageRequest(int damage) {
        this.damage = damage;
    }

    public void modifyDamage(int factor) {
        damage *= factor;
    }
}
