package parser;

import compilador.CodigosToken;
import java.util.List;
import java.util.Stack;
import scanner.Token;
import scanner.Tokenizer;
import util.Erro;
import util.Print;

public class Parser {
    private Token token;
    private Tokenizer scanner;
    private int escopo = -1;
    private int labelNum = 0;
    private int tNum = 0;
    private Stack<Simbolo> tabelaSimbolos = new Stack();
    
    public Parser(Tokenizer scanner){
        this.scanner = scanner;
    }
    
    public void run(){
//        while(!scanner.eof()){
//            scan();
//        }
        scan();
        if(!scanner.eof()){
            programa();
            if(!scanner.eof()){
                parserError(CodigosToken.EOF);
            }
        }else parserError(First.programa);
    }
    
    private void programa(){
        //int main"("")" <bloco>
        
        if(token.getCodigo() == CodigosToken.INT){
            scan();
            if(token.getCodigo() == CodigosToken.MAIN){
                scan();
                if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
                    scan();
                    if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                        scan();
                        bloco();
                    }else parserError(CodigosToken.FECHA_PARENTESES);
                }else parserError(CodigosToken.ABRE_PARENTESES);
            } else parserError(CodigosToken.MAIN);
        } else parserError(CodigosToken.INT);
    }
    
    private void bloco() {
        //“{“ {<decl_var>}* {<comando>}* “}”
        escopo++;
        if(token.getCodigo() == CodigosToken.ABRE_CHAVES){
            scan();
            while(First.decl_var.contains(token.getCodigo())){
                decl_var();
            }
            while(First.comando.contains(token.getCodigo())){
                comando();
            }
            if(token.getCodigo() == CodigosToken.FECHA_CHAVES){
                scan();
            } else parserError(CodigosToken.FECHA_CHAVES);
        } else parserError(CodigosToken.ABRE_CHAVES);
        limparSimbolos(escopo);
        escopo--;
    }
    
    private void decl_var() {
        //<tipo> <id> {,<id>}* ";"
        
        int tipo = tipo();
        if(token.getCodigo() == CodigosToken.ID){
            novoSimbolo(token, escopo, tipo);
            scan();
            while(token.getCodigo() == CodigosToken.VIRGULA){
                scan();
                if(token.getCodigo() == CodigosToken.ID){
                    novoSimbolo(token, escopo, tipo);
                    scan();
                } else parserError(CodigosToken.ID);
            }
            if(token.getCodigo() == CodigosToken.PONTO_VIRGULA){
                scan();
            }else parserError(CodigosToken.PONTO_VIRGULA); 
        }else parserError(CodigosToken.ID); 
    }
    
    
    private int tipo(){
        int tipo = token.getCodigo();
        if(First.tipo.contains(token.getCodigo())){
            scan();
            return tipo;
        } else parserError(First.tipo);
        return -10;
    }    

    private void comando() {
        //    <comando_básico>
        //    <iteração>
        //    if "("<expr_relacional>")" <comando> {else <comando>}?
        
        if(First.comando_basico.contains(token.getCodigo())){ // comando_basico
            comando_basico();
        } else if(First.iteracao.contains(token.getCodigo())){ // iteracao
            iteracao();
        } else if(First._if.contains(token.getCodigo())){ // if
            _if();
        } else parserError(First.comando);
    }
    
    private void _if(){
        // if "("<expr_relacional>")" <comando> {else <comando>}?
        String exprRel;
        String labelElse = newLabel();
        String labelEndIf = newLabel();
        if(token.getCodigo() == CodigosToken.IF){
            scan();
            if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
                scan();
                exprRel = expr_relacional();
                if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                    Gerador.genIf(exprRel, labelElse, CodigosToken.IGUAL);
                    scan();
                    comando();
                    
                    //else opcional
                    if(token.getCodigo() == CodigosToken.ELSE){
                        Gerador.genGoto(labelEndIf);
                        Gerador.genLabel(labelElse);
                        scan();
                        comando();
                        Gerador.genLabel(labelEndIf);
                    } else{
                        Gerador.genLabel(labelElse);
                    }
                }else parserError(CodigosToken.FECHA_PARENTESES);
            }else parserError(CodigosToken.ABRE_PARENTESES);
        } else parserError(First._if);
    }
    
    private void iteracao(){
        // while "("<expr_relacional>")" <comando>
        // do <comando> while "("<expr_relacional>")"";"

        if(token.getCodigo() == CodigosToken.WHILE){
            _while();
            
        }else if(token.getCodigo() == CodigosToken.DO){
            _do();
        } else parserError(First.iteracao);
    }
    
    private void _while(){
        //while
        
        String labelInicio, labelFim, exprRel;
        labelInicio = newLabel();
        labelFim = newLabel();
 
        scan();
        if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            Gerador.genLabel(labelInicio);
            scan();
            exprRel = expr_relacional();
            Gerador.genIf(exprRel, labelFim, CodigosToken.IGUAL);
            
            if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                scan();
                comando();
            } else parserError(CodigosToken.FECHA_PARENTESES);
            
            Gerador.genGoto(labelInicio);
            Gerador.genLabel(labelFim);
            
        } else parserError(CodigosToken.ABRE_PARENTESES);
    }
    
    private void _do(){
        //do
        
        String labelInicio, exprRel;
        labelInicio = newLabel();
        
        Gerador.genLabel(labelInicio);
        scan();
        comando();
        if(token.getCodigo() == CodigosToken.WHILE){
            scan();
            if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
                scan();
                exprRel = expr_relacional();
                Gerador.genIf(exprRel, labelInicio, CodigosToken.DIFERENTE);
                if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                    scan();
                    if(token.getCodigo() == CodigosToken.PONTO_VIRGULA){
                        scan();
                    } else parserError(CodigosToken.PONTO_VIRGULA);
                } else parserError(CodigosToken.FECHA_PARENTESES);
            } else parserError(CodigosToken.ABRE_PARENTESES);
        }else parserError(CodigosToken.WHILE);
    }
    
    private String expr_relacional(){
        //<expr_arit> <op_relacional> <expr_arit>
        int op;
        Expr T, expr1, expr2;
        
        expr1 = expr_arit();
        op = op_relacional();
        expr2 = expr_arit();
        
        checarTipoExprRelacional(expr1.getTipo(), expr2.getTipo());
        
        //coercao de tipos
        if(expr1.getTipo() == CodigosToken.INT && expr2.getTipo() == CodigosToken.FLOAT){
            expr1.setLex("(float) " + expr1.getLex());
        } else if(expr1.getTipo() == CodigosToken.FLOAT && expr2.getTipo() == CodigosToken.INT){
            expr2.setLex("(float) " + expr2.getLex());
        }
        
        T = new Expr(newT());
        Gerador.gen(T, expr1, expr2, op);
        
        return T.getLex();
    }
    
    private void checarTipoExprRelacional(int tipo1, int tipo2){
        if(tipo1 != tipo2 && (tipo1 == CodigosToken.CHAR || tipo2 == CodigosToken.CHAR)){
            semanticError(tipo1, tipo2, 3);
        }
    }
    
    private int op_relacional(){
        // "==" | "!=" | "<" | ">" | "<=" | ">="
        int op = -10;
        if(First.op_relacional.contains(token.getCodigo())){
            op = token.getCodigo();
            scan();
        } else parserError(First.op_relacional);
        return op;
    }
    
    private Expr termo(){
        //<fator> ((* | /) <fator>)*
        Expr expr1, expr2;
        int op, tipoFinal;
        
        Expr T;
        
        expr1 = fator();
        while(token.getCodigo() == CodigosToken.MULTIPLICACAO || token.getCodigo() == CodigosToken.DIVISAO){
            op = token.getCodigo();
            scan();
            expr2 = fator();
            
            tipoFinal = checarTiposTermo(expr1, expr2, op);
            
            //coercao de tipos
            if(expr1.getTipo() == CodigosToken.INT && expr2.getTipo() == CodigosToken.FLOAT){
                expr1.setLex("(float) " + expr1.getLex());
            } else if(expr1.getTipo() == CodigosToken.FLOAT && expr2.getTipo() == CodigosToken.INT){
                expr2.setLex("(float) " + expr2.getLex());
            }
            
            T = new Expr(tipoFinal, newT());
            Gerador.gen(T, expr1, expr2, op);
            expr1 = T;
        }
        return expr1;
    }
    
    private int checarTiposTermo(Expr expr1, Expr expr2, int op){
        int tipo1, tipo2;
        if(expr2 != null){
            tipo1 = expr1.getTipo();
            tipo2 = expr2.getTipo();
            
            if(tipo1 == tipo2 && tipo1 == CodigosToken.CHAR){
                return expr1.getTipo();
            } else if(tipo1 == CodigosToken.CHAR || tipo2 == CodigosToken.CHAR){
                semanticError(tipo1, tipo2, 3); //erro de char operando com outros tipos
            }else if(op == CodigosToken.DIVISAO){
                return CodigosToken.FLOAT;
            } else if(tipo1 == CodigosToken.FLOAT || tipo2 == CodigosToken.FLOAT){
                return CodigosToken.FLOAT;
            }
            return CodigosToken.INT;
        } else{
            return expr1.getTipo();
        }
    }
    
    private Expr fator(){
        //“(“ <expr_arit> “)”
        //<id>
        //<real>
        //<inteiro>
        //<char>
        Expr expr;
        int tipo = -10;
        String lex = null;
        Simbolo tempSimbolo;
        
        if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){    
            scan();
            expr = expr_arit();
            if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                scan();
            }else parserError(CodigosToken.FECHA_PARENTESES);
            return expr;
        } else{
            lex = token.getLexema();
            expr = new Expr(lex);
            if(token.getCodigo() == CodigosToken.ID){

                tempSimbolo = buscaSimbolo(token.getLexema(), -1);
                if(tempSimbolo != null){
                    tipo = tempSimbolo.getTipo();
                } else semanticError(token.getCodigo(), -10, 1); //variavel nao declarada
                scan();

            } else if(token.getCodigo() == CodigosToken.VALOR_REAL){
                tipo = CodigosToken.FLOAT;
                scan();
            }else if(token.getCodigo() == CodigosToken.VALOR_INTEIRO){
                tipo = CodigosToken.INT;
                scan();
            }else if(token.getCodigo() == CodigosToken.VALOR_CHAR){
                tipo = CodigosToken.CHAR;
                scan();
            }else parserError(First.fator);
        }
        
        expr.setTipo(tipo);
        return expr;
    }
    
    private Expr expr_arit(){
        //<termo> <expr_arit2>
        Expr expr1, expr2, T;
        int tipoFinal;
        
        expr1 = termo();
        expr2 = expr_arit2();
        
        
        if(expr2 != null){
            tipoFinal = checarTiposTermo(expr1, expr2, expr2.getOp());
            //coercao de tipos
            if(expr1.getTipo() == CodigosToken.INT && expr2.getTipo() == CodigosToken.FLOAT){
                expr1.setLex("(float) " + expr1.getLex());
            } else if(expr1.getTipo() == CodigosToken.FLOAT && expr2.getTipo() == CodigosToken.INT){
                expr2.setLex("(float) " + expr2.getLex());
            }
            
            T = new Expr(tipoFinal, newT());
            T.setOp(expr2.getOp());
            Gerador.gen(T, expr1, expr2, T.getOp());
            
        } else{
            return expr1;
        }

        return T;
    }
    
    private Expr expr_arit2(){
        //"+" <termo> <expr_arit2>
        //"-" <termo> <expr_arit2>
        //λ
        Expr expr1, expr2, T;
        int op, tipoFinal;
        
        if(token.getCodigo() == CodigosToken.SOMA || token.getCodigo() == CodigosToken.SUBTRACAO){
            op = token.getCodigo();
            scan();
            expr1 = termo();
            expr2 = expr_arit2();
        } else{
            //λ
            return null;
        }
        tipoFinal = checarTiposTermo(expr1, expr2, op);
        
        if(expr2 != null){
            //coercao de tipos
            if(expr1.getTipo() == CodigosToken.INT && expr2.getTipo() == CodigosToken.FLOAT){
                expr1.setLex("(float) " + expr1.getLex());
            } else if(expr1.getTipo() == CodigosToken.FLOAT && expr2.getTipo() == CodigosToken.INT){
                expr2.setLex("(float) " + expr2.getLex());
            }
            
            T = new Expr(tipoFinal, newT());
            T.setOp(op);
            Gerador.gen(T, expr1, expr2, op);
            
        } else{
            expr1.setOp(op);
            return expr1;
        }

        return T;
    }
    

    private void comando_basico(){
        //    <atribuição>
        //    <bloco>       
        
        if(First.atribuicao.contains(token.getCodigo())){ // atribuicao
            atribuicao();
        }else if(First.bloco.contains(token.getCodigo())){ //bloco
            bloco();
        } else parserError(First.comando_basico);
    }
    
    private void atribuicao(){
        //<id> "=" <expr_arit> ";"
        Expr expr1 = null, expr2;
        Simbolo tempSimbolo;
        if(token.getCodigo() == CodigosToken.ID){
            
            tempSimbolo = buscaSimbolo(token.getLexema(), -1);
            if(tempSimbolo != null){
                expr1 = new Expr(tempSimbolo.getTipo(), tempSimbolo.getLexema());
            } else semanticError(token.getCodigo(), -10, 1); //variavel nao declarada
            
            scan();
            if(token.getCodigo() == CodigosToken.ATRIBUICAO){
                scan();
                expr2 = expr_arit();
                
                checarTipoAtribuicao(expr1.getTipo(), expr2.getTipo());
                
                //coercao de tipos
                if(expr1.getTipo() == CodigosToken.FLOAT && expr2.getTipo() == CodigosToken.INT){
                    expr2.setLex("(float) " + expr2.getLex());
                }
                
                Gerador.gen(expr1, expr2, CodigosToken.ATRIBUICAO);
                
                if(token.getCodigo() == CodigosToken.PONTO_VIRGULA){
                    scan();
                } else parserError(CodigosToken.PONTO_VIRGULA);
            } else parserError(CodigosToken.ATRIBUICAO);
        } else parserError(CodigosToken.ID);
    }
    
    private void checarTipoAtribuicao(int tipo1, int tipo2){
        if(tipo1 != tipo2){
            if(!(tipo1 == CodigosToken.FLOAT && tipo2 == CodigosToken.INT)){
                semanticError(tipo1, tipo2, 2);
            }
        }
    }

    private void scan() {
        token = scanner.scan();
        //if(token != null) Print.printToken(token);
    }
    /*
    private void gerarCodigo3End(String resultado, String fonte1, String fonte2, String op){
        String linha = "";
        if(resultado != null){
            linha += resultado;
        }
        linha += " ";
        if(op != null){
            linha += op;
        }
        linha += " ";
        if(fonte1 != null){
            linha += fonte1;
        }
        linha += " ";
        if(fonte2 != null){
            linha += fonte2;
        }
        Print.show(linha, true);
    }
*/
    private void parserError(int codigoToken) {
        Erro.sintaxError(codigoToken, scanner.getUltimoTokenValido(), scanner.eof(), scanner.getCursor());
    }
    private void parserError(List<Integer> codigoToken) {
        Erro.sintaxError(codigoToken, scanner.getUltimoTokenValido(), scanner.eof(), scanner.getCursor());
    }
    private void semanticError(int tipo1, int tipo2, int codErro) {
        Erro.semanticError(tipo1, tipo2, codErro, scanner.getUltimoTokenValido(), scanner.getCursor());
    }

    private void novoSimbolo(Token token, int escopo, int tipo) {
        Simbolo novo = new Simbolo(token, escopo, tipo);
        
        if(buscaSimbolo(novo.getLexema(), novo.getEscopo()) == null){
            tabelaSimbolos.push(novo);
        }else semanticError(novo.getTipo(), -10, 0);
    }

    private Simbolo buscaSimbolo(String lexema, int escopo) {
        Simbolo temp;
        for(int i = tabelaSimbolos.size() - 1; i >= 0; i--){
            temp = tabelaSimbolos.get(i);
            if(temp.getLexema().equals(lexema)){
                if(escopo == -1){
                    return temp;
                } else if(temp.getEscopo() == escopo){
                    return temp;
                }
            }
        }
        return null;
    }
    
    private void limparSimbolos(int escopoAtual){
        Simbolo temp;
        if(!tabelaSimbolos.isEmpty()){
            do{
                temp = tabelaSimbolos.peek();
                if(temp.getEscopo() == escopoAtual){
                    tabelaSimbolos.pop();
                }
            }while(!tabelaSimbolos.isEmpty() && temp.getEscopo() == escopoAtual);
        }
    }

    private String newLabel() {
        return "L" + labelNum++;
    }
    private String newT() {
        return "T" + tNum++;
    }


}

