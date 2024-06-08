package mcr.gdx.dungeon.ChainOfResponsibility;

abstract public class GenericHandler {

  private GenericHandler successor;

  public GenericHandler setSuccessor(GenericHandler successor) {
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
