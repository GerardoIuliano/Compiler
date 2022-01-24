# Quinta esercitazione:  Modulo Analisi semantica e generazione codice C
Iuliano Gerardo - Covino Francesco Pio

Linguaggio di programmazione definito per il corso di Compilatori anno 2021/2022.
Il compilatore integra l'utilizzo di JFlex e JavaCup per l'implementazione del Lexer (per l'analisi lessicale)
e del Parser (per l'analisi sintattica).

La Classe MyFun2C mette insieme i linguaggi MyFun e C: si tratta di un compilatore completo che prende in input un codice MyFun, dopo aver svolto l'analisi
lessicale, sintattica e semantica lo compila in un programma C. Completata la compilazione viene aperto un terminale da cui l'utente dovrà avviare l'applicazione
digitando "a.exe" e premendo invio. Ciò consente di poter interagire dinamicamente con il programma.

## Specifiche lessicali

| Token | Lexeme |
|-------|--------|
| FUN | "fun" |
| MAIN | "main" |
| LPAR | "(" |
| RPAR | ")" |
| END | "end" |
| ID | Fun id format |
| COLON | ":" |
| SEMI | ";" |
| COMMA | "," |
| NULL | "null" |
| VAR | "var" |
| OUT | "out" |
| OUTPAR | "@" |
| INTEGER | "integer" |
| BOOL | "bool" |
| REAL | "real" |
| STRING | "string" |
| BLPAR | "{" |
| BRPAR | "}" |
| ASSIGN | ":=" |
| WHILE | "while" |
| LOOP | "loop" |
| DO | "do" |
| IF | "if" |
| THEN | "then" |
| ELSE | "else" |
| READ | "%" |
| WRITE | "?" |
| WRITELN | "?." |
| WRITEB | "?," |
| WRITET | "?:" |
| RETURN | "return" |
| TRUE | "true" |
| FALSE | "false" |
| STR_CONCAT | "&" |
| PLUS | "+" |
| MINUS | "-" |
| TIMES | "*" |
| DIV | "/" |
| DIVINT | "div" |
| POW | "^" |
| AND | "and" |
| OR | "or" |
| GT | ">" | 
| GE | ">=" |
| LT | "<" |
| LE | "<=" |
| EQ | "=" |
| NE | "!=" |
| NE | "<>" |
| NOT | "not" |

## Specifiche Sintattiche: Grammatiche

```

Program ::= VarDeclList FunList Main:;

        VarDeclList ::= /* empty */
        | VarDecl VarDeclList
        ;

        Main ::= MAIN VarDeclList StatList END MAIN SEMI
        ;

        FunList ::= /* empty */
        | Fun FunList
        ;

        VarDecl ::= Type IdListInit SEMI
        | VAR IdListInitObbl SEMI
        ;

        Type ::= INTEGER
        | BOOL
        | REAL
        | STRING
        ;

        IdListInit ::= ID
        | IdListInit COMMA ID
        | ID ASSIGN Expr
        | IdListInit COMMA ID ASSIGN Expr
        ;

        IdListInitObbl ::= ID ASSIGN Const
        | IdListInitObbl COMMA ID ASSIGN Const
        ;

        Const ::= INTEGER_CONST
        | REAL_CONST
        | TRUE
        | FALSE
        | STRING_CONST
        ;

        Fun ::= FUN ID LPAR ParamDeclList RPAR COLON Type
        VarDeclList StatList END FUN SEMI
        | FUN ID LPAR ParamDeclList RPAR
        VarDeclList StatList END FUN SEMI
        ;

        ParamDeclList ::= /*empty */
        | NonEmptyParamDeclList
        ;

        NonEmptyParamDeclList ::= ParDecl
        | NonEmptyParamDeclList COMMA ParDecl
        ;

        ParDecl ::= Type ID
        | OUT Type ID
        ;

        StatList ::= Stat
        | Stat StatList
        ;

        Stat ::= IfStat SEMI
        | WhileStat SEMI
        | ReadStat SEMI
        | WriteStat SEMI
        | AssignStat SEMI
        | CallFun SEMI
        | RETURN Expr SEMI
        | /* empty */
        ;

        IfStat ::= IF Expr THEN VarDeclList StatList Else END IF
        ;

        Else ::= /* empty */
        | ELSE VarDeclList  StatList
        ;

        WhileStat ::= WHILE Expr LOOP VarDeclList StatList END LOOP
        ;

        ReadStat ::= READ IdList Expr
        | READ IdList
        ;

        IdList ::= ID
        | IdList COMMA ID
        ;

        WriteStat ::=  WRITE  Expr
        | WRITELN  Expr
        | WRITET  Expr
        | WRITEB  Expr
        ;

        AssignStat ::=  ID ASSIGN Expr
        ;

        CallFun ::= ID LPAR ExprList RPAR
        | ID LPAR RPAR
        ;

        ExprList ::= Expr
        | Expr COMMA ExprList
        | OUTPAR ID
        | OUTPAR ID COMMA ExprList
        ;

        Expr ::= TRUE
        | FALSE
        | INTEGER_CONST
        | REAL_CONST
        | STRING_CONST
        | ID
        | CallFun
        | Expr PLUS Expr
        | Expr MINUS Expr
        | Expr TIMES Expr
        | Expr DIV Expr
        | Expr DIVINT Expr
        | Expr AND Expr
        | Expr POW Expr
        | Expr STR_CONCAT Expr
        | Expr OR Expr
        | Expr GT Expr
        | Expr GE Expr
        | Expr LT Expr
        | Expr LE Expr
        | Expr EQ Expr
        | Expr  NE Expr
        | MINUS Expr
        %prec UMINUS
        | NOT Expr
        | LPAR Expr RPAR
        ;
```
### Regole di inferenza
Sono presenti nel file inference_rules.pdf nella repository.
