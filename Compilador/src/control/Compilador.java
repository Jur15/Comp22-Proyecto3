package control;

import analisis.Lexer;
import analisis.Parser;
import analisis.excepcion.LexicalException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JFileChooser;

/**
 *
 * @author moral
 */
public class Compilador {

    public static int suma(int a, int b) {
        return a + b;
    }
    
    public static void prueba(int a[]) {
        return;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final JFileChooser fc = new JFileChooser();
        int codigoOper = fc.showOpenDialog(null);
        if (codigoOper == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            try {
                Parser p = new Parser(new Lexer(new BufferedReader(new FileReader(archivo))));
                Object result = p.parse().value;
                System.out.println("\nEl archivo se puede compilar.");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }
    
}
