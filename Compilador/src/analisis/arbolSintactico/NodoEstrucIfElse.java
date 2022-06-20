package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoEstrucIfElse extends NodoSintactico {
    public NodoCondicion condicion;
    public NodoBloqueCod bloqueIf, bloqueElse;    

    public NodoEstrucIfElse(NodoCondicion condicion, NodoBloqueCod bloqueIf, NodoBloqueCod bloqueElse) {
        this.condicion = condicion;
        this.bloqueIf = bloqueIf;
        this.bloqueElse = bloqueElse;
    }
}
