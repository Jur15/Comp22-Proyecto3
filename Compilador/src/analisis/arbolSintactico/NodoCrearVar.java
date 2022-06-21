package analisis.arbolSintactico;

import informacion.TipoCompuesto;
import informacion.TipoIdentificador;

/**
 *
 * @author moral
 */
public class NodoCrearVar extends NodoSintactico {
    public TipoIdentificador id;
    public TipoCompuesto tipo;

    public NodoCrearVar(String tipo, TipoIdentificador id) {
        this.tipo = new TipoCompuesto(tipo);
        this.id = id;
    }

    public NodoCrearVar(String tipo, TipoIdentificador id, Integer tamArray) {
        this.tipo = new TipoCompuesto(tipo,tamArray);
        this.id = id;
    }
}
