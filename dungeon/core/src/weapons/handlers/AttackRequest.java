package weapons.handlers;

import mcr.gdx.dungeon.characters.Enemy;
import mcr.gdx.dungeon.characters.Player;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
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
