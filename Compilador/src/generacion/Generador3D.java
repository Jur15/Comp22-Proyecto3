package generacion;

import analisis.TablaSimbolos;
import analisis.arbolSintactico.*;
import informacion.DetalleFuncion;
import informacion.DetalleParametro;
import informacion.DetalleVariable;
import informacion.TipoIdentificador;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author moral
 */
public class Generador3D {

    private NodoPrograma arbol;
    private TablaSimbolos tabla;
    private PrintWriter escritor;

    //Contadores y referencias
    private String nFuncAct, bloqueAct, nEstrucAct;
    private ArrayList<String> bloquesAlcanzables;
    private int espacioStack;
    private int registroAct, registroFloatAct;
    private int contArrayInt, contArrayChar, contString, contChar;

    private int cParams;
    private int contIf, contWhile, contSwitch, contCase;

    public Generador3D(NodoPrograma arbol, TablaSimbolos tabla) {
        this.arbol = arbol;
        this.tabla = tabla;
        reiniciarPropiedades();
    }

    private void reiniciarPropiedades() {
        //Referencias
        nFuncAct = "";
        bloqueAct = "";
        nEstrucAct = "";
        bloquesAlcanzables = new ArrayList<>();
        espacioStack = -1;
        registroAct = 0;
        registroFloatAct = 0;
        //Contadores
        contArrayInt = 0;
        contArrayChar = 0;
        contString = 0;
        contChar = 0;

        cParams = 0;
        contIf = 0;
        contWhile = 0;
        contSwitch = 0;
        contCase = 0;
    }

    public void generar() {
        try {
            File file = new File("resultado/TresDirec.txt");
            file.createNewFile();
            FileWriter f = new FileWriter(file);
            escritor = new PrintWriter(f);
            //Recorre el arbol
            escribirPrograma(arbol);
            //Cierra el escritor
            escritor.close();
            System.out.println("Codigo de Tres Direcciones generado.");
        } catch (IOException ex) {
            System.out.println("Error al generar archivo de salida.");
            System.out.println(ex.getMessage());
        }
    }

    private void escribirPrograma(NodoPrograma n) {
        escribirMain(n.funcMain);
        escritor.println();
        if (n.funcUsuario != null) {
            escribirFuncUsuario(n.funcUsuario);
        }
    }

    private void escribirMain(NodoFuncMain n) {
        nFuncAct = "main";
        bloqueAct = nFuncAct;
        bloquesAlcanzables.add(bloqueAct);
        escritor.println("main_begin:"); //Inicio de la funcion

        //Determina el espacio necesario para el stack
        espacioStack = tabla.calcularEspacioStack(nFuncAct);
        if (espacioStack != -1) {
            espacioStack += 4;
            escritor.printf("stack_space -%d%n", espacioStack); //Espacio en el stack
        }

        escribirBloqueCod(n.bloqueCod); //Resto del codigo
        escritor.printf("exit_program%nmain_end%n"); //Fin de la funcion
        bloquesAlcanzables.remove(bloqueAct);
    }

    private void escribirFuncUsuario(NodoFuncUsuario n) {
        reiniciarPropiedades();
        escribirCrearFuncion(n.funcion);
        escritor.println();

        if (n.funcUsuario != null) {
            escribirFuncUsuario(n.funcUsuario);
        }
    }

    private void escribirCrearFuncion(NodoCrearFuncion n) {
        nFuncAct = n.id.id;
        bloqueAct = nFuncAct;
        bloquesAlcanzables.add(bloqueAct);
        escritor.printf("%s_begin:%n", nFuncAct); //Inicio de la funcion

        //Determina el espacio necesario para el stack
        espacioStack = tabla.calcularEspacioStack(nFuncAct);
        if (espacioStack != -1) {
            espacioStack += 4;
            escritor.printf("stack_space -%d%n", espacioStack); //Espacio en el stack
        }

        escribirBloqueCod(n.bloque); //Resto del codigo
        escritor.printf("%s_end", nFuncAct); //Fin de la funcion
        bloquesAlcanzables.remove(bloqueAct);
    }

