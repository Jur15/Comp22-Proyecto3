package analisis;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 * @author moral
 */
public class ExtendedSymbol extends java_cup.runtime.Symbol {

    private int line;

    public ExtendedSymbol(int type, int line) {
        this(type, line, -1, -1, null);
    }

    public ExtendedSymbol(int type, int line, Object value) {
        this(type, line, -1, -1, value);
    }

    public ExtendedSymbol(int type, int line, int left, int right, Object value) {
        super(type, left, right, value);
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    private String getNombreSimbolo(int value) {
    for (Field f : ParserSym.class.getDeclaredFields()) {
        int mod = f.getModifiers();
        if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)) {
            try {
                if((int) f.get(null) == value) {return f.getName();}
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    return null;
}
    
    public String toString() {
        return "Simbolo: " 
                + getNombreSimbolo(sym)
                + ", Linea: "
                + line
                + (value == null ? "" : (", Valor: '" + value + "'"));
    }
}
