package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoExprUna extends NodoSintactico {
    public Object operando;
    public NodoOperadorUna operador;

    public NodoExprUna(Object operando, NodoOperadorUna operador) {
        this.operando = operando;
        this.operador = operador;
    }
}