    private void escribirBloqueCod(NodoBloqueCod n) {
        if (n.linea != null) {
            if (n.linea instanceof NodoSentencia) {
                escribirSentencia((NodoSentencia) n.linea);
            } else {
                //escribirEstrucControl(n.linea);
            }
        }

        if (n.bloqueCod != null) {
            escribirBloqueCod(n.bloqueCod);
        }
    }

    private void escribirSentencia(NodoSentencia n) {
        if (n.sentencia instanceof NodoCrearVar) {
            escribirCrearVar((NodoCrearVar) n.sentencia);
        } else if (n.sentencia instanceof NodoCrearAsignVar) {
            escribirCrearAsignVar((NodoCrearAsignVar) n.sentencia);
        } else if (n.sentencia instanceof NodoAsignVar) {
            escribirAsignVar((NodoAsignVar) n.sentencia);
        } else if (n.sentencia instanceof NodoAsignElemArreg) {
            //escribirAsignElemArreg((NodoAsignElemArreg) n.sentencia);
        } else if (n.sentencia instanceof NodoLlamarFuncion) {
            escribirLlamarFuncion((NodoLlamarFuncion) n.sentencia);
        } else if (n.sentencia instanceof NodoSalidaEst) {
            //escribirSalidaEst((NodoSalidaEst) n.sentencia);
        } else if (n.sentencia instanceof NodoBreak) {
            //escribirBreak((NodoBreak) n.sentencia);
        } else if (n.sentencia instanceof NodoReturn) {
            //escribirReturn((NodoReturn) n.sentencia);
        } else if (n.sentencia instanceof NodoExprBin) {
            //escribirExpBin((NodoExprBin) n.sentencia);
        } else if (n.sentencia instanceof NodoExprUna) {
            //escribirExpUna((NodoExprUna) n.sentencia);
        } else if (n.sentencia instanceof NodoExprRel) {
            //escribirExpRel((NodoExprRel) n.sentencia);
        } else if (n.sentencia instanceof NodoExprLog) {
            //escribirExpLog((NodoExprLog) n.sentencia);
        }
    }

    private void escribirCrearVar(NodoCrearVar n) {
        String nombreVar = n.id.id;
        DetalleVariable detVar = tabla.getDetalleVariable(bloquesAlcanzables, nombreVar);
        String tipoVar = n.tipo.tipo;
        if (tipoVar.equals("int")) {
            if (n.tipo.esArray) {
                String valor = ".space " + 4 * n.tipo.tamArray;
                String label = String.format("var_%s_int_array_%d", nFuncAct, contArrayInt);
                detVar.label = label;
                escritor.printf("%s = %s%n", label, valor);
                contArrayInt++;
            } else {
                escritor.printf("stack_%d = 0%n", detVar.offsetMemoria);
            }
        } else if (tipoVar.equals("char")) {
            if (n.tipo.esArray) {
                String valor = ".space " + n.tipo.tamArray;
                String label = String.format("var_%s_char_array_%d", nFuncAct, contArrayChar);
                detVar.label = label;
                escritor.printf("%s = %s%n", label, valor);
                contArrayChar++;
            } else {
                String label = String.format("var_%s_char_%d", nFuncAct, contChar);
                detVar.label = label;
                escritor.printf("%s = .space 1%n", label);
                contChar++;
            }
        } else if (tipoVar.equals("float")) {
            escritor.printf("stack_%d = 0.0%n", detVar.offsetMemoria);
        } else if (tipoVar.equals("boolean")) {
            escritor.printf("stack_%d = false%n", detVar.offsetMemoria);
        } else if (tipoVar.equals("String")) {
            String label = String.format("var_%s_String_%d", nFuncAct, contString);
            detVar.label = label;
            escritor.printf("%s = .asciiz \"\"%n", label);
            contString++;
        }
    }

