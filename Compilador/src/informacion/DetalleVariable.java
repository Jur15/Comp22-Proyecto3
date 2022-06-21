package informacion;

import java.util.ArrayList;

/**
 *
 * @author moral
 */
public class DetalleVariable {

    public TipoCompuesto tipo;
    public Object valor;
    public int offsetMemoria;

    public DetalleVariable(TipoCompuesto tipo, int offsetMemoria) {
        this.tipo = tipo;
        this.offsetMemoria = offsetMemoria;
        if(tipo.esArray) {
            valor = new ArrayList<Object>(tipo.tamArray);
        } else {
            valor = null;
        }
    }

}
