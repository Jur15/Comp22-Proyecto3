package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoCasosString extends NodoSintactico {
    public String caso;
    public NodoBloqueCod bloque;
    public NodoCasosString nodoCasos;

    public NodoCasosString(String caso, NodoBloqueCod bloque) {
        this.caso = caso;
        this.bloque = bloque;
        this.nodoCasos = null;
    }

    public NodoCasosString(String caso, NodoBloqueCod bloque, NodoCasosString nodoCasos) {
        this.caso = caso;
        this.bloque = bloque;
        this.nodoCasos = nodoCasos;
    }
}
