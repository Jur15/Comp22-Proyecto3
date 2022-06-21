package informacion;

import java.util.Objects;

/**
 *
 * @author moral
 */
public class DetalleParametro {

    public TipoIdentificador id;
    public TipoCompuesto tipo;

    public DetalleParametro(TipoIdentificador id, TipoCompuesto tipo) {
        this.id = id;
        this.tipo = tipo;
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
        final DetalleParametro other = (DetalleParametro) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.tipo, other.tipo);
    }
}
