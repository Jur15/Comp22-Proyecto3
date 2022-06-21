package analisis.arbolSintactico;

import informacion.TipoCompuesto;

/**
 *
 * @author moral
 */
public class NodoExprUna extends NodoSintactico {
    public Object operando;
    public NodoOperadorUna operador;
    
    public TipoCompuesto tipo;

    public NodoExprUna(Object operando, NodoOperadorUna operador) {
        this.operando = operando;
        this.operador = operador;
        this.tipo = null;
    }
}
