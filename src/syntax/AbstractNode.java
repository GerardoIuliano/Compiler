package syntax;

import visitor.Visitable;

public abstract class AbstractNode implements Visitable {

  private int leftLocation;
  private int rightLocation;

  public AbstractNode(int leftLocation, int rightLocation) {
    this.leftLocation = leftLocation;
    this.rightLocation = rightLocation;
  }

  public int getLeftLocation() {
    return leftLocation;
  }

  public int getRightLocation() {
    return rightLocation;
  }
}
