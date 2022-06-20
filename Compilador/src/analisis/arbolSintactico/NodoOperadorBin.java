package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoOperadorBin extends NodoSintactico {
    public String operador;

    public NodoOperadorBin(String operador) {
        this.operador = operador;
    }
}
