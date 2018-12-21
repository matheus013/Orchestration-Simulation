/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author cais
 */



public class Expr {
    private int tipo;
    private int op;
    private String lex;
    
    public Expr(String lex){
        this.lex = lex;
    }
    public Expr(int tipo){
        this.tipo = tipo;
    }
    public Expr(int tipo, String lex){
        this.tipo = tipo;
        this.lex = lex;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public String getLex() {
        return lex;
    }

    public void setLex(String lex) {
        this.lex = lex;
    }
}
