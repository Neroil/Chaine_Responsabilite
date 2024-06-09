package mcr.gdx.dungeon.ChainOfResponsibility.characters.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.characters.DamageRequest;
import mcr.gdx.dungeon.elements.CharacterTile;

public class HitHandler extends DamageHandler {

    @Override
    protected boolean handleDamageRequest(DamageRequest request) {
        System.out.println("Trying to hit");
        for(CharacterTile target : request.getTargets()){
            System.out.println("Target hit!!");
            target.reduceLife(request.getDamage());
        }
        return invokeSuccessor(request);
    }
}
