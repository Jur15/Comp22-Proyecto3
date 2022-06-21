package informacion;

import java.util.ArrayList;

/**
 *
 * @author moral
 */
public class DetalleFuncion {

    public String tipo;
    public ArrayList<DetalleParametro> parametros;

    public DetalleFuncion(String tipo, ArrayList<DetalleParametro> parametros) {
        this.tipo = tipo;
        this.parametros = parametros;
    }

    public int getCantParams() {
        return parametros.size();
    }

    public int getPosicionParam(String nombre) {
        for(int i = 0; i < parametros.size(); i++) {
            if(parametros.get(i).id.equals(nombre)) {
                return i;
            }
        }
        return 0;
    }
    
    public DetalleParametro getDetalleParametro(String nombre) {
        return parametros.get(getPosicionParam(nombre));
    }
}
