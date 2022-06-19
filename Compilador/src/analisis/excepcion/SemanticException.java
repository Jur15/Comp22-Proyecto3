package analisis.excepcion;

/**
 *
 * @author moral
 */
public class SemanticException extends Exception {
    public SemanticException(String message) {
        super("Error semantico: " + message);
    }
}
