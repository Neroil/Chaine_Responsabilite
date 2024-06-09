package mcr.gdx.dungeon.ChainOfResponsibility.weapons.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.weapons.AttackRequest;
import mcr.gdx.dungeon.elements.PlayerTile;

public class PhysicalRessourceHandler extends RessourceHandler {

    @Override
    protected boolean checkResources(AttackRequest request) {
        int vigor = request.getPlayer().getVigor();
        int cost = request.getWeaponCost();

        return vigor >= cost;
    }

    @Override
    protected void updateResources(AttackRequest request) {
        PlayerTile player = request.getPlayer();
        int cost = request.getWeaponCost();

        player.reduceVigor(cost);
    }


}
