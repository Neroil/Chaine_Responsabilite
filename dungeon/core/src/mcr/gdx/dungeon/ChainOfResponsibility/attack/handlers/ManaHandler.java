package mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;
import mcr.gdx.dungeon.elements.PlayerTile;

public class ManaHandler extends RessourceHandler {

    @Override
    protected boolean checkResources(AttackRequest request) {
        int mana = request.getPlayer().getMana();
        int cost = request.getWeaponCost();

        return mana >= cost;
    }

    @Override
    protected void updateResources(AttackRequest request) {
        PlayerTile player = request.getPlayer();
        int cost = request.getWeaponCost();

        player.reduceMana(cost);
    }

}
