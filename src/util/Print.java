package util;

import compilador.CodigosToken;
import scanner.Token;

public class Print {
    public static void show(String texto, boolean quebraLinha){
        if(quebraLinha) System.out.println(texto);
        else System.out.print(texto);
    }
    
    
    public static void show(char caractere, boolean quebraLinha){
        if(!quebraLinha) System.out.println(caractere);
        else  System.out.print(caractere);
    }
    
    public static void printToken(Token token){
        String strCodigo;
        int codigo = token.getCodigo();
        String lexema = token.getLexema();
        if(codigo < 10){
            strCodigo = "0" + codigo;
        } else{
            strCodigo = Integer.toString(codigo);
        }
        
        if(codigo < 0){
            Print.show(strCodigo + " " + CodigosToken.getNome(codigo), true);
        } else if(codigo < 50){
            Print.show(strCodigo + " \"" + lexema + "\"", true);
        } else{
            Print.show(strCodigo + " " + CodigosToken.getNome(codigo) + " \"" + lexema + "\"", true);
        }
        
    }
}