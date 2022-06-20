package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoAsignElemArreg extends NodoSintactico {
    public NodoElemArreg elemento;
    public Object valor;

    public NodoAsignElemArreg(NodoElemArreg elemento, Object valor) {
        this.elemento = elemento;
        this.valor = valor;
    }
}
