package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoCasosFloat extends NodoSintactico {
    public float caso;
    public NodoBloqueCod bloque;
    public NodoCasosFloat nodoCasos;
    
    public NodoCasosFloat(float caso, NodoBloqueCod bloque) {
        this.caso = caso;
        this.bloque = bloque;
        this.nodoCasos = null;
    }

    public NodoCasosFloat(float caso, NodoBloqueCod bloque, NodoCasosFloat nodoCasos) {
        this.caso = caso;
        this.bloque = bloque;
        this.nodoCasos = nodoCasos;
    }
}
