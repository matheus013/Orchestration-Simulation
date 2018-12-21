package scanner;

public class Token {
    private int codigo;
    private String lexema = null;

    public Token(int codigo, String lexema) {
        this.codigo = codigo;
        this.lexema = lexema;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getLexema() {
        return lexema;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }
}
