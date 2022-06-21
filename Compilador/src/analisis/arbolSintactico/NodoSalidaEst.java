package analisis.arbolSintactico;

import informacion.TipoCompuesto;

/**
 *
 * @author moral
 */
public class NodoSalidaEst extends NodoSintactico {

    public Object valor;
    public TipoCompuesto tipo;

    public NodoSalidaEst(Object valor) {
        this.valor = valor;
        this.tipo = null;
    }
}
