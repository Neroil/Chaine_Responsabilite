package mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;
import mcr.gdx.dungeon.elements.PlayerTile;

public class VigorHandler extends RessourceHandler {

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
