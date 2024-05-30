package weapons.handlers;

import characters.Enemy;
import characters.Player;
import weapons.Weapon;

public abstract class AttackRequest {

  private final Player player;
  private final Weapon weapon;
  private final Enemy enemy;

  public AttackRequest(Player player, Weapon weapon, Enemy enemy) {
    this.player = player;
    this.weapon = weapon;
    this.enemy = enemy;
  }

  public Player getPlayer() {
    return player;
  }

  public Weapon getWeapon() { return weapon; }

  public Enemy getEnemy() {
    return enemy;
  }

}
