package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoOperadorRel extends NodoSintactico {
    public String operador;

    public NodoOperadorRel(String operador) {
        this.operador = operador;
    }
}
