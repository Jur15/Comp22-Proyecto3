package analisis.arbolSintactico;

import informacion.TipoIdentificador;

/**
 *
 * @author moral
 */
public class NodoAsignVar extends NodoSintactico {
    public TipoIdentificador id;
    public Object valor;

    public NodoAsignVar(TipoIdentificador id, Object valor) {
        this.id = id;
        this.valor = valor;
    }
}
