package analisis.excepcion;

/**
 *
 * @author moral
 */
public class LexicalException extends Exception {
    public LexicalException(String message) {
        super("Error lexico: " + message);
    }
}
