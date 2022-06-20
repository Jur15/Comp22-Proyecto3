package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoEstrucIf extends NodoSintactico {
    public NodoCondicion condicion;
    public NodoBloqueCod bloque;

    public NodoEstrucIf(NodoCondicion condicion, NodoBloqueCod bloque) {
        this.condicion = condicion;
        this.bloque = bloque;
    }
}
