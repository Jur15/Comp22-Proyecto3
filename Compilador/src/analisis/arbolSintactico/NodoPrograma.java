package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoPrograma extends NodoSintactico {

    public NodoFuncMain funcMain;
    public NodoFuncUsuario funcUsuario;

    public NodoPrograma(NodoFuncMain funcMain) {
        this.funcMain = funcMain;
        this.funcUsuario = null;
    }

    public NodoPrograma(NodoFuncMain funcMain, NodoFuncUsuario funcUsuario) {
        this.funcMain = funcMain;
        this.funcUsuario = funcUsuario;
    }
}
