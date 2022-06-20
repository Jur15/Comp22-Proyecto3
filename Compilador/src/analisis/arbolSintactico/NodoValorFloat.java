package analisis.arbolSintactico;

/**
 *
 * @author moral
 */
public class NodoValorFloat extends NodoSintactico {
    public float valor;
    public boolean esEntradaEst;

    public NodoValorFloat(float valor) {
        this.valor = valor;
        this.esEntradaEst = false;
    }

    public NodoValorFloat() {
        this.esEntradaEst = true;
    }
}
