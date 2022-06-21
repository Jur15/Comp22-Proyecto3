package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoValorInt extends NodoSintactico {
    public int valor;
    public boolean esEntradaEst;

    public NodoValorInt(Integer valor) {
        this.valor = valor;
        this.esEntradaEst = false;
    }

    public NodoValorInt() {
        this.valor = 0;
        this.esEntradaEst = true;
    }
}
