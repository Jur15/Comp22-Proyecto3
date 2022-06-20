package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoAsignVar extends NodoSintactico {
    public String id;
    public Object valor;

    public NodoAsignVar(String id, Object valor) {
        this.id = id;
        this.valor = valor;
    }
}
