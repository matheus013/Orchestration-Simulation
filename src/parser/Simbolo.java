/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import scanner.Token;

/**
 *
 * @author cais
 */
public class Simbolo {
        //tipos possiveis: int:6 / float:7 / char:8
        
    private int tipo;
    private String lexema;
    private int escopo;

    public Simbolo(Token token, int escopo, int tipo) {
        this.escopo = escopo;
        lexema = token.getLexema();
        this.tipo = tipo;
    }

    public int getEscopo() {
        return escopo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getTipo() {
        return tipo;
    }
        
}
