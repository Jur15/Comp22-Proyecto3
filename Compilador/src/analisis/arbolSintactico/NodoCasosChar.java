package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoCasosChar extends NodoSintactico {
    public char caso;
    public NodoBloqueCod bloque;
    public NodoCasosChar nodoCasos;

    public NodoCasosChar(char caso, NodoBloqueCod bloque) {
        this.caso = caso;
        this.bloque = bloque;
        this.nodoCasos = null;
    }

    public NodoCasosChar(char caso, NodoBloqueCod bloque, NodoCasosChar nodoCasos) {
        this.caso = caso;
        this.bloque = bloque;
        this.nodoCasos = nodoCasos;
    }
}
