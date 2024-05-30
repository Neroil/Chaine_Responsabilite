package weapons.handlers;

import characters.Enemy;
import characters.Player;
import weapons.Weapon;

public abstract class AttackHandler {

  private AttackHandler successor;

  public AttackHandler setSuccessor(AttackHandler successor) {
    this.successor = successor;
    return successor;
  }

  protected boolean handleRequestGeneric(AttackRequest request, int manaOrVigor) {
    Player player = request.getPlayer();
    Enemy enemy = request.getEnemy();

    Weapon weapon = request.getWeapon();
    int cost = weapon.getCost();
    int damage = weapon.getDamage();
    boolean available = weapon.isAvailable();

    if (manaOrVigor >= cost && available) {
      // Checker aussi la distance entre le joueur et l'ennemi
      // (range >= distance)
      System.out.println("Physical attack handled!");
      player.reduceVigor(cost);
      // enemy.reduceLifePoints(damage);
    } else
      System.out.println("Not enough vigor!");

    return invokeSuccessor(request);
  }

  public abstract boolean handleRequest(AttackRequest request);

  protected boolean invokeSuccessor(AttackRequest request) {
    if (successor != null)
      return successor.handleRequest(request);

    System.out.println("All handler passed!");
    return true; // End of the chain, attack succeeds
  }

}
