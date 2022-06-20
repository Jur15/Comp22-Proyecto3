package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoCrearVar extends NodoSintactico {
    public String tipo, id;
    public int tamArray;
    public boolean esArray;

    public NodoCrearVar(String tipo, String id) {
        this.tipo = tipo;
        this.id = id;
        this.tamArray = 0;
        this.esArray = false;
    }

    public NodoCrearVar(String tipo, String id, int tamArray) {
        this.tipo = tipo;
        this.id = id;
        this.tamArray = tamArray;
        this.esArray = true;
    }
}
