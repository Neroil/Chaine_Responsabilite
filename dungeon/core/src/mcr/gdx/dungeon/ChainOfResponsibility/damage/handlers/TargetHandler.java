package mcr.gdx.dungeon.ChainOfResponsibility.damage.handlers;

import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.damage.DamageRequest;
import mcr.gdx.dungeon.elements.CharacterTile;

/**
 * Allow us to fill the target list of our DamageRequest by checking if we find an entity on our attacked positions.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class TargetHandler extends DamageHandler {


    /**
     * Fill the target list with the entities found on our attacked positons
     *
     * @param request   the DamageRequest we may have to handle
     * @return          the result of the chain
     */
    @Override
    protected boolean handleDamageRequest(DamageRequest request) {
        System.out.println("Searching targets");
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
