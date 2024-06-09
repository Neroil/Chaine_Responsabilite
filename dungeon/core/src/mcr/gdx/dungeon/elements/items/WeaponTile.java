package mcr.gdx.dungeon.elements.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.elements.PlayerTile;

import java.sql.Timestamp;

public abstract class WeaponTile extends ItemTile {
    private final int damage;
    private final int cooldown;
    private int lastAttack;
    private final int range;
    private final int cost;

    public enum AttackType {
        PHYSICAL,
        MAGICAL
    }

    public WeaponTile(Vector2 position, TextureRegion texture, int damage, int cooldown, int range, int cost){
        super(position, texture);
        this.damage = damage;
        this.cooldown = cooldown;
        this.range = range;
        this.cost = cost;
        this.lastAttack = -1;
    }

    @Override
    public void pickUp(PlayerTile player){
        player.setWeapon(this);
    }

    public int getDamage() { return damage; }

    public int getCooldown() { return cooldown; }

    public void setLastAttack(int step) {
        lastAttack = step;
    }

    public int getLastAttack() {
        return lastAttack;
    }

    public int getRange() { return range; }

    public int getCost() { return cost; }

    public abstract AttackType getAttackType();
}
