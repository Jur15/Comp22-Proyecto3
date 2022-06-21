package informacion;

import java.util.Objects;

/**
 *
 * @author moral
 */
public class TipoCompuesto {
    public String tipo;
    public int tamArray;
    public boolean esArray;

    public TipoCompuesto(String tipo) {
        this.tipo = tipo;
        this.tamArray = 0;
        this.esArray = false;
    }

    public TipoCompuesto(String tipo, int tamArray) {
        this.tipo = tipo;
        this.tamArray = tamArray;
        this.esArray = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoCompuesto other = (TipoCompuesto) obj;
        if (this.tamArray != other.tamArray) {
            return false;
        }
        if (this.esArray != other.esArray) {
            return false;
        }
        return Objects.equals(this.tipo, other.tipo);
    }
}
