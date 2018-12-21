package scanner;

import util.Erro;
import compilador.CodigosToken;
import java.util.Scanner;

public class Tokenizer {
    
    private final Scanner leitor;
    private String ch = null;
    private Token ultimoTokenValido = null;
    private Token token = null;
    private int codigo;
    private String lexema;
    private Cursor cursor;
    private boolean EOF = false;
    
    public Tokenizer(Scanner scan){
        leitor = scan;
        leitor.useDelimiter("");
        cursor = new Cursor();
        
        getNextChar(); //ler primeiro char
    }
    
    public Token scan(){
        lexema = "";
        token = null;
        int codErro = 0;
        
        if(EOF){
            //token de fim de arquivo
            return tokenEOF();
        }
        
        while(ehCharBranco(ch)){
            if(ch.charAt(0) == '\n' && leitor.hasNext()) {
                cursor.addLinha();
            }
            getNextChar();
            if(EOF){
                //token de fim de arquivo
                return tokenEOF();
            }
        }
            
        if(ER.ehSimboloSimples(ch)){ // simbolos simples: ( ) { } , ; + - *
                codErro = defineTokenFound(true);
        } else{
            //Automato
            switch(ch.charAt(0)){
                case '<':
                    lerProximoChar();
                    if(ch != null){
                        switch(ch.charAt(0)){
                            case '=': //token '<='
                                codErro = defineTokenFound(true);
                                break;
                            default: //token '<'
                                codErro = defineTokenFound(false);
                                break;
                        }
                    }
                    break;
                case '>':
                    lerProximoChar();
                    if(ch != null){
                        switch(ch.charAt(0)){
                            case '=': //token '>='
                                codErro = defineTokenFound(true);
                                break;
                            default: //token '>'
                                codErro = defineTokenFound(false);
                                break;
                        }
                    }
                    break;
                case '=':
                    lerProximoChar();
                    if(ch != null){
                        switch(ch.charAt(0)){
                            case '=': //token '=='
                                codErro = defineTokenFound(true);
                                break;
                            default: //token '='
                                codErro = defineTokenFound(false);
                                break;
                        }
                    }
                    break;
                case '!':
                    lerProximoChar();
                    if(ch != null){
                        switch(ch.charAt(0)){
                            case '=': //token '!='
                                codErro = defineTokenFound(true);
                                break;
                            default: //ERRO ! SOZINHO
                                codErro = 2;
                                token = null;
                                break;
                        }
                    }
                    break;
                case '/':
                    lerProximoChar();
                    if(ch != null){
                        switch(ch.charAt(0)){
                            case '/': //comentario de linha unica "//"
                                consumirComentario(0);
                                token = scan();
                                break;
                            case '*': /* comentario multilinhas */
                                getNextChar();
                                codErro = consumirComentario(1);
                                if(codErro == 0){
                                    token = scan();
                                }
                                break;
                            default: //token '/'
                                codErro = defineTokenFound(false);
                                break;
                        }
                    }
                    break;

                default:
                    if(ER.ehLetra(ch) || ch.charAt(0) == '_'){ //pode ser identificador ou palavra reservada
                        montarPalavra();

                    }else if(ER.ehDigito(ch) || ch.charAt(0) == '.'){ //pode ser INTEIRO OU FLOAT
                        codErro = montarNumero();

                    } else if(ch.charAt(0) == '\''){ //pode ser um CHAR
                        codErro = montarChar();

                    }else{
                        //erro de caractere invalido
                        codErro = 1;
                        break;
                    }
            }
        }
        reportErro(codErro);
        
        if(token != null){
            ultimoTokenValido = token;
        }
        
        return token;
    }

    private void getNextChar() {
        if(leitor.hasNext()){
            ch =  leitor.next();
            cursor.addColuna();
            if(ch.charAt(0) == '\t'){
                cursor.addColuna(3);
            }
        } else{
            ch = null;
            EOF = true;
        }
    }
    
