package weapons.handlers;

import mcr.gdx.dungeon.characters.Player;

public class MagicalRessourceHandler extends RessourceHandler {

  protected boolean checkResources(AttackRequest request) {
    int mana = request.getPlayer().getMana();
    int cost = request.getWeapon().getCost();

    return mana >= cost;
  }

  protected void updateResources(AttackRequest request) {
    Player player = request.getPlayer();
    int cost = request.getWeapon().getCost();

    player.reduceMana(cost);
  }

}
