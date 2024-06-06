package mcr.gdx.dungeon.weapons.handlers;

import mcr.gdx.dungeon.characters.Player;

public class PhysicalRessourceHandler extends RessourceHandler {

  protected boolean checkResources(AttackRequest request) {
    int vigor = request.getPlayer().getVigor();
    int cost = request.getWeapon().getCost();

    return vigor >= cost;
  }

  protected void updateResources(AttackRequest request) {
    Player player = request.getPlayer();
    int cost = request.getWeapon().getCost();

    player.reduceVigor(cost);
  }

}
