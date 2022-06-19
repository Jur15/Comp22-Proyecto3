package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoCrearFuncion extends NodoSintactico {
    public String tipo, id;
    public NodoCrearParametros parametros;
    public NodoBloqueCod bloque;

    public NodoCrearFuncion(String tipo, String id, NodoBloqueCod bloque) {
        this.tipo = tipo;
        this.id = id;
        this.parametros = null;
        this.bloque = bloque;
    }
    
    public NodoCrearFuncion(String tipo, String id, NodoCrearParametros parametros, NodoBloqueCod bloque) {
        this.tipo = tipo;
        this.id = id;
        this.parametros = parametros;
        this.bloque = bloque;
    }
    
    
}