    private boolean ehCharBranco(String ch){
        if(ch != null && (ch.charAt(0) == '\n' || ch.charAt(0) == ' ' || ch.charAt(0) == '\t')){
            return true;
        } else{
            return false;
        }
    }

    private int defineTokenFound(boolean lerProximo) {
        if(lerProximo){
            lerProximoChar();
        }
        codigo = CodigosToken.lookUp(lexema);
        token = new Token(codigo, lexema);
        return 0;
    }
   
    
    private void lerProximoChar(){
        lexema += ch;
        getNextChar();
    }

    private int consumirComentario(int tipoComentario) {
        int erro = 0;
        
        if(tipoComentario == 0){ //comentario de uma linha
            
            while(ch.charAt(0) != '\n'){
                ch = leitor.next();
                cursor.addColuna();
            }
        } else{ /* Comentario multilinha */
            
            while(!EOF){
                if(ch.charAt(0) == '*'){
                    while(!EOF && ch.charAt(0) == '*'){
                        getNextChar();
                    }
                    if(!EOF && ch.charAt(0) == '/'){
                        getNextChar();
                        break;
                    }
                    if(ch.charAt(0) == '\n' && leitor.hasNextLine()){
                        cursor.addLinha();
                    }
                }else if(ch.charAt(0) == '\n' && leitor.hasNextLine()){
                    cursor.addLinha();
                }
                getNextChar();
                
            }
            if(EOF){
                erro = 3;
            }
        }
        return erro;
    }
    
    private void reportErro(int codErro) {
        if(codErro > 0){
            switch (codErro){
                case 1:
                    Erro.tokenError(cursor, ultimoTokenValido, "Caractere invalido: \"" + ch + "\"");
                    getNextChar();
                    break;
                case 2:
                    Erro.tokenError(cursor, ultimoTokenValido, "Ma formacao do operador relacional DIFERENTE. Caractere \"!\" sozinho");
                    break;
                case 3:
                    Erro.tokenError(cursor, ultimoTokenValido, "EOF dentro de comentario");
                    break;
                case 4:
                    Erro.tokenError(cursor, ultimoTokenValido, "Ma formacao de FLOAT \""+lexema+"\"");
                    break;
                case 5:
                    Erro.tokenError(cursor, ultimoTokenValido, "Ma formacao de CHAR \""+lexema+"\"");
                    break;
                case 6:
                    Erro.tokenError(cursor, ultimoTokenValido, "Token nao reconhecido \""+lexema+"\"");
                    break;
                default:
                    //NENHUM ERRO
                    break;
            }
        }
        
    }

    private int montarPalavra() {
        while(ER.ehLetra(ch) || ch.charAt(0) == '_' || ER.ehDigito(ch)){
            lerProximoChar();
        }
        return defineTokenFound(false);
    }

    private int montarNumero() {
        int codErro = 0;
        while(ER.ehDigito(ch)){
            lerProximoChar();
        }
        if(ch.charAt(0) != '.'){ //é inteiro
            codErro = defineTokenFound(false);
        } else{ //vai ser float ou dar erro de má formacao
            lerProximoChar();
            if(ER.ehDigito(ch)){
                while(ER.ehDigito(ch)){
                    lerProximoChar();
                }
                codErro = defineTokenFound(false);
            } else{
                codErro = 4;
            }
        }
        return codErro;
    }

    private int montarChar() {
        int codErro = 0;
        do{
            lerProximoChar();
            if(ch.charAt(0) == '\n' || ch == null){
                codErro = 5;
                return codErro;
            }
        }while(ch.charAt(0) != '\'');
        lerProximoChar();
        if(ER.ehChar(lexema)){
            codErro = defineTokenFound(false);
        } else{
            codErro = 5;
        }
        return codErro;
    }

    public boolean eof() {
        return EOF;
    }

    public Token getUltimoTokenValido() {
        return ultimoTokenValido;
    }
    
    public Cursor getCursor(){
        return cursor;
    }

    private Token tokenEOF() {
        ultimoTokenValido = new Token(-1, null);
        return ultimoTokenValido;
    }

}