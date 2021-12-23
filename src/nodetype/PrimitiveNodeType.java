package nodetype;

public class PrimitiveNodeType implements NodeType {

  private String nodoType;

  public PrimitiveNodeType(String nodoType) {
    this.nodoType = nodoType;
  }

  public String getNodoType() {
    return nodoType;
  }

  public void setNodoType(String nodoType) {
    this.nodoType = nodoType;
  }

  @Override
  public PrimitiveNodeType checkAdd(PrimitiveNodeType type) {
    return null;
  }

  @Override
  public PrimitiveNodeType checkSub(PrimitiveNodeType type) {
    return null;
  }

  @Override
  public PrimitiveNodeType checkMul(PrimitiveNodeType type) {
    return null;
  }

  @Override
  public PrimitiveNodeType checkDiv(PrimitiveNodeType type) {
    return null;
  }

  @Override
  public PrimitiveNodeType checkRel(PrimitiveNodeType type) {
    return null;
  }
}
