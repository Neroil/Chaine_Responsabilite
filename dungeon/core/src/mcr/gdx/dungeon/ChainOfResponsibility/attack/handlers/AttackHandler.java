package mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;

/**
 * The abstract handler from which all the handlers of the attack chain derive.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
abstract public class AttackHandler extends Handler {

    /**
     * Override of the handle request function, so that we always cast the Request into an AttackRequest.
     *
     * @param request   the Request we may have to handle
     * @return          the result of the chain
     */
    @Override
    public boolean handleRequest(Request request) {
        return handleAttackRequest((AttackRequest) request);
    }

    /**
     * The new handle request function we have to implement for our attack chain.
     *
     * @param request   the AttackRequest we may have to handle
     * @return          the result of the chain
     */
    protected abstract boolean handleAttackRequest(AttackRequest request);
}
