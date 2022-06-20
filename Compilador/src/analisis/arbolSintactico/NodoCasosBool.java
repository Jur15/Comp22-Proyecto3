package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoCasosBool extends NodoSintactico {
    public boolean caso;
    public NodoBloqueCod bloque;
    public NodoCasosBool nodoCasos;

    public NodoCasosBool(boolean caso, NodoBloqueCod bloque) {
        this.caso = caso;
        this.bloque = bloque;
        this.nodoCasos = null;
    }

    public NodoCasosBool(boolean caso, NodoBloqueCod bloque, NodoCasosBool nodoCasos) {
        this.caso = caso;
        this.bloque = bloque;
        this.nodoCasos = nodoCasos;
    }
}
