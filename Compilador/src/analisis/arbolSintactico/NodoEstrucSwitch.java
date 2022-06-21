package analisis.arbolSintactico;

import informacion.TipoIdentificador;

/**
 *
 * @author moral
 */
public class NodoEstrucSwitch extends NodoSintactico {
    public TipoIdentificador id;
    public String tipo;
    public NodoSintactico casos;
    public NodoCasoDefault casoDefault;    

    public NodoEstrucSwitch(TipoIdentificador id, String tipo, NodoSintactico casos, NodoCasoDefault casoDefault) {
        this.id = id;
        this.tipo = tipo;
        this.casos = casos;
        this.casoDefault = casoDefault;
    }
}
