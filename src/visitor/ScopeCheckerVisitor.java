package visitor;

import error.ErrorHandler;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ScopeCheckerVisitor implements Visitor<Boolean, SymbolTable> {

  private boolean checkContext(List<? extends AbstractNode> nodes, SymbolTable arg) {
    if(nodes != null) {
      if (nodes.isEmpty()) {
        return true;
      } else {
        return nodes.stream().allMatch(node -> node.accept(this, arg));
      }
    }else return true;
  }
  private boolean checkContext(LinkedList<Statement> nodes, SymbolTable arg) {
    Boolean returnSafe = true;
    if(nodes != null){
      for (Statement s:nodes){
        if(s != null)
         returnSafe = s.accept(this, arg);
         if(!returnSafe){
           return false;
         }
      }
    }
    return true;
  }

  @Override
  public Boolean visit(Program program, SymbolTable arg) {
    arg.enterScope();
    boolean isFunctionSafe = checkContext(program.getFunOpList(), arg);
    boolean isVariableSafe = checkContext(program.getVarDeclOpList(), arg);
    boolean isBodySafe = program.getBodyOp().accept(this,arg);
    boolean isProgramSafe = isFunctionSafe && isVariableSafe && isBodySafe;
    if(!isProgramSafe){
      ErrorHandler errorHandler = new ErrorHandler("Errore di compilazione");
    }
    arg.exitScope();
    return isProgramSafe;
  }

  @Override
  public Boolean visit(AssignOp assignOp, SymbolTable arg) {
    String lexema = assignOp.getId().getValue();
    Optional<SymbolTableRecord> tableRecord = arg.lookup(lexema);
    if(tableRecord.isEmpty()){
      new ErrorHandler("Variabile "+assignOp.getId().getValue()+" non dichiarata ");
      return false;
    }else return true;
  }

  @Override
  public Boolean visit(BodyOp bodyOp, SymbolTable arg) {
      arg.enterScope();
      Boolean varDeclSafe = checkContext(bodyOp.getVarDeclList(), arg);
      Boolean statListSafe = checkContext(bodyOp.getStatList(), arg);
      arg.exitScope();
    return varDeclSafe && statListSafe;
  }

  @Override
  public Boolean visit(StringConst stringConst, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(FunOp funOp, SymbolTable arg) {
    Boolean funNameSafe = saveFunName(funOp, arg);

    arg.enterScope();
    Boolean parDeclSafe = checkContext(funOp.getParDeclOp(), arg);
    Boolean varDeclSafe = checkContext(funOp.getBodyOp().getVarDeclList(),arg);
    Boolean statListSafe = checkContext(funOp.getBodyOp().getStatList(),arg);
    arg.exitScope();

    return varDeclSafe && statListSafe && parDeclSafe && funNameSafe;
  }

  @Override
  public Boolean visit(IdInitOp idInitOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(ParDeclOp parDeclOp, SymbolTable arg) {
    String lexema = parDeclOp.getId().getValue();
    String type = parDeclOp.getType().getValue();
    if(!arg.probe(lexema)) {
      arg.addEntry(lexema, new SymbolTableRecord(lexema, new PrimitiveNodeType(type), findKind(parDeclOp)));
      return true;
    }else{
      new ErrorHandler("Errore dichiarazione multipla parametri");
      return false;
    }

  }

  @Override
  public Boolean visit(VarDeclOp varDeclOp, SymbolTable arg) {
    String lexema;
    PrimitiveNodeType nodeType;

    for(IdInitOp initOp : varDeclOp.getIdInitList()){
      lexema=initOp.getId().getValue();
      if(!arg.probe(lexema)){
        if(!varDeclOp.getPrimitiveType().getValue().equals("var")){
          nodeType = new PrimitiveNodeType(varDeclOp.getPrimitiveType().getValue());
        }else{
          nodeType = new PrimitiveNodeType(inference(initOp.getExpr()));
        }
        arg.addEntry(lexema,new SymbolTableRecord(lexema,nodeType,NodeKind.VARIABLE));
      }else{
        new ErrorHandler("Errore dichiarazione multipla variabili");
        return false;
      }
    }
    return true;
  }

  @Override
  public Boolean visit(CallFunOp callFunOp, SymbolTable arg) {
    return null;
  }

  @Override
  public Boolean visit(ElseOp elseOp, SymbolTable arg) {
    return elseOp.getBodyOp().accept(this, arg);
  }

  @Override
  public Boolean visit(IfstatOp ifstatOp, SymbolTable arg) {
    Boolean ifSafe = ifstatOp.getBodyOp().accept(this, arg);
    if(ifstatOp.getElseop() != null) {
      Boolean elseSafe = ifstatOp.getElseop().accept(this, arg);
      return ifSafe && elseSafe;
    }
    return ifSafe;
  }

  @Override
  public Boolean visit(ReadOp readOp, SymbolTable arg) {
    return null;
    //return checkContext(readOp.getIds(),arg) && readOp.getExpr().accept(this, arg);
  }

  @Override
  public Boolean visit(ReturnOp returnOp, SymbolTable arg) {
    return true;
  }

  @Override
  public Boolean visit(WhileOp whileOp, SymbolTable arg) {
    Boolean exprSafe = whileOp.getExpr().accept(this, arg);
    return exprSafe && whileOp.getBodyOp().accept(this, arg);
  }

  @Override
  public Boolean visit(WriteOp writeOp, SymbolTable arg) {
    //writeOp.getExpr().accept(this, arg);
    return null;
  }

  @Override
  public Boolean visit(Id id, SymbolTable arg) {
    Optional<SymbolTableRecord> tableRecord = arg.lookup(id.getValue());
    if(tableRecord.isEmpty()){
      new ErrorHandler("Variabile "+id.getValue()+" non dichiarata ");
      return false;
    }else return true;
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
    Boolean lfSafe = ltOp.getLeftValue().accept(this, arg);
    Boolean rgSafe = ltOp.getRightValue().accept(this, arg);
    return lfSafe && rgSafe;
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

  private NodeKind findKind(ParDeclOp parDeclOp){
    switch (parDeclOp.getKind()){
      case "IN": return NodeKind.VARIABLE_IN;
      case "OUT": return NodeKind.VARIABLE_OUT;
      default:return NodeKind.VARIABLE;
    }
  }

  private String inference(Expr expr){
    if(expr instanceof IntegerConst){
      return "integer";
    }
    if(expr instanceof RealConst){
      return "real";
    }
    if(expr instanceof StringConst){
      return "string";
    }
    if(expr instanceof TrueConst || expr instanceof FalseConst){
      return "bool";
    }
    return "error type";
  }

  private Boolean saveFunName(FunOp funOp, SymbolTable arg){
    String lexema = funOp.getId().getValue();
    if(!arg.probe(lexema)) {
      NodeType returnType;
      if (funOp.getType() != null) {
        returnType = new PrimitiveNodeType(funOp.getType().getValue());
      } else {
        returnType = new PrimitiveNodeType("void");
      }
      CompositeNodeType compositeNodeType;
      if (funOp.getParDeclOp() != null) {
        compositeNodeType = createComposite(funOp.getParDeclOp());
      } else {
        compositeNodeType = new CompositeNodeType(new ArrayList<NodeType>());
      }
      FunctionNodeType functionNodeType = new FunctionNodeType(compositeNodeType, returnType);
      arg.addEntry(lexema, new SymbolTableRecord(lexema, functionNodeType, NodeKind.FUNCTION));
      return true;
    }else{
      System.err.println("FunOp error");
      return false;
    }
  }
}