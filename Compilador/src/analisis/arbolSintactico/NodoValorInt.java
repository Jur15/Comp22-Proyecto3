package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoValorInt extends NodoSintactico {
    public int valor;
    public boolean esEntradaEst;

    public NodoValorInt(int valor) {
        this.valor = valor;
        this.esEntradaEst = false;
    }

    public NodoValorInt() {
        this.esEntradaEst = true;
    }
}