    private void escribirCrearAsignVar(NodoCrearAsignVar n) {
        String nombreVar = n.nodoCrear.id.id;
        String tipoVar = n.nodoCrear.tipo.tipo;
        DetalleVariable detVar = tabla.getDetalleVariable(bloquesAlcanzables, nombreVar);
        if (n.valor != null) {
            String valor = escribirValor(n.valor); //Obtiene la ubicacion del valor
            if (tipoVar.equals("int")) {
                escritor.printf("stack_%d = %s%n", detVar.offsetMemoria, valor);
            } else if (tipoVar.equals("float")) {
                escritor.printf("stack_%d = %s%n", detVar.offsetMemoria, valor);
            } else if (tipoVar.equals("boolean")) {
                escritor.printf("stack_%d = %s%n", detVar.offsetMemoria, valor);
            } else if (tipoVar.equals("char")) {
                escritor.printf("var_%s_char_%d = %s%n", nFuncAct, contChar, valor);
                contChar++;
            } else if (tipoVar.equals("String")) {
                escritor.printf("var_%s_String_%d = %s%n", nFuncAct, contString, valor);
                contString++;
            }
        } else {
            //El valor es nulo por lo que asigna el valor default
            if (tipoVar.equals("int")) {
                escritor.printf("stack_%d = 0n", detVar.offsetMemoria);
            } else if (tipoVar.equals("float")) {
                escritor.printf("stack_%d = 0.0n", detVar.offsetMemoria);
            } else if (tipoVar.equals("boolean")) {
                escritor.printf("stack_%d = false%n", detVar.offsetMemoria);
            } else if (tipoVar.equals("char")) {
                String label = String.format("var_%s_char_%d", nFuncAct, contChar);
                detVar.label = label;
                escritor.printf("%s = .space 1%n", label);
                contChar++;
            } else if (tipoVar.equals("String")) {
                String label = String.format("var_%s_String_%d", nFuncAct, contString);
                detVar.label = label;
                escritor.printf("%s = .asciiz \"\"%n", label);
                contString++;
            }
        }
        registroAct = 0;
        registroFloatAct = 0;
    }

    private void escribirAsignVar(NodoAsignVar n) {
        DetalleVariable detVar = tabla.getDetalleVariable(bloquesAlcanzables, n.id.id);
        String tipoVar = detVar.tipo.tipo;
        if (n.valor != null) {
            String valor = escribirValor(n.valor); //Obtiene la ubicacion del valor
            if (tipoVar.equals("int")) {
                escritor.printf("stack_%d = %s%n", detVar.offsetMemoria, valor);
            } else if (tipoVar.equals("float")) {
                escritor.printf("stack_%d = %s%n", detVar.offsetMemoria, valor);
            } else if (tipoVar.equals("boolean")) {
                escritor.printf("stack_%d = %s%n", detVar.offsetMemoria, valor);
            } else if (tipoVar.equals("char")) {
                escritor.printf("var_%s_char_%d = %s%n", nFuncAct, contChar, valor);
                contChar++;
            } else if (tipoVar.equals("String")) {
                escritor.printf("var_%s_String_%d = %s%n", nFuncAct, contString, valor);
                contString++;
            }
        } else {
            //El valor es nulo por lo que asigna el valor default
            if (tipoVar.equals("int")) {
                escritor.printf("stack_%d = 0n", detVar.offsetMemoria);
            } else if (tipoVar.equals("float")) {
                escritor.printf("stack_%d = 0.0n", detVar.offsetMemoria);
            } else if (tipoVar.equals("boolean")) {
                escritor.printf("stack_%d = false%n", detVar.offsetMemoria);
            } else if (tipoVar.equals("char")) {
                String label = String.format("var_%s_char_%d", nFuncAct, contChar);
                detVar.label = label;
                escritor.printf("%s = .space 1%n", label);
                contChar++;
            } else if (tipoVar.equals("String")) {
                String label = String.format("var_%s_String_%d", nFuncAct, contString);
                detVar.label = label;
                escritor.printf("%s = .asciiz \"\"%n", label);
                contString++;
            }
        }
        registroAct = 0;
        registroFloatAct = 0;
    }

