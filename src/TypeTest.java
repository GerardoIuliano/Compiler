import lexical.ArrayStringTable;
import lexical.StringTable;
import nodetype.NodeType;
import org.w3c.dom.Document;
import semantic.StackSymbolTable;
import syntax.Program;
import syntax.template.XMLTemplate;
import visitor.ConcreteVisitor;
import visitor.ScopeCheckerVisitor;
import visitor.TypeCheckerVisitor;

public class TypeTest {
  static Lexer lexer;
  static Parser parser;


  public static void main(String[] args) throws Exception {
    StringTable table = new ArrayStringTable();
    lexer = new Lexer(table);
    StackSymbolTable symbolTable = new StackSymbolTable(table);

    if(lexer.initialize(args[0])) {
      parser = new Parser(lexer);

      Program program = (Program) parser.parse().value;
      ScopeCheckerVisitor scopeCheckerVisitor = new ScopeCheckerVisitor();
      TypeCheckerVisitor typeCheckerVisitor = new TypeCheckerVisitor();
      program.accept(scopeCheckerVisitor, symbolTable);
      System.out.println(symbolTable.toString());
      symbolTable.resetLevel();
      NodeType typeCheck = program.accept(typeCheckerVisitor, symbolTable);
      System.out.println("TypeCheck: "+typeCheck.toString());


    } else {
      System.out.println("File not found");
    }
  }
}