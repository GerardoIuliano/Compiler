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
import java.nio.charset.StandardCharsets;

public class MyFun2C {

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
            compileCprog(cfile.getName());
        } else {
            System.out.println("File not found!");
        }
    }

    //compila ed esegue un file C
    public static void compileCprog(String fileName){
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");

        /*
        try {
            Runtime.getRuntime().exec("cmd.exe /C start");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        ProcessBuilder builder = new ProcessBuilder("gcc", "-o", "main", "main.c");
        try {
            builder.start().waitFor();
            ProcessBuilder run = new ProcessBuilder("./" + "main" );
            Process process = run.start();
            StreamReader streamReader = new StreamReader(process.getInputStream());
            new Thread(streamReader).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
        if (isWindows) {
            //builder.command("cmd.exe", "/c", "wsl", "clang-format", "-style=google", finalFileName.replace("\\", "/"), "-i");
            builder.command("cmd.exe", "/c", "wsl", "./launch.sh", "c_files\\" + fileName.replace("\\", "/"));
        } else {
            //builder.command("sh", "-c", "clang-format", "-style=google", finalFileName.replace("\\", "/"), "-i");
            builder.command("sh", "-c", "./launch.sh", fileName.replace("\\", "/"));
        }

        System.out.println("Command: " + builder.command());
        //builder.directory(new File(workingDir));

        try {
         Process process = builder.start();
         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
         String readline = "";
         int i = 0;

         while ((readline = reader.readLine()) != null) {
            System.out.println(++i + " " + readline);
         }
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    static class StreamReader implements Runnable {
        private InputStream reader;

        public StreamReader(InputStream inStream) {
            reader = inStream;
        }

        @Override
        public void run() {
            byte[] buf = new byte[1024];
            int size = 0;
            try {
                while ((size = reader.read(buf)) != -1) {
                    System.out.println(new String(buf));
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}