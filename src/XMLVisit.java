import lexical.ArrayStringTable;
import lexical.StringTable;
import org.w3c.dom.Document;
import syntax.Program;
import syntax.template.XMLTemplate;
import visitor.ConcreteVisitor;

public class XMLVisit {
    static Lexer lexer;
    static Parser parser;

    public static void main(String[] args) throws Exception {
        StringTable table = new ArrayStringTable();
        lexer = new Lexer(table);

        if(lexer.initialize(args[0])) {
            System.out.println(args[0]);
            parser = new Parser(lexer);

            Program program = (Program) parser.parse().value;
            XMLTemplate xmlTemplate = new XMLTemplate();
            Document xmlDocument = xmlTemplate.create().get();
            ConcreteVisitor xmlVisitor = new ConcreteVisitor();

            program.accept(xmlVisitor, xmlDocument);
            xmlTemplate.write("ast" + ".xml", xmlDocument);
            System.out.println("String Table\n"+table.toString());
        } else {
          System.out.println("File not found");
        }
    }
}
