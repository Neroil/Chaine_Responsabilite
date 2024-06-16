package mcr.gdx.dungeon.ChainOfResponsibility;

/**
 * The abstract handler from which all the handler derive.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
abstract public class Handler {

    private Handler successor;

    /**
     * Allow us to set a successor to our handler.
     *
     * @param successor the handler we want to set as a successor to this
     * @return          the new successor so that we can chain this function or this if we passed a null as a parameter
     */
    public Handler setSuccessor(Handler successor) {
        if (successor == null) return this;
        this.successor = successor;
        return successor;
    }

    /**
     * The handler will handle the request it is given if it has to.
     *
     * @param request   the request we want the handler to process
     * @return          the result of the chain or it can break it early
     */
    public abstract boolean handleRequest(Request request);

    /**
     * Calls the handle request if this has a successor, if not we consider we finished the chain and return true
     *
     * @param request   the request our handler will pass to his successor
     * @return          true if we arrived at the end of our chain (no successor), the further result of the chain otherwise
     */
    protected boolean invokeSuccessor(Request request) {
        if (successor != null)
            return successor.handleRequest(request);

        System.out.println("All handler passed!");
        return true;
    }

}
