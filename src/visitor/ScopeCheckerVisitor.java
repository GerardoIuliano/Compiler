package visitor;

import nodekind.NodeKind;
import nodetype.CompositeNodeType;
import nodetype.FunctionNodeType;
import nodetype.NodeType;
import nodetype.PrimitiveNodeType;
import semantic.SymbolTable;
import semantic.SymbolTableRecord;
import syntax.*;
import syntax.expr.*;
import syntax.expr.arithop.*;
import syntax.expr.constant.*;
import syntax.expr.relop.*;
import syntax.statements.*;

import java.util.ArrayList;
import java.util.List;

public class ScopeCheckerVisitor implements Visitor<Boolean, SymbolTable> {

  private boolean checkContext(List<? extends AbstractNode> nodes, SymbolTable arg) {
    if(nodes.isEmpty()){
      return true;
    }
    else {
      return nodes.stream().allMatch(node -> node.accept(this, arg));
    }
  }

  @Override
  public Boolean visit(Program program, SymbolTable arg) {
    arg.enterScope();
    boolean isFunctionSafe = checkContext(program.getFunOpList(), arg);
    boolean isVariableSafe = checkContext(program.getVarDeclOpList(), arg);
    boolean isBodySafe = program.getBodyOp().accept(this,arg);
    boolean isProgramSafe = isFunctionSafe && isVariableSafe && isBodySafe;
    if(!isProgramSafe){
      System.err.println("Program Error");
    }
    arg.exitScope();
    return isProgramSafe;
  }

  @Override
  public Boolean visit(AssignOp assignOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(BodyOp bodyOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(StringConst stringConst, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(FunOp funOp, SymbolTable arg) {
    //salvo in nome della funzione nello scope di program
    String lexema = funOp.getId().getValue();
    NodeType returnType = new PrimitiveNodeType(funOp.getType().getValue());
    CompositeNodeType compositeNodeType = createComposite(funOp.getParDeclOp());
    FunctionNodeType functionNodeType = new FunctionNodeType(compositeNodeType,returnType);
    arg.addEntry(lexema,new SymbolTableRecord(lexema,functionNodeType, NodeKind.FUNCTION));

    //apro lo scope per la funzione
    arg.enterScope();

    arg.exitScope();

    return null;
  }

  @Override
  public Boolean visit(IdInitOp idInitOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(ParDeclOp parDeclOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(VarDeclOp varDeclOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(CallFunOp callFunOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(ElseOp elseOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(IfstatOp ifstatOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(ReadOp readOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(ReturnOp returnOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(WhileOp whileOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(WriteOp writeOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(Id id, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(IdOutpar id, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(MinusOp minusOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(ParExprOp parExprOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(PrimitiveType primitiveType, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(StrCatOp strCatOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(UminusOp uminusOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(AndOp andOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(EQOp eqOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(GEOp geOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(GTOp gtOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(LEOp leOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(LTOp ltOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(NEOp neOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(NotOp notOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(OrOp orOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(TrueConst trueConst, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(FalseConst falseConst, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(IntegerConst integerConst, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(RealConst realConst, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(AddOp addOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(DiffOp diffOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(DivIntOp divIntOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(DivOp divOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(MulOp mulOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(PowOp powOp, SymbolTable arg) {
    return null;
  }

  private CompositeNodeType createComposite(List<ParDeclOp> list) {
    CompositeNodeType compositeNodeType = new CompositeNodeType(new ArrayList<NodeType>());
    for(ParDeclOp p : list){
      NodeType nodeType = new PrimitiveNodeType(p.getType().getValue());
      compositeNodeType.addNodeType(nodeType);
    }
    return compositeNodeType;
  }
}