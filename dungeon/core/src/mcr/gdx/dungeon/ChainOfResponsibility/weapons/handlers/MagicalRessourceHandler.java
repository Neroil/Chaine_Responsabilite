package mcr.gdx.dungeon.ChainOfResponsibility.weapons.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.weapons.AttackRequest;
import mcr.gdx.dungeon.elements.PlayerTile;

public class MagicalRessourceHandler extends RessourceHandler {

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
