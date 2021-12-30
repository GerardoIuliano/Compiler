package nodetype;

import java.util.ArrayList;
import java.util.List;

public class TypeTable {

  private List<TypeTableRecord> typeTable;

  private static NodeType integer = new PrimitiveNodeType("integer");
  private static NodeType real = new PrimitiveNodeType("real");
  private static NodeType string = new PrimitiveNodeType("string");
  private static NodeType bool = new PrimitiveNodeType("bool");


  public TypeTable() {
    this.typeTable = new ArrayList<TypeTableRecord>();

    /* ARITH_OP => ADD,DIFF,DIV,MUL,POW */
    this.typeTable.add(new TypeTableRecord("ARITH",integer,integer,integer));
    this.typeTable.add(new TypeTableRecord("ARITH",integer,real,real));
    this.typeTable.add(new TypeTableRecord("ARITH",real,integer,real));
    this.typeTable.add(new TypeTableRecord("ARITH",real,real,real));

    this.typeTable.add(new TypeTableRecord("DIVINT",integer,integer,integer));
    this.typeTable.add(new TypeTableRecord("DIVINT",integer,real,integer));
    this.typeTable.add(new TypeTableRecord("DIVINT",real,integer,integer));
    this.typeTable.add(new TypeTableRecord("DIVINT",real,real,integer));

    this.typeTable.add(new TypeTableRecord("STRCAT",string,string,string));

    /* REL_OP => EQ,LT,LE,GT,GE */
    this.typeTable.add(new TypeTableRecord("REL",integer,integer,bool));
    this.typeTable.add(new TypeTableRecord("REL",integer,real,bool));
    this.typeTable.add(new TypeTableRecord("REL",real,integer,bool));
    this.typeTable.add(new TypeTableRecord("REL",real,real,bool));

    this.typeTable.add(new TypeTableRecord("AND",bool,bool,bool));
    this.typeTable.add(new TypeTableRecord("OR",bool,bool,bool));
  }

  public NodeType check(String op, NodeType arg1, NodeType arg2){
    for(TypeTableRecord t : this.typeTable){
      if(t.equals(new TypeTableRecord(op,arg1,arg2,null))){
        return t.getResult();
      }
    }
    return new PrimitiveNodeType("error");
  }



}
