package mcr.gdx.dungeon.characters.handlers;

import mcr.gdx.dungeon.characters.DamageRequest;
import mcr.gdx.dungeon.elements.CharacterTile;

public class HitHandler extends DamageHandler {

    @Override
    protected boolean handleDamageRequest(DamageRequest request) {
        for(CharacterTile target : request.getTargets()){
            target.reduceLife(request.getDamage());
        }
        return invokeSuccessor(request);
    }
}
