package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoParametros extends NodoSintactico {
    public NodoParametro parametro;
    public NodoParametros parametros;

    public NodoParametros(NodoParametro parametro) {
        this.parametro = parametro;
        this.parametros = null;
    }

    public NodoParametros(NodoParametro parametro, NodoParametros parametros) {
        this.parametro = parametro;
        this.parametros = parametros;
    }
}
