import lexical.ArrayStringTable;
import lexical.StringTable;
import nodetype.NodeType;
import org.w3c.dom.Document;
import semantic.StackSymbolTable;
import syntax.Program;
import syntax.template.XMLTemplate;
import template.CTemplate;
import visitor.CodeGeneratorVisitor;
import visitor.ConcreteVisitor;
import visitor.ScopeCheckerVisitor;
import visitor.TypeCheckerVisitor;

import java.io.File;

public class GenerateCodeCTest {

    static Lexer lexer;
    static Parser parser;

    public static void main(String[] args) throws Exception {
        StringTable stringTable= new ArrayStringTable();
        lexer = new Lexer(stringTable);
        StackSymbolTable symbolTable = new StackSymbolTable(stringTable);

        if(lexer.initialize(args[0])) {
            parser = new Parser(lexer);

            Program program = (Program) parser.parse().value;
            //VISITORS
            ScopeCheckerVisitor scopeCheckerVisitor = new ScopeCheckerVisitor();
            TypeCheckerVisitor typeCheckerVisitor = new TypeCheckerVisitor();
            CodeGeneratorVisitor codeGeneratorVisitor = new CodeGeneratorVisitor();
            //SCOPE CHECK
            program.accept(scopeCheckerVisitor, symbolTable);
            System.out.println(symbolTable.toString());
            symbolTable.resetLevel();
            //TYPE CHECK
            NodeType typeCheck = program.accept(typeCheckerVisitor, symbolTable);
            System.out.println("TypeCheck: "+typeCheck.toString());
            symbolTable.resetLevel();
            //GENERATE C CODE
            CTemplate cTemplate = new CTemplate();
            File cfile = cTemplate.create("main").get();
            String root = program.accept(codeGeneratorVisitor, symbolTable);
            cTemplate.write(cfile, root);
        } else {
            System.out.println("File not found!");
        }
    }
}