    private String escribirValor(Object n) {
        if (n instanceof NodoValorInt) {
            return escribirValorInt((NodoValorInt) n);
        } else if (n instanceof NodoValorFloat) {
            return escribirValorFloat((NodoValorFloat) n);
        } else if (n instanceof Boolean) {
            if ((Boolean) n) {
                escritor.printf("t%d = 1%n", registroAct);
            } else {
                escritor.printf("t%d = 0%n", registroAct);
            }
            registroAct++;
            return "t" + (registroAct - 1);
        } else if (n instanceof Character) {
            String labelChar = String.format("var_%s_char_%d", nFuncAct, contChar);
            escritor.printf("%s = %c%n", labelChar, (Character) n);
            contChar++;
            return labelChar;
        } else if (n instanceof String) {
            String labelString = String.format("var_%s_String_%d", nFuncAct, contString);
            escritor.printf("%s = %s%n", labelString, (String) n);
            contString++;
            return labelString;
        } else if (n instanceof TipoIdentificador) {
            String idVar = ((TipoIdentificador) n).id;
            DetalleVariable detVar = tabla.getDetalleVariable(bloquesAlcanzables, idVar);
            //La variable es de tipo int/float
            if (detVar.label.equals("")) {
                String temporal;
                if (detVar.tipo.tipo.equals("float")) {
                    temporal = "f" + registroFloatAct;
                    registroFloatAct++;
                } else {
                    temporal = "t" + registroAct;
                    registroAct++;
                }
                escritor.printf("%s = stack_%d%n", temporal, detVar.offsetMemoria);
                return temporal;
            } else {
                //La variable es una arreglo/char/String por lo que se guarda el la memoria con un label
                return detVar.label;
            }
        } else if (n instanceof NodoLlamarFuncion) {
            return escribirLlamarFuncion((NodoLlamarFuncion) n);
        } else if (n instanceof NodoElemArreg) {
            String idVar = ((NodoElemArreg) n).id.id;
            DetalleVariable detVar = tabla.getDetalleVariable(bloquesAlcanzables, idVar);
            return String.format("%s[%d]", detVar.label, ((NodoElemArreg) n).posicion);
        } else if (n instanceof NodoExprBin) {
            return escribirExprBin((NodoExprBin) n);
        } else if (n instanceof NodoExprUna) {
            return escribirExprUna((NodoExprUna) n);
        } else if (n instanceof NodoExprRel) {
            return escribirExprRel((NodoExprRel) n);
        } else if (n instanceof NodoExprLog) {
            return escribirExprLog((NodoExprLog) n);
        }
        return null;
    }

    private String escribirValorInt(NodoValorInt n) {
        String valor = "";
        if (n.esEntradaEst) {
            valor = "call readInt, 0";
        } else {
            valor += n.valor;
        }
        String result = "t" + registroAct;
        escritor.printf("%s = %s%n", result, valor);
        registroAct++;
        return result;
    }

    private String escribirValorFloat(NodoValorFloat n) {
        String valor = "";
        if (n.esEntradaEst) {
            valor = "call readFloat, 0";
        } else {
            valor += n.valor;
        }
        String result = "f" + registroFloatAct;
        escritor.printf("%s = %s%n", result, valor);
        registroFloatAct++;
        return result;
    }

