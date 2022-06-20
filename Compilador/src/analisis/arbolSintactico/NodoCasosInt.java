package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoCasosInt extends NodoSintactico {
    public int caso;
    public NodoBloqueCod bloque;
    public NodoCasosInt nodoCasos;

    public NodoCasosInt(int caso, NodoBloqueCod bloque) {
        this.caso = caso;
        this.bloque = bloque;
        this.nodoCasos = null;
    }

    public NodoCasosInt(int caso, NodoBloqueCod bloque, NodoCasosInt nodoCasos) {
        this.caso = caso;
        this.bloque = bloque;
        this.nodoCasos = nodoCasos;
    }
}
