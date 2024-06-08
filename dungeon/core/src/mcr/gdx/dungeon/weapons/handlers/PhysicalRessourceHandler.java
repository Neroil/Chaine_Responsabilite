package mcr.gdx.dungeon.weapons.handlers;

import mcr.gdx.dungeon.characters.Player;
import mcr.gdx.dungeon.weapons.AttackRequest;

public class PhysicalRessourceHandler extends RessourceHandler {


  protected boolean checkResources(AttackRequest request) {
    int vigor = request.getPlayer().getVigor();
    int cost = request.getWeaponCost();

    return vigor >= cost;
  }

  protected void updateResources(AttackRequest request) {
    Player player = request.getPlayer();
    int cost = request.getWeaponCost();

    player.reduceVigor(cost);
  }


}
