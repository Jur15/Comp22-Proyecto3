package analisis.arbolSintactico;

import informacion.TipoIdentificador;

/**
 *
 * @author moral
 */
public class NodoCrearFuncion extends NodoSintactico {
    public String tipo;
    public TipoIdentificador id;
    public NodoCrearParametros parametros;
    public NodoBloqueCod bloque;

    public NodoCrearFuncion(String tipo, TipoIdentificador id, NodoBloqueCod bloque) {
        this.tipo = tipo;
        this.id = id;
        this.parametros = null;
        this.bloque = bloque;
    }
    
    public NodoCrearFuncion(String tipo, TipoIdentificador id, NodoCrearParametros parametros, NodoBloqueCod bloque) {
        this.tipo = tipo;
        this.id = id;
        this.parametros = parametros;
        this.bloque = bloque;
    }
    
    
}
