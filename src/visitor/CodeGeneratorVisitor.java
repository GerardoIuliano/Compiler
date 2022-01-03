package visitor;

import semantic.SymbolTable;
import syntax.*;
import syntax.expr.*;
import syntax.expr.arithop.*;
import syntax.expr.constant.*;
import syntax.expr.relop.*;
import syntax.statements.*;

public class CodeGeneratorVisitor implements Visitor<String, SymbolTable>{

  @Override
  public String visit(Program program, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(AssignOp assignOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(BodyOp bodyOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(FunOp funOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(IdInitOp idInitOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(ParDeclOp parDeclOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(VarDeclOp varDeclOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(CallFunOp callFunOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(ElseOp elseOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(IfstatOp ifstatOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(ReadOp readOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(ReturnOp returnOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(WhileOp whileOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(WriteOp writeOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(Id id, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(IdOutpar id, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(MinusOp minusOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(ParExprOp parExprOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(PrimitiveType primitiveType, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(StrCatOp strCatOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(UminusOp uminusOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(AndOp andOp, SymbolTable arg) {
    String left = andOp.getLeftValue().accept(this, arg);
    String right = andOp.getRightValue().accept(this, arg);
    return String.format("%s && %s", left, right);
  }

  @Override
  public String visit(EQOp eqOp, SymbolTable arg) {
    String left = eqOp.getLeftValue().accept(this, arg);
    String right = eqOp.getRightValue().accept(this, arg);
    return String.format("%s == %s", left, right);
  }

  @Override
  public String visit(GEOp geOp, SymbolTable arg) {
    String left = geOp.getLeftValue().accept(this, arg);
    String right = geOp.getRightValue().accept(this, arg);
    return String.format("%s >= %s", left, right);  }

  @Override
  public String visit(GTOp gtOp, SymbolTable arg) {
    String left = gtOp.getLeftValue().accept(this, arg);
    String right = gtOp.getRightValue().accept(this, arg);
    return String.format("%s > %s", left, right);
  }

  @Override
  public String visit(LEOp leOp, SymbolTable arg) {
    String left = leOp.getLeftValue().accept(this, arg);
    String right = leOp.getRightValue().accept(this, arg);
    return String.format("%s <= %s", left, right);
  }

  @Override
  public String visit(LTOp ltOp, SymbolTable arg) {
    String left = ltOp.getLeftValue().accept(this, arg);
    String right = ltOp.getRightValue().accept(this, arg);
    return String.format("%s < %s", left, right);
  }

  @Override
  public String visit(NEOp neOp, SymbolTable arg) {
    String left = neOp.getLeftValue().accept(this, arg);
    String right = neOp.getRightValue().accept(this, arg);
    return String.format("%s != %s", left, right);
  }

  @Override
  public String visit(NotOp notOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(OrOp orOp, SymbolTable arg) {
    String left = orOp.getLeftValue().accept(this, arg);
    String right = orOp.getRightValue().accept(this, arg);
    return String.format("%s || %s", left, right);
  }

  @Override
  public String visit(AddOp addOp, SymbolTable arg) {
    String left = addOp.getLeftValue().accept(this, arg);
    String right = addOp.getRightValue().accept(this, arg);
    return String.format("%s + %s", left, right);
  }

  @Override
  public String visit(DiffOp diffOp, SymbolTable arg) {
    String left = diffOp.getLeftValue().accept(this, arg);
    String right = diffOp.getRightValue().accept(this, arg);
    return String.format("%s - %s", left, right);
  }

  @Override
  public String visit(DivIntOp divIntOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(DivOp divOp, SymbolTable arg) {
    return null;
  }

  @Override
  public String visit(MulOp mulOp, SymbolTable arg) {
    String left = mulOp.getLeftValue().accept(this, arg);
    String right = mulOp.getRightValue().accept(this, arg);
    return String.format("%s * %s", left, right);
  }

  @Override
  public String visit(PowOp powOp, SymbolTable arg) {
    return "null";
  }

  @Override
  public String visit(TrueConst trueConst, SymbolTable arg) {
    return Boolean.toString(trueConst.getValue());
  }

  @Override
  public String visit(FalseConst falseConst, SymbolTable arg) {
    return Boolean.toString(falseConst.getValue());
  }

  @Override
  public String visit(IntegerConst integerConst, SymbolTable arg) {
    return Integer.toString(integerConst.getValue());
  }

  @Override
  public String visit(RealConst realConst, SymbolTable arg) {
    return Float.toString(realConst.getValue());
  }

  @Override
  public String visit(StringConst stringConst, SymbolTable arg) {
    return "\""+stringConst.getValue()+"\"";
  }
}
