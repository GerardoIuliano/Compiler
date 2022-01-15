package visitor;

import nodekind.NodeKind;
import nodetype.FunctionNodeType;
import nodetype.NodeType;
import nodetype.PrimitiveNodeType;
import semantic.SymbolTable;
import syntax.*;
import syntax.expr.*;
import syntax.expr.arithop.*;
import syntax.expr.constant.*;
import syntax.expr.relop.*;
import syntax.statements.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class CodeGeneratorVisitor implements Visitor<String, SymbolTable>{

  //data una lista di nodi esegua la join dei valori
  private String beautify(List<? extends AbstractNode> nodes, StringJoiner joiner, SymbolTable table){
    nodes.forEach(node -> joiner.add(node.accept(this, table)));
    return joiner.toString();
  }

  private String beautify(LinkedList<Statement> nodes, StringJoiner joiner, SymbolTable table){
    for(Statement s: nodes) {
      if(s!= null)
        joiner.add(s.accept(this, table));
    }
    return joiner.toString();
  }

  @Override
  public String visit(Program program, SymbolTable arg) {
    String vars = "", funs = "";
    arg.enterScope();
    if(program.getVarDeclOpList() != null)
      vars = beautify(program.getVarDeclOpList(), new StringJoiner("\n"), arg);
    if(program.getFunOpList() != null)
      funs = beautify(program.getFunOpList(), new StringJoiner("\n"), arg);

    String main = program.getBodyOp().accept(this, arg);
    arg.exitScope();
    return String.format("#include<stdio.h>\n#include<stdlib.h>\n#include<math.h>\n#include<stdbool.h>\n#include<stdlib.h>\n#include<stddef.h>\n#include<string.h>\n\n" + "%s\n%s\nint main(int argc, char *argv[]){\n%s\n}",vars, funs, main);
  }

  @Override
  public String visit(AssignOp assignOp, SymbolTable arg) {
    String left = assignOp.getId().accept(this, arg);
    String right = assignOp.getExpr().accept(this, arg);
    if(assignOp.getExpr() instanceof CallFunOp)
      right = right.substring(0,right.length()-1);
    return String.format("%s = %s;", left, right);
  }

  @Override
  public String visit(BodyOp bodyOp, SymbolTable arg) {
    String vars = "", stats = "";
    arg.enterScope();
    if(bodyOp.getVarDeclList() != null)
      vars = beautify(bodyOp.getVarDeclList(), new StringJoiner("\n"), arg);
    if(bodyOp.getStatList() != null)
      stats = beautify(bodyOp.getStatList(), new StringJoiner("\n"), arg);
    arg.exitScope();
    return String.format("%s\n%s\n", vars, stats);
  }

  @Override
  public String visit(FunOp funOp, SymbolTable arg) {
    String params = "";
    String fname = funOp.getId().accept(this, arg);
    arg.enterScope();
    if(funOp.getParDeclOp() != null)
      params = beautify(funOp.getParDeclOp(), new StringJoiner(","), arg);
    String vars = "", stats = "";
    if(funOp.getBodyOp().getVarDeclList() != null)
      vars = beautify(funOp.getBodyOp().getVarDeclList(), new StringJoiner("\n"), arg);
    if(funOp.getBodyOp().getStatList() != null)
      stats = beautify(funOp.getBodyOp().getStatList(), new StringJoiner("\n"), arg);
    String body = vars+stats;
    String retType="void";
    if(funOp.getType()!=null)
      retType = funOp.getType().accept(this, arg);
    arg.exitScope();
    return String.format("%s %s(%s){\n%s\n}\n", retType, fname, params, body);
  }

  @Override
  public String visit(IdInitOp idInitOp, SymbolTable arg) {
    String id = idInitOp.getId().accept(this, arg);
    String tipo = chooseType(arg.lookup(idInitOp.getId().getValue()).get().getNodeType().toString());
    if(idInitOp.getExpr() != null) {
      String expr = idInitOp.getExpr().accept(this, arg);
      if(idInitOp.getExpr() instanceof CallFunOp)
        expr = expr.substring(0, expr.length()-1);
      return String.format("%s %s = %s;\n",tipo, id, expr);
    }
    return String.format("%s %s;\n",tipo, id);
  }

  @Override
  public String visit(ParDeclOp parDeclOp, SymbolTable arg) {
    String type = parDeclOp.getType().accept(this, arg);
    String id = parDeclOp.getId().accept(this, arg);
    return String.format("%s %s", type, id);
  }

  @Override
  public String visit(VarDeclOp varDeclOp, SymbolTable arg) {
    String type = varDeclOp.getPrimitiveType().accept(this, arg);
    String ids = beautify(varDeclOp.getIdInitList(), new StringJoiner("\n"), arg);
    //costruire la dichiarazione usando idInitOp in modo da risolvere l'inferenza di tipo.
    return String.format("%s", ids);
  }

  @Override
  public String visit(CallFunOp callFunOp, SymbolTable arg) {
    String fName = callFunOp.getId().accept(this, arg);
    StringJoiner funJoiner = new StringJoiner(", ");
    if(callFunOp.getParams() != null) {
      callFunOp.getParams().forEach(i -> funJoiner.add(i.accept(this, arg)));
      return String.format("%s(%s);", fName, funJoiner.toString());
    }
    return String.format("%s();", fName);
  }

  @Override
  public String visit(ElseOp elseOp, SymbolTable arg) {
    return elseOp.getBodyOp().accept(this, arg);
  }

  @Override
  public String visit(IfstatOp ifstatOp, SymbolTable arg) {
    String condition = ifstatOp.getExpr().accept(this, arg);
    String body = ifstatOp.getBodyOp().accept(this, arg);
    String elseOp;
    if (ifstatOp.getElseop() != null) {
      elseOp = ifstatOp.getElseop().accept(this, arg);
      return String.format("if(%s){\n%s\n} else {\n%s\n}", condition, body, elseOp);
    }
    return String.format("if(%s){\n%s\n}", condition, body);
  }

  @Override
  public String visit(ReadOp readOp, SymbolTable arg) {
    String expr="";
    if(readOp.getExpr()!= null)
      expr = readOp.getExpr().accept(this, arg);

    StringJoiner scanfs = new StringJoiner("\n");


    readOp.getIds().forEach(var -> {
      String type = this.formatType(arg.lookup(var.getValue()).get().getNodeType());
      scanfs.add(String.format("scanf(\"%s\", &%s);", type, var.getValue()));
    });
    if(expr.equals(""))
      return scanfs.toString();
    else {
      String printf = String.format("printf(%s);\n", expr);
      return printf + scanfs.toString();
    }
  }

  private String formatType(NodeType type){
    PrimitiveNodeType pType = PrimitiveNodeType.class.cast(type);
    switch(pType.getNodoType()){
      case "real":
        return "%lf";
      case "string":
        return "%s";
      default:
        return "%d";
    }
  }

  @Override
  public String visit(ReturnOp returnOp, SymbolTable arg) {
    String toReturn = returnOp.getExpr().accept(this, arg);
    return String.format("return %s;", toReturn);
  }

  @Override
  public String visit(WhileOp whileOp, SymbolTable arg) {
    String expr = whileOp.getExpr().accept(this, arg);
    String body = whileOp.getBodyOp().accept(this, arg);
    return String.format("while(%s){\n%s\n}", expr, body);
  }

  @Override
  public String visit(WriteOp writeOp, SymbolTable arg) {
    String expr = writeOp.getExpr().accept(this, arg);
    return writeOp.getWriteC(expr,writeOp.getExpr());
  }

  @Override
  public String visit(Id id, SymbolTable arg) {
      return id.getValue();
  }

  @Override
  public String visit(IdOutpar id, SymbolTable arg) {
    return "&"+id.getValue();
  }

  @Override
  public String visit(MinusOp minusOp, SymbolTable arg) {
    String expr = minusOp.getExpr().accept(this, arg);
    return String.format("-%s", expr);
  }

  @Override
  public String visit(ParExprOp parExprOp, SymbolTable arg) {
    String expr = parExprOp.getExpr().accept(this, arg);
    return String.format("(%s)", expr);
  }

  @Override
  public String visit(PrimitiveType primitiveType, SymbolTable arg) {
    return chooseType(primitiveType.getValue());
  }

  public String chooseType(String s) {
    if(s.equals("integer"))
      return "int";
    if(s.equals("bool")) //<stdbool.h>
      return "bool";
    if(s.equals("real"))
      return "double";
    if(s.equals("string"))
      return "char*";
    return s;
  }

  @Override
  public String visit(StrCatOp strCatOp, SymbolTable arg) {
    String left = strCatOp.getLeftValue().accept(this, arg);
    String right = strCatOp.getRightValue().accept(this, arg);
    return String.format("strcat(%s, %s)", left, right);
  }

  @Override
  public String visit(UminusOp uminusOp, SymbolTable arg) {
    String expr = uminusOp.getExpr().accept(this, arg);
    return String.format("-%s", expr);
  }

  @Override
  public String visit(AndOp andOp, SymbolTable arg) {
    String left = andOp.getLeftValue().accept(this, arg);
    String right = andOp.getRightValue().accept(this, arg);
    return String.format("%s && %s", left, right);
  }

  @Override
  public String visit(EQOp eqOp, SymbolTable arg) {
    return equalString(eqOp.getLeftValue(),eqOp.getRightValue(), arg, true);
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
    return equalString(neOp.getLeftValue(),neOp.getRightValue(), arg, false);
  }

  @Override
  public String visit(NotOp notOp, SymbolTable arg) {
    String expr = notOp.getExpr().accept(this, arg);
    return String.format("!(%s)", expr);
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
    String left = divIntOp.getLeftValue().accept(this, arg);
    String right = divIntOp.getRightValue().accept(this, arg);
    return String.format("%s / %s", left, right);
  }

  @Override
  public String visit(DivOp divOp, SymbolTable arg) {
    String left = divOp.getLeftValue().accept(this, arg);
    String right = divOp.getRightValue().accept(this, arg);
    return String.format("%s / %s", left, right);
  }

  @Override
  public String visit(MulOp mulOp, SymbolTable arg) {
    String left = mulOp.getLeftValue().accept(this, arg);
    String right = mulOp.getRightValue().accept(this, arg);
    return String.format("%s * %s", left, right);
  }

  @Override
  public String visit(PowOp powOp, SymbolTable arg) {
    String left = powOp.getLeftValue().accept(this, arg);
    String right = powOp.getRightValue().accept(this, arg);
    return String.format("pow(%s, %s)", left, right);
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

  public String equalString(Expr exprLeft, Expr exprRight, SymbolTable arg, boolean equal){
    Id id;
    CallFunOp call;
    String left = exprLeft.accept(this, arg), right = exprRight.accept(this, arg);
    boolean leftOk = false;
    boolean rightOk = false;
    if(exprLeft instanceof Id){
      id =(Id)exprLeft;
      if(arg.lookup(id.getValue()).get().getNodeType().equals(new PrimitiveNodeType("string"))){
        left = "&"+left;
        leftOk = true;
      }
    }
    if(exprRight instanceof Id){
      id =(Id)exprRight;
      if(arg.lookup(id.getValue()).get().getNodeType().equals(new PrimitiveNodeType("string"))){
        right = "&"+right;
        rightOk = true;
      }
    }
    if(exprLeft instanceof StringConst){
      leftOk = true;
    }
    if(exprRight instanceof StringConst){
      rightOk = true;
    }
    if(exprLeft instanceof CallFunOp){
      call =(CallFunOp) exprLeft;
      id=call.getId();
      if(((FunctionNodeType) arg.lookup(id.getValue()).get().getNodeType()).getNodeType().equals(new PrimitiveNodeType("string")))
        leftOk = true;
    }
    if(exprRight instanceof CallFunOp){
      call =(CallFunOp) exprRight;
      id=call.getId();
      if(((FunctionNodeType) arg.lookup(id.getValue()).get().getNodeType()).getNodeType().equals(new PrimitiveNodeType("string")))
        rightOk = true;
    }
    if(leftOk && rightOk){
      if(equal)
        return String.format("strcmp(%s,%s) == 0", left, right);
      else
        return String.format("strcmp(%s,%s) != 0", left, right);
    }
    if(equal)
      return String.format("%s == %s", left, right);
    else
      return String.format("%s != %s", left, right);
  }
}
