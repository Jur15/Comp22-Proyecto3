package analisis.arbolSintactico;

import informacion.TipoIdentificador;

/**
 *
 * @author moral
 */
public class NodoLlamarFuncion extends NodoSintactico {
    public TipoIdentificador id;
    public NodoParametros parametros;

    public NodoLlamarFuncion(TipoIdentificador id) {
        this.id = id;
        this.parametros = null;
    }
    
    public NodoLlamarFuncion(TipoIdentificador id, NodoParametros parametros) {
        this.id = id;
        this.parametros = parametros;
    }
    
    
}
