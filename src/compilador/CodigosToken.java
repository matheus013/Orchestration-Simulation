/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.Arrays;
import java.util.List;
import scanner.ER;

/**
 *
 * @author uira
 */
public class CodigosToken {
    public static final int EOF = -1;
    public static final int MAIN = 0;
    public static final int IF = 1;
    public static final int ELSE = 2;
    public static final int WHILE = 3;
    public static final int DO = 4;
    public static final int FOR = 5;
    public static final int INT = 6;
    public static final int FLOAT = 7;
    public static final int CHAR = 8;
    public static final int MENOR = 9;
    public static final int MAIOR = 10;
    public static final int MENOR_IGUAL = 11;
    public static final int MAIOR_IGUAL = 12;
    public static final int IGUAL = 13;
    public static final int DIFERENTE = 14;
    public static final int SOMA = 15;
    public static final int SUBTRACAO = 16;
    public static final int MULTIPLICACAO = 17;
    public static final int DIVISAO = 18;
    public static final int ATRIBUICAO = 19;
    public static final int FECHA_PARENTESES = 20;
    public static final int ABRE_PARENTESES = 21;
    public static final int ABRE_CHAVES = 22;
    public static final int FECHA_CHAVES = 23;
    public static final int VIRGULA = 24;
    public static final int PONTO_VIRGULA = 25;
    public static final int ID = 50;
    public static final int VALOR_INTEIRO = 51;
    public static final int VALOR_REAL = 52;
    public static final int VALOR_CHAR = 53;
    
    CodigosToken nomesTokens;
    
    private static final List<String> TABELA = Arrays.asList(
        "main",
        "if",
        "else",
        "while",
        "do",
        "for",
        "int",
        "float",
        "char",
        "<",
        ">",
        "<=",
        ">=",
        "==",
        "!=",
        "+",
        "-",
        "*",
        "/",
        "=",
        ")",
        "(",
        "{",
        "}",
        ",",
        ";"
    );

    public static int lookUp(String lexema){
        if(TABELA.contains(lexema)){
            return TABELA.indexOf(lexema);
        } else if(ER.ehIdentificador(lexema)){
            return 50;
        } else if(ER.ehInteiro(lexema)){
            return 51;
        } else if(ER.ehFloat(lexema)){
            return 52;
        } else if(ER.ehChar(lexema)){
            return 53;
        }
        else return -1;
    }
    
    public static String getNome(int codigo){
        if(codigo >= 50){
            switch (codigo){
                case 50: return "ID";
                case 51: return "VALOR_INTEIRO";
                case 52: return "VALOR_REAL";
                case 53: return "VALOR_CHAR";
            }
        } else if(codigo >= 0){
            switch(TABELA.get(codigo)){
                case "main":    return "MAIN";
                case "if":      return "IF";
                case "else":    return "ELSE";
                case "while":   return "WHILE";
                case "do":      return "DO";
                case "for":     return "FOR";
                case "int":     return "INT";
                case "float":   return "FLOAT";
                case "char":    return "CHAR";
                case "<":       return "MENOR";
                case ">":       return "MAIOR";
                case "<=":      return "MENOR_IGUAL";
                case ">=":      return "MAIOR_IGUAL";
                case "==":      return "IGUAL";        
                case "!=":      return "DIFERENTE";    
                case "+":       return "SOMA";
                case "-":       return "SUBTRACAO";
                case "*":       return "MULTIPLICACAO";
                case "/":       return "DIVISAO";
                case "=":       return "ATRIBUICAO";
                case ")":       return "FECHA_PARENTESES";
                case "(":       return "ABRE_PARENTESES";
                case "{":       return "ABRE_CHAVES";
                case "}":       return "FECHA_CHAVES";
                case ",":       return "VIRGULA";
                case ";":       return "PONTO_VIRGULA";
                }
        }
        return "EOF";
    }
    
    public static String getLexema(int codigo){
        if(codigo > -1 && codigo < 50){
            return TABELA.get(codigo);
        } else return getNome(codigo);
    }
}