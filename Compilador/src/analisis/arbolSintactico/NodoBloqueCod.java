package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoBloqueCod extends NodoSintactico {
    public NodoSintactico linea;
    public NodoBloqueCod bloqueCod;

    public NodoBloqueCod(NodoSintactico linea) {
        this.linea = linea;
        this.bloqueCod = null;
    }

    public NodoBloqueCod(NodoSintactico linea, NodoBloqueCod bloqueCod) {
        this.linea = linea;
        this.bloqueCod = bloqueCod;
    }
    
}
