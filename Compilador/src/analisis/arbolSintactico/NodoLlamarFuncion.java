package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoLlamarFuncion extends NodoSintactico {
    public String id;
    public NodoParametros parametros;

    public NodoLlamarFuncion(String id) {
        this.id = id;
        this.parametros = null;
    }
    
    public NodoLlamarFuncion(String id, NodoParametros parametros) {
        this.id = id;
        this.parametros = parametros;
    }
    
    
}
