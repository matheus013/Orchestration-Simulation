package util;

import compilador.CodigosToken;
import scanner.Cursor;
import compilador.CodigosToken;
import java.util.List;
import scanner.Token;

public class Erro {
    private static final String[] MENSAGENS = 
    {
        "Nenhum arquivo lido", //0
        "Parametro invalido" //1
    };
    
    public static void show(int codigo){
        Print.show(MENSAGENS[codigo], true);
    }
    
    public static void tokenError(Cursor cursor, Token tok, String detalhes){
        String lex;
        
        if(tok == null){
            Print.show("ERRO na linha " + cursor.getLinha() + ", coluna " + cursor.getColuna() + ", ultimo token lido (nenhum): "+ detalhes, true);
            
        } else{
            lex = CodigosToken.getLexema(tok.getCodigo());
            
            if(tok.getCodigo() >= 50){ 
                Print.show("ERRO na linha " + cursor.getLinha() + ", coluna " + cursor.getColuna() + ", ultimo token lido "+ lex + " \"" + tok.getLexema() + "\": " + detalhes, true);
            } else{
                Print.show("ERRO na linha " + cursor.getLinha() + ", coluna " + cursor.getColuna() + ", ultimo token lido "+ lex +": "+ detalhes, true);
            }
        }
        System.exit(0);
        
    }

    public static void sintaxError(List<Integer> cod, Token token, boolean eof, Cursor cursor) {
        String msg1 = "";
        String msg2 = "";
   
        msg2 = "Token esperado: " + CodigosToken.getLexema(cod.get(0));
        for(int i = 1; i < cod.size(); i++){
            msg2 += " ou " + CodigosToken.getLexema(cod.get(i));
        }
        
        tokenError(cursor, token, msg1 + msg2);
    }
    
    public static void sintaxError(int cod, Token token, boolean eof, Cursor cursor) {
        String msg1 = "";
        String msg2 = "";

        msg2 = "Token esperado: " + CodigosToken.getLexema(cod);

        tokenError(cursor, token, msg1 + msg2);
    }

    public static void semanticError(int tipo1, int tipo2, int codErro, Token token, Cursor cursor) {
        String msg, lex1, lex2;
        lex1 = CodigosToken.getLexema(tipo1);
        lex2 = CodigosToken.getLexema(tipo2);
        switch(codErro){
            case 0: msg = "Identificador \"" + token.getLexema() + "\" repetido no mesmo escopo."; break;
            case 1: msg = "Variavel \"" + token.getLexema() + "\" nao declarada."; break;
            case 2: msg = "Atribuicao de tipos incompativeis. Encontrado "+ lex1 + " <- " + lex2 + "."; break;
            case 3: msg = "Tipo \"char\" nao opera com outros tipos. Encontrado "+ lex1 + " <-> " + lex2 + "."; break;
            default: msg = ""; break;
        }
        
        tokenError(cursor, token, msg);
    }


}
