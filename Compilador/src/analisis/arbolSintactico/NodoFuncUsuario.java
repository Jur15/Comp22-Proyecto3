package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoFuncUsuario extends NodoSintactico {
    public NodoCrearFuncion funcion;
    public NodoFuncUsuario funcUsuario;

    public NodoFuncUsuario(NodoCrearFuncion funcion, NodoFuncUsuario funcUsuario) {
        this.funcion = funcion;
        this.funcUsuario = funcUsuario;
    }

    public NodoFuncUsuario(NodoCrearFuncion funcion) {
        this.funcion = funcion;
        this.funcUsuario = null;
    }
}
