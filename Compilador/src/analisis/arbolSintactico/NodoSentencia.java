package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoSentencia extends NodoSintactico {
    public NodoSintactico sentencia;

    public NodoSentencia(NodoSintactico sentencia) {
        this.sentencia = sentencia;
    }
}
