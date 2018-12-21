
package scanner;

public class ER {
    
    static String L = "[a-z]";
    static String D = "[0-9]";
    static String ID = "(_|"+L+")(_|"+L+"|"+D+")*";
    static String INTEIRO = D+"+";
    static String FLOAT = D+"*."+D+"+";
    static String CHAR = "('"+L+"'|'"+D+"')";
    
    public static boolean ehLetra(String ch) {
        if(ch.matches(L)){
            //Print.show("É letra");
            return true;
        } else{
            //Print.show("Não é letra");
            return false;
        }
    }
    
    public static boolean ehDigito(String ch) {
        if(ch.matches(D)){
            //Print.show("É número"); 
            return true;
        } else{
            //Print.show("Não é número");
            return false;
        }
        
    }
    
    public static boolean ehChar(String lexema){
        if(lexema.matches(CHAR)){
            return true;
        }else{
            return false;                
        }
    }

    public static boolean ehIdentificador(String lexema) {
        if(lexema.matches(ID)){
            return true;
        }else{
            return false;                
        }
    }

    public static boolean ehInteiro(String lexema) {
        if(lexema.matches(INTEIRO)){
            return true;
        }else{
            return false;                
        }
    }

    public static boolean ehFloat(String lexema) {
        if(lexema.matches(FLOAT)){
            return true;
        }else{
            return false;                
        }
    }
    
    public static boolean ehSimboloSimples(String ch){
        char c = ch.charAt(0);
        if(
            c == '(' ||
            c == ')' ||
            c == '{' ||
            c == '}' ||
            c == ',' ||
            c == ';' ||
            c == '+' ||
            c == '-' ||
            c == '*'
        )
            return true;
        else
            return false;
    }
}
