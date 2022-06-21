package analisis.arbolSintactico;

import informacion.TipoIdentificador;

/**
 *
 * @author moral
 */
public class NodoElemArreg extends NodoSintactico {
    public TipoIdentificador id;
    public int posicion;

    public NodoElemArreg(TipoIdentificador id, Integer posicion) {
        this.id = id;
        this.posicion = posicion;
    }
}
