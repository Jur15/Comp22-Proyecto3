package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoExprRel extends NodoSintactico {
    public Object operando1, operando2;
    public NodoOperadorRel operador;

    public NodoExprRel(Object operando1, Object operando2, NodoOperadorRel operador) {
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operador = operador;
    }
}
