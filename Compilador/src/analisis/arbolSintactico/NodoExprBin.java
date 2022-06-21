package analisis.arbolSintactico;

import informacion.TipoCompuesto;

/**
 *
 * @author moral
 */
public class NodoExprBin extends NodoSintactico {
    public Object operando1, operando2;
    public NodoOperadorBin operador;
    
    public TipoCompuesto tipo;

    public NodoExprBin(Object operando1, Object operando2, NodoOperadorBin operador) {
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operador = operador;
        this.tipo = null;
    }
}
