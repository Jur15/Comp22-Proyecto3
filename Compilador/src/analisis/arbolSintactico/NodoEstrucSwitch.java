package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoEstrucSwitch extends NodoSintactico {
    public String id, tipo;
    public NodoSintactico casos;
    public NodoCasoDefault casoDefault;    

    public NodoEstrucSwitch(String id, String tipo, NodoSintactico casos, NodoCasoDefault casoDefault) {
        this.id = id;
        this.tipo = tipo;
        this.casos = casos;
        this.casoDefault = casoDefault;
    }
}
