package visitor;

import nodetype.NodeType;
import nodetype.PrimitiveNodeType;
import nodetype.TypeTable;
import nodetype.TypeTableRecord;
import semantic.SymbolTable;
import syntax.*;
import syntax.expr.*;
import syntax.expr.arithop.*;
import syntax.expr.constant.*;
import syntax.expr.relop.*;
import syntax.statements.*;

public class TypeCheckerVisitor implements Visitor<NodeType, SymbolTable> {

  private static TypeTable typeTable = new TypeTable();

  @Override
  public NodeType visit(Program program, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(AssignOp assignOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(BodyOp bodyOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(FunOp funOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(IdInitOp idInitOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(ParDeclOp parDeclOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(VarDeclOp varDeclOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(CallFunOp callFunOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(ElseOp elseOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(IfstatOp ifstatOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(ReadOp readOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(ReturnOp returnOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(WhileOp whileOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(WriteOp writeOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(Id id, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(IdOutpar id, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(MinusOp minusOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(ParExprOp parExprOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(PrimitiveType primitiveType, SymbolTable arg) {
    return new PrimitiveNodeType(primitiveType.getValue());
  }

  @Override
  public NodeType visit(StrCatOp strCatOp, SymbolTable arg) {
    NodeType arg1 = strCatOp.getLeftValue().accept(this,arg);
    NodeType arg2 = strCatOp.getRightValue().accept(this,arg);
    return typeTable.check("STRCAT",arg1,arg2);
  }

  @Override
  public NodeType visit(UminusOp uminusOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(AndOp andOp, SymbolTable arg) {
    NodeType arg1 = andOp.getLeftValue().accept(this,arg);
    NodeType arg2 = andOp.getRightValue().accept(this,arg);
    return typeTable.check("OR",arg1,arg2);
  }

  @Override
  public NodeType visit(EQOp eqOp, SymbolTable arg) {
    NodeType arg1 = eqOp.getLeftValue().accept(this,arg);
    NodeType arg2 = eqOp.getRightValue().accept(this,arg);
    return typeTable.check("REL",arg1,arg2);
  }

  @Override
  public NodeType visit(GEOp geOp, SymbolTable arg) {
    NodeType arg1 = geOp.getLeftValue().accept(this,arg);
    NodeType arg2 = geOp.getRightValue().accept(this,arg);
    return typeTable.check("REL",arg1,arg2);
  }

  @Override
  public NodeType visit(GTOp gtOp, SymbolTable arg) {
    NodeType arg1 = gtOp.getLeftValue().accept(this,arg);
    NodeType arg2 = gtOp.getRightValue().accept(this,arg);
    return typeTable.check("REL",arg1,arg2);
  }

  @Override
  public NodeType visit(LEOp leOp, SymbolTable arg) {
    NodeType arg1 = leOp.getLeftValue().accept(this,arg);
    NodeType arg2 = leOp.getRightValue().accept(this,arg);
    return typeTable.check("REL",arg1,arg2);
  }

  @Override
  public NodeType visit(LTOp ltOp, SymbolTable arg) {
    NodeType arg1 = ltOp.getLeftValue().accept(this,arg);
    NodeType arg2 = ltOp.getRightValue().accept(this,arg);
    return typeTable.check("REL",arg1,arg2);
  }

  @Override
  public NodeType visit(NEOp neOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(NotOp notOp, SymbolTable arg) {
    return null;
  }

  @Override
  public NodeType visit(OrOp orOp, SymbolTable arg) {
    NodeType arg1 = orOp.getLeftValue().accept(this,arg);
    NodeType arg2 = orOp.getRightValue().accept(this,arg);
    return typeTable.check("OR",arg1,arg2);
  }

  @Override
  public NodeType visit(TrueConst trueConst, SymbolTable arg) {
    return new PrimitiveNodeType("bool");
  }

  @Override
  public NodeType visit(FalseConst falseConst, SymbolTable arg) {
    return new PrimitiveNodeType("bool");
  }

  @Override
  public NodeType visit(IntegerConst integerConst, SymbolTable arg) {
    return new PrimitiveNodeType("integer");
  }

  @Override
  public NodeType visit(RealConst realConst, SymbolTable arg) {
    return new PrimitiveNodeType("real");
  }

  @Override
  public NodeType visit(StringConst stringConst, SymbolTable arg) {
    return new PrimitiveNodeType("string");
  }

  @Override
  public NodeType visit(AddOp addOp, SymbolTable arg) {
    NodeType arg1 = addOp.getLeftValue().accept(this,arg);
    NodeType arg2 = addOp.getRightValue().accept(this,arg);
    return typeTable.check("ARITH",arg1,arg2);
  }

  @Override
  public NodeType visit(DiffOp diffOp, SymbolTable arg) {
    NodeType arg1 = diffOp.getLeftValue().accept(this,arg);
    NodeType arg2 = diffOp.getRightValue().accept(this,arg);
    return typeTable.check("ARITH",arg1,arg2);
  }

  @Override
  public NodeType visit(DivIntOp divIntOp, SymbolTable arg) {
    NodeType arg1 = divIntOp.getLeftValue().accept(this,arg);
    NodeType arg2 = divIntOp.getRightValue().accept(this,arg);
    return typeTable.check("DIVINT",arg1,arg2);
  }

  @Override
  public NodeType visit(DivOp divOp, SymbolTable arg) {
    NodeType arg1 = divOp.getLeftValue().accept(this,arg);
    NodeType arg2 = divOp.getRightValue().accept(this,arg);
    return typeTable.check("ARITH",arg1,arg2);
  }

  @Override
  public NodeType visit(MulOp mulOp, SymbolTable arg) {
    NodeType arg1 = mulOp.getLeftValue().accept(this,arg);
    NodeType arg2 = mulOp.getRightValue().accept(this,arg);
    return typeTable.check("ARITH",arg1,arg2);
  }

  @Override
  public NodeType visit(PowOp powOp, SymbolTable arg) {
    NodeType arg1 = powOp.getLeftValue().accept(this,arg);
    NodeType arg2 = powOp.getRightValue().accept(this,arg);
    return typeTable.check("ARITH",arg1,arg2);
  }
}
