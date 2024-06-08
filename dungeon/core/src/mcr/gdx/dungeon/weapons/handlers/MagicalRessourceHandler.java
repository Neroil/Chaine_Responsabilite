package mcr.gdx.dungeon.weapons.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.elements.PlayerTile;
import mcr.gdx.dungeon.weapons.AttackRequest;

public class MagicalRessourceHandler extends RessourceHandler {


  protected boolean checkResources(AttackRequest request) {
    int mana = request.getPlayer().getMana();
    int cost = request.getWeaponCost();

    return mana >= cost;
  }

  protected void updateResources(AttackRequest request) {
    PlayerTile player = request.getPlayer();
    int cost = request.getWeaponCost();

    player.reduceMana(cost);
  }

}
