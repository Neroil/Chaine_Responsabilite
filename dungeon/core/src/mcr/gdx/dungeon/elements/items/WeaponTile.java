package mcr.gdx.dungeon.elements.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.elements.PlayerTile;

import java.sql.Timestamp;

public abstract class WeaponTile extends ItemTile {
    private final String name;
    private final int damage;
    private final int cooldown;
    private Timestamp lastAttack;
    private final int range;
    private final int cost;

    public enum AttackType {
        PHYSICAL,
        MAGICAL
    }

    public WeaponTile(Vector2 position, TextureRegion texture, String name, int damage, int cooldown, int range, int cost){
        super(position, texture);
        this.name = name;
        this.damage = damage;
        this.cooldown = cooldown;
        this.range = range;
        this.cost = cost;
    }

    @Override
    public void pickUp(PlayerTile player){
        player.setWeapon(this);
    }

    public String getName() { return name; }

    public int getDamage() { return damage; }

    public int getCooldown() { return cooldown; }

    public void setLastAttack() {
        lastAttack = new Timestamp(System.currentTimeMillis());
    }

    public long getLastAttack() {
        return lastAttack == null ? -1 : lastAttack.getTime();
    }

    public int getRange() { return range; }

    public int getCost() { return cost; }

    public abstract AttackType getAttackType();
}
