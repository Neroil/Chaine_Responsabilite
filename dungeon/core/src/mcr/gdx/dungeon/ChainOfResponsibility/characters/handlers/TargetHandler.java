package mcr.gdx.dungeon.ChainOfResponsibility.characters.handlers;

import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.characters.DamageRequest;
import mcr.gdx.dungeon.elements.CharacterTile;

public class TargetHandler extends DamageHandler {


    @Override
    protected boolean handleDamageRequest(DamageRequest request) {
        for(Vector2 position : request.getPositionsAttacked()){
            for(CharacterTile entity : request.getCollidableEntities()){
                if(entity.position.equals(position)){
                    request.addTarget(entity);
                    break;
                }
            }
        }
        return invokeSuccessor(request);
    }

}
