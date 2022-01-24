import lexical.ArrayStringTable;
import lexical.StringTable;
import nodetype.NodeType;
import semantic.StackSymbolTable;
import syntax.Program;
import template.CTemplate;
import visitor.CodeGeneratorVisitor;
import visitor.ScopeCheckerVisitor;
import visitor.TypeCheckerVisitor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TestMyFunToC {

    static Lexer lexer;
    static Parser parser;
    private static String workingDir = System.getProperty("user.dir");

    public static void main(String[] args) throws Exception {
        StringTable stringTable = new ArrayStringTable();
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

            //COMPILE C SOURCE FILE
            compileCprog();
            //RUN FILE .exe
            runCprog();
        } else {
            System.out.println("File not found!");
        }
    }

    //compila ed esegue un file C
    public static void compileCprog() throws IOException, InterruptedException {
        try {
            Files.delete(Path.of("a.exe"));
        } catch (Exception x) {
            System.err.format(x.toString());
        }

        ProcessBuilder builder = new ProcessBuilder();
        final List<String> commandsCompile = new ArrayList<>();

        commandsCompile.add("cmd.exe");
        commandsCompile.add("/C");
        commandsCompile.add("gcc main.c");

        builder.command(commandsCompile);

        builder.directory(new File(workingDir));
        Process process = builder.start();
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        process.waitFor();
    }
    public static void runCprog() throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("cmd.exe /c start ");
        int exitCode = p.waitFor();
        assert exitCode == 0;
        System.exit(0);
    }

}
