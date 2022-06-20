package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoCrearParametro extends NodoSintactico {
    public String tipo, id;
    public int tamArray;
    public boolean esArray;

    public NodoCrearParametro(String tipo, String id) {
        this.tipo = tipo;
        this.id = id;
        this.tamArray = 0;
        this.esArray = false;
    }
    
    public NodoCrearParametro(String tipo, String id, int tamArray) {
        this.tipo = tipo;
        this.id = id;
        this.tamArray = tamArray;
        this.esArray = true;
    }
    
    
}
