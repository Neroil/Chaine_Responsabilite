package weapons.handlers;

import mcr.gdx.dungeon.characters.Enemy;
import mcr.gdx.dungeon.characters.Player;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import weapons.Weapon;

public abstract class AttackRequest implements Request{

  private final Player player;
  private final Weapon weapon;

  public AttackRequest(Player player, Weapon weapon) {
    this.player = player;
    this.weapon = weapon;
  }

  public Player getPlayer() {
    return player;
  }

  public Weapon getWeapon() { return weapon; }
}
