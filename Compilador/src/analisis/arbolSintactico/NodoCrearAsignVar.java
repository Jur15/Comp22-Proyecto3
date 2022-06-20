package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoCrearAsignVar extends NodoSintactico {
    public NodoCrearVar nodoCrear;
    public Object valor;

    public NodoCrearAsignVar(NodoCrearVar nodoCrear, Object valor) {
        this.nodoCrear = nodoCrear;
        this.valor = valor;
    }
}
