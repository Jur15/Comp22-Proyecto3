package analisis;

import informacion.DetalleVariable;
import informacion.DetalleParametro;
import informacion.DetalleFuncion;
import informacion.TipoCompuesto;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author moral
 */
public class TablaSimbolos {

    private Hashtable<String, Hashtable<String, DetalleVariable>> tabla;
    private Hashtable<String, DetalleFuncion> listaFunciones;

    public TablaSimbolos() {
        tabla = new Hashtable<>();
        listaFunciones = new Hashtable<>();
    }

    //Bloques
    public void agregarBloque(String nombre) {
        tabla.put(nombre, new Hashtable<>());
    }

    //Funciones
    public void agregarFuncion(String nombre, String tipo, ArrayList<DetalleParametro> parametros) {
        listaFunciones.put(nombre, new DetalleFuncion(tipo, parametros));
    }

    public boolean existeFuncion(String nombre) {
        return listaFunciones.get(nombre) != null;
    }

    public DetalleFuncion getDetalleFuncion(String nombre) {
        return listaFunciones.get(nombre);
    }

    //Variables  
    public void agregarVar(String bloque, String nombre, TipoCompuesto tipo, int offsetMemoria) {
        tabla.get(bloque).put(nombre, new DetalleVariable(tipo, offsetMemoria));
    }

    public boolean existeVar(ArrayList<String> bloquesAlcanzables, String nombre) {
        return !getBloqueVar(bloquesAlcanzables, nombre).equals("");
    }

    public DetalleVariable getDetalleVariable(ArrayList<String> bloquesAlcanzables, String nombre) {
        return tabla.get(getBloqueVar(bloquesAlcanzables, nombre)).get(nombre);
    }

    private String getBloqueVar(ArrayList<String> bloquesAlcanzables, String nombre) {
        for (String b : bloquesAlcanzables) {
            if (tabla.get(b).get(nombre) != null) {
                return b;
            }
        }
        return "";
    }

    //Acceso desde generador de codigo
    public int calcularEspacioStack(String funcion) {
        ArrayList<String> bloques = getBloquesFuncion(funcion);
        int espacio = -1;
        for (String s : bloques) {
            Iterator<DetalleVariable> variables = tabla.get(s).elements().asIterator();
            while (variables.hasNext()) {
                DetalleVariable detVar = variables.next();
                if (detVar.offsetMemoria > espacio) {
                    espacio = detVar.offsetMemoria;
                }
            }
        }
        return espacio;
    }

    private ArrayList<String> getBloquesFuncion(String funcion) {
        ArrayList<String> result = new ArrayList<>();
        Iterator<String> bloques = tabla.keys().asIterator();
        while (bloques.hasNext()) {
            String bloqueAct = bloques.next();
            if (bloqueAct.contains(funcion)) {
                result.add(bloqueAct);
            }
        }
        return result;
    }
}
