/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import compilador.CodigosToken;
import util.Print;

/**
 *
 * @author cais
 */
public class Gerador {
    private static String TAB = "    ";
    static String gen(Expr expr1, Expr expr2, int op) {
        String output;
        
        String OP = CodigosToken.getLexema(op);
        StringBuilder codigo = new StringBuilder();
        codigo.append(TAB);
        codigo.append(expr1.getLex());
        codigo.append(' ');
        codigo.append(OP);
        codigo.append(' ');
        codigo.append(expr2.getLex());
        
        output = codigo.toString();
        
        Print.show(output, true);
        
        return output;
    }
    
    static void gen(Expr result, Expr arg1, Expr arg2, int op) {
        String OP = CodigosToken.getLexema(op);
        StringBuilder codigo = new StringBuilder();
        codigo.append(TAB);
        codigo.append(result.getLex());
        codigo.append(" = ");        
        codigo.append(arg1.getLex());
        codigo.append(' ');
        codigo.append(OP);
        codigo.append(' ');
        codigo.append(arg2.getLex());
        
        Print.show(codigo.toString(), true);
    }

    static void genIf(String exprRel, String labelEndIf, int op) {
        String OP = CodigosToken.getLexema(op);
        StringBuilder codigo = new StringBuilder();
        codigo.append(TAB);
        codigo.append("if ");
        codigo.append(exprRel);
        codigo.append(' ');
        codigo.append(OP);
        codigo.append(" 0 goto ");
        codigo.append(labelEndIf);
        
        Print.show(codigo.toString(), true);
    }

    static void genLabel(String label) {
        StringBuilder codigo = new StringBuilder();
        codigo.append(label);
        codigo.append(':');
        
        Print.show(codigo.toString(), true);
    }

    static void genGoto(String label) {
        StringBuilder codigo = new StringBuilder();
        codigo.append(TAB);
        codigo.append("goto ");
        codigo.append(label);
        
        Print.show(codigo.toString(), true);
    }
    
}
