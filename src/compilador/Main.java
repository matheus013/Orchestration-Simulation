package compilador;

import util.Erro;
import scanner.Tokenizer;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import parser.Parser;

public class Main {
    
    private static String enderecoArquivo = null;
    
    public static void main(String[] args){
        Tokenizer scanner;
        Parser parser;
        if(args.length == 1){
            enderecoArquivo = args[0];
            scanner = abrirArquivo(enderecoArquivo);
            
            if(scanner != null){
                parser = new Parser(scanner);
                parser.run();
            } else{
                Erro.show(0);
            }
            
        } else if(args.length > 1){
            Erro.show(1);
        } else{
            Erro.show(0);
        }

    }

    private static Tokenizer abrirArquivo(String arquivo){
        Path path;
        Scanner scan;
        path = Paths.get(arquivo);
    
        try {
            scan = new Scanner(path.toFile());
            return new Tokenizer(scan);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
}
