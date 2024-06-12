package mcr.gdx.dungeon.ChainOfResponsibility;

abstract public class Handler {

    private Handler successor;

    public Handler setSuccessor(Handler successor) {
        if (successor == null) return this;
        this.successor = successor;
        return successor;
    }

    public abstract boolean handleRequest(Request request);

    protected boolean invokeSuccessor(Request request) {
        if (successor != null)
            return successor.handleRequest(request);

        System.out.println("All handler passed!");
        return true;
    }

}
