package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoExprLog extends NodoSintactico {
    public Object operando1, operando2;
    public NodoOperadorLog operador;

    public NodoExprLog(Object operando1, Object operando2, NodoOperadorLog operador) {
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operador = operador;
    }

    public NodoExprLog(Object operando1, NodoOperadorLog operador) {
        this.operando1 = operando1;
        this.operando2 = null;
        this.operador = operador;
    }
}
