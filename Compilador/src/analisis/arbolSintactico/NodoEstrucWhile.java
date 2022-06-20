package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoEstrucWhile extends NodoSintactico {
    public NodoCondicion condicion;
    public NodoBloqueCod bloque;

    public NodoEstrucWhile(NodoCondicion condicion, NodoBloqueCod bloque) {
        this.condicion = condicion;
        this.bloque = bloque;
    }
}
