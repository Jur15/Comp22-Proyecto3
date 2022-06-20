package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoCondicion extends NodoSintactico {
    public Object condicion;

    public NodoCondicion(Object condicion) {
        this.condicion = condicion;
    }
}
