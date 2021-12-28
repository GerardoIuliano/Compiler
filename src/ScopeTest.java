import lexical.ArrayStringTable;
import lexical.StringTable;
import org.w3c.dom.Document;
import semantic.StackSymbolTable;
import syntax.Program;
import syntax.template.XMLTemplate;
import visitor.ConcreteVisitor;
import visitor.ScopeCheckerVisitor;

public class ScopeTest {
  static Lexer lexer;
  static Parser parser;


  public static void main(String[] args) throws Exception {
    StringTable table = new ArrayStringTable();
    StackSymbolTable symbolTable = new StackSymbolTable(table);
    lexer = new Lexer(table);

    if(lexer.initialize(args[0])) {
      System.out.println(args[0]);
      parser = new Parser(lexer);

      Program program = (Program) parser.parse().value;
      ScopeCheckerVisitor scopeCheckerVisitor = new ScopeCheckerVisitor();
      program.accept(scopeCheckerVisitor,symbolTable);
      System.out.println(symbolTable.toString());
    } else {
      System.out.println("File not found");
    }
  }
}