package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoElemArreg extends NodoSintactico {
    public String id;
    public int posicion;

    public NodoElemArreg(String id, int posicion) {
        this.id = id;
        this.posicion = posicion;
    }
}
