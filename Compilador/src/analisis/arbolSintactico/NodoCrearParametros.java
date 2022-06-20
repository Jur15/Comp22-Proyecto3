package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoCrearParametros extends NodoSintactico {
    public NodoCrearParametro parametro;
    public NodoCrearParametros crearParametros;

    public NodoCrearParametros(NodoCrearParametro parametro, NodoCrearParametros crearParametros) {
        this.parametro = parametro;
        this.crearParametros = crearParametros;
    }
    
    public NodoCrearParametros(NodoCrearParametro parametro) {
        this.parametro = parametro;
        this.crearParametros = null;
    }
}
