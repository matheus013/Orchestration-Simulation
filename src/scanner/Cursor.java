/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

/**
 *
 * @author cais
 */
public class Cursor {
    
    public int linha;
    private int coluna;
    
    public Cursor(){
        linha = 1;
        coluna = 0;
    }

    public int getLinha() {
        return linha;
    }

    public void addLinha(){
        this.linha++;
        this.coluna = 0;
    }

    public int getColuna() {
        return coluna;
    }

    public void addColuna() {
        this.coluna++;
    }
    
    public void addColuna(int qtd) {
        this.coluna += qtd;
    }
}