    private String escribirLlamarFuncion(NodoLlamarFuncion n) {
        DetalleFuncion detFunc = tabla.getDetalleFuncion(n.id.id);
        String tempResult = String.format("call %s, %d", n.id.id, detFunc.getCantParams());
        //Escribir parametros
        int registroOriginal = registroAct;
        NodoParametros nodoActual = n.parametros;
        for (int i = 0; i < detFunc.getCantParams(); i++) {
            //Obtiene el tipo del parametro
            String tipoParam = detFunc.parametros.get(i).tipo.tipo;
            if (detFunc.parametros.get(i).tipo.esArray) {
                tipoParam += "_array";
            }

            String regParam = escribirValor(nodoActual.parametro.valor);
            escritor.printf("param_%s = %s%n", tipoParam, regParam);

            nodoActual = nodoActual.parametros;
            registroAct = registroOriginal;
        }
        return tempResult;
    }

    private String escribirExprBin(NodoExprBin n) {
        String tempOp1 = escribirValor(n.operando1);
        String tempOp2 = escribirValor(n.operando2);
        String tempExpr;
        if (n.tipo.tipo.equals("int")) {
            tempExpr = "t" + registroAct;
            escritor.printf("%s = %s %s %s%n", tempExpr, tempOp1, n.operador.operador, tempOp2);
            registroAct++;
        } else {
            //Verifica si necesita mover un operando a un registro flotante
            if (tempOp1.startsWith("t")) {
                escritor.printf("f%d = %s%n", registroFloatAct, tempOp1);
                tempOp1 = "f" + registroFloatAct;
                registroFloatAct++;
            }
            if (tempOp2.startsWith("t")) {
                escritor.printf("f%d = %s%n", registroFloatAct, tempOp2);
                tempOp2 = "f" + registroFloatAct;
                registroFloatAct++;
            }
            tempExpr = "f" + registroFloatAct;
            escritor.printf("%s = %s %s %s%n", tempExpr, tempOp1, n.operador.operador, tempOp2);
            registroFloatAct++;

        }
        return tempExpr;
    }

    private String escribirExprUna(NodoExprUna n) {
        String tempOp = escribirValor(n.operando);
        String tempExpr;
        if (tempOp.startsWith("t")) {
            tempExpr = "t" + registroAct;
            registroAct++;
        } else {
            tempExpr = "f" + registroFloatAct;
            registroFloatAct++;
        }
        if (n.operador.operador.equals("-")) {
            escritor.printf("%s = - %s%n", tempExpr, tempOp);
        } else {
            escritor.printf("%s = %s %s%n", tempExpr, tempOp, n.operador.operador);
        }
        return tempExpr;
    }

    private String escribirExprRel(NodoExprRel n) {
        String tempOp1 = escribirValor(n.operando1);
        String tempOp2 = escribirValor(n.operando2);
        String tempExpr;
        if (tempOp1.startsWith("t")) {
            tempExpr = "t" + registroAct;
            registroAct++;
        } else {
            tempExpr = "f" + registroFloatAct;
            registroFloatAct++;
        }
        escritor.printf("%s = %s %s %s%n", tempExpr, tempOp1, n.operador.operador, tempOp2);
        return tempExpr;
    }

    private String escribirExprLog(NodoExprLog n) {
        String tempOp1 = escribirValor(n.operando1);
        String tempOp2 = escribirValor(n.operando2);
        //Verifica si necesita mover un operando a un registro entero
        if (tempOp1.startsWith("f")) {
            escritor.printf("t%d = %s%n", registroAct, tempOp1);
            tempOp1 = "t" + registroAct;
            registroAct++;
        }
        if (tempOp2.startsWith("f")) {
            escritor.printf("t%d = %s%n", registroAct, tempOp2);
            tempOp2 = "t" + registroAct;
            registroAct++;
        }
        String tempExpr = "t" + registroAct;
        escritor.printf("%s = %s %s %s%n", tempExpr, tempOp1, n.operador.operador, tempOp2);
        registroAct++;
        return tempExpr;
    }
}
