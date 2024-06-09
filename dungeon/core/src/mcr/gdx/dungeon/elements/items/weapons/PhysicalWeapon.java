package mcr.gdx.dungeon.elements.items.weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.elements.items.WeaponTile;

public abstract class PhysicalWeapon extends WeaponTile {

    public PhysicalWeapon(Vector2 position, TextureRegion texture, int damage, int cooldown, int range, int cost) {
        super(position, texture, damage, cooldown, range, cost);
    }

    @Override
    public AttackType getAttackType() {
        return AttackType.PHYSICAL;
    }
}
