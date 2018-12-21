/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import compilador.CodigosToken;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author uira
 */
public class First {
    
    static List<Integer> fator = Arrays.asList(CodigosToken.ABRE_PARENTESES,
            CodigosToken.ID,
            CodigosToken.VALOR_REAL,
            CodigosToken.VALOR_INTEIRO,
            CodigosToken.VALOR_CHAR
    );
    
    static List<Integer> termo = fator;
    
    static List<Integer> expr_arit = termo;
    
    static List<Integer> expr_relacional = expr_arit;
    
    static List<Integer> atribuicao = Arrays.asList(
            CodigosToken.ID
    );
    
    static List<Integer> iteracao = Arrays.asList(
            CodigosToken.WHILE,
            CodigosToken.DO
    );
    
    static List<Integer> bloco = Arrays.asList(
            CodigosToken.ABRE_CHAVES
    );
    
    
    static List<Integer> _if = Arrays.asList(
            CodigosToken.IF
    );
    
    static List<Integer> comando_basico = uniao(bloco, atribuicao);
    
    static List<Integer> comando = uniao(comando_basico, iteracao, _if);
    
    static List<Integer> tipo = Arrays.asList(
            CodigosToken.INT,
            CodigosToken.FLOAT,
            CodigosToken.CHAR
    );
    
    static List<Integer> decl_var = tipo;

    static List<Integer> programa = Arrays.asList(
            CodigosToken.INT
    );
    
    static List<Integer> op_relacional = Arrays.asList(
            CodigosToken.IGUAL,
            CodigosToken.DIFERENTE,
            CodigosToken.MENOR,
            CodigosToken.MAIOR,
            CodigosToken.MENOR_IGUAL,
            CodigosToken.MAIOR_IGUAL
    );

    
    private static List<Integer> uniao(List<Integer>... conjuntos){
        List<Integer> uniao = new ArrayList();
        uniao.addAll(conjuntos[0]);
        for(List<Integer> conjunto: conjuntos){
            for(Integer valor: conjunto){
                if(!uniao.contains(valor)){
                    uniao.add(valor);
                }
            }
        }
        return uniao;
    }
    private static List<Integer> uniao(Integer... conjunto){
        List<Integer> uniao = new ArrayList();
        for(Integer valor: conjunto){
            uniao.add(valor);
        }
        return uniao;
    }

}
