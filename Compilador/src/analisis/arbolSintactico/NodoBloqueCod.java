package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoBloqueCod extends NodoSintactico {
    public NodoSintactico hijo;
    public NodoBloqueCod bloqueCod;

    public NodoBloqueCod(NodoSintactico hijo) {
        this.hijo = hijo;
        this.bloqueCod = null;
    }

    public NodoBloqueCod(NodoSintactico hijo, NodoBloqueCod bloqueCod) {
        this.hijo = hijo;
        this.bloqueCod = bloqueCod;
    }
    
}
