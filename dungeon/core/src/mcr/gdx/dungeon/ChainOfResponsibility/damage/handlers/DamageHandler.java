package mcr.gdx.dungeon.ChainOfResponsibility.damage.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.ChainOfResponsibility.damage.DamageRequest;

/**
 * The abstract handler from which all the handler from the damage request chain derive.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public abstract class DamageHandler extends Handler {

    /**
     * Override of the handle request function, so that we always cast the Request into an DamageRequest.
     *
     * @param request   the Request we may have to handle
     * @return          the result of the chain
     */
    @Override
    public boolean handleRequest(Request request) {
        return handleDamageRequest((DamageRequest) request);
    }

    /**
     * The new handle request function we have to implement for our attack chain.
     *
     * @param request   the DamageRequest we may have to handle
     * @return          the result of the chain
     */
    protected abstract boolean handleDamageRequest(DamageRequest request);
}
