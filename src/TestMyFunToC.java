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
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TestMyFunToC {

    static Lexer lexer;
    static Parser parser;

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

            //EXEC C SOURCE FILE
            compileCprog("c_files/main.c");
        } else {
            System.out.println("File not found!");
        }
    }

    //compila ed esegue un file C
    public static void compileCprog(String finalFileName) throws IOException, InterruptedException {

        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        ProcessBuilder builder = new ProcessBuilder();
        String workingDir = System.getProperty("user.dir");

        if (isWindows) {
            //builder.command("cmd.exe", "/c", "wsl", "clang-format", "-style=google", finalFileName.replace("\\", "/"), "-i");
            builder.command("cmd.exe", "/c", "wsl", "./launch.sh", finalFileName);
        } else {
            //builder.command("sh", "-c", "clang-format", "-style=google", finalFileName.replace("\\", "/"), "-i");
            builder.command("sh", "-c", "./launch.sh", finalFileName);
        }

        builder.directory(new File(workingDir));
        Process process = builder.start();
        StreamReader streamGobbler = new StreamReader(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
        System.exit(0);
    }

    static class StreamReader implements Runnable {
        private InputStream reader;
        private Consumer<String> consumer;

        public StreamReader(InputStream inStream, Consumer<String> consumer) {
            this.reader = inStream;
            this.consumer =consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(reader)).lines()
                    .forEach(consumer);
        }
    }
}
