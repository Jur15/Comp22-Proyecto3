package informacion;

import java.util.Objects;

/**
 *
 * @author moral
 */
public class TipoIdentificador {

    public String id;

    public TipoIdentificador(String id) {
        this.id = id;
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
        final TipoIdentificador other = (TipoIdentificador) obj;
        return Objects.equals(this.id, other.id);
    }
}
