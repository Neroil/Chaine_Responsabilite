package mcr.gdx.dungeon.characters;

public class DamageRequest {
    private int damage;
    private Player player;
    private Enemy enemy;

    public DamageRequest(int damage, Player player, Enemy enemy) {
        this.damage = damage;
        this.player = player;
        this.enemy = enemy;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public Player getPlayer() {
        return player;
    }

    public void modifyDamage(int factor) {
        damage *= factor;
    }
}
