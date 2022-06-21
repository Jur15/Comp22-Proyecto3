package generacion;

import analisis.TablaSimbolos;
import analisis.arbolSintactico.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author moral
 */
public class Generador3D {

    private NodoPrograma arbol;
    private PrintWriter escritor;

    //Contadores y referencias
    private int espacioStack = 0, tempAct = 0,
            cVarInt = 0, cVarFloat = 0, cVarBoolean = 0, cVarChar = 0, cVarString = 0,
            cParams = 0,
            cIf = 0, cWhile = 0, cSwitch = 0;
    private String nFuncAct = "", nEstrucAct = "";

    public Generador3D(NodoPrograma arbol) {
        this.arbol = arbol;
    }

    public void generar() {
        try {
            FileWriter f = new FileWriter("/Salida/TresDirec.txt");
            escritor = new PrintWriter(f);
            //Recorre el arbol
            escribirPrograma(arbol);
            //Cierra el escritor
            escritor.close();
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
        TablaSimbolos.getInstancia().agregarFuncion(nFuncAct);
        escritor.printf("main_begin:%nstack_space numStack%n");
        escribirBloqueCod(n.bloqueCod);
        escritor.printf("exit_program%nmain_end%n");
        //Remplazar numStack por espacioStack
    }

    private void escribirFuncUsuario(NodoFuncUsuario n) {
        //Reinicia las variables
        espacioStack = 0;
        tempAct = 0;
        cVarInt = 0;
        cVarFloat = 0;
        cVarBoolean = 0;
        cVarChar = 0;
        cVarString = 0;
        cIf = 0;
        cWhile = 0;
        cSwitch = 0;
        cParams = 0;
        nFuncAct = "";
        nEstrucAct = "";

        escribirCrearFuncion(n.funcion);
        escritor.println();
        if (n.funcUsuario != null) {
            escribirFuncUsuario(n.funcUsuario);
        }
    }

    private void escribirCrearFuncion(NodoCrearFuncion n) {
        nFuncAct = n.id;
        TablaSimbolos.getInstancia().agregarFuncion(nFuncAct);
        escritor.printf("%s_begin:%n", nFuncAct);
        //Mover parametros a temp
        escribirBloqueCod(n.bloque);
        escritor.printf("%s_end", nFuncAct);
    }

    private void escribirBloqueCod(NodoBloqueCod n) {
        if (n.linea != null) {
            if (n.linea instanceof NodoSentencia) {
                escribirSentencia((NodoSentencia) n.linea);
            } else {
                escribirEstrucControl(n.linea);
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
            escribirAsignElemArreg((NodoAsignElemArreg) n.sentencia);
        } else if (n.sentencia instanceof NodoExprBin) {
            escribirExpBin((NodoExprBin) n.sentencia);
        } else if (n.sentencia instanceof NodoExprUna) {
            escribirExpUna((NodoExprUna) n.sentencia);
        } else if (n.sentencia instanceof NodoExprRel) {
            escribirExpRel((NodoExprRel) n.sentencia);
        } else if (n.sentencia instanceof NodoExprLog) {
            escribirExpLog((NodoExprLog) n.sentencia);
        } else if (n.sentencia instanceof NodoLlamarFuncion) {
            escribirLlamarFuncion((NodoLlamarFuncion) n.sentencia);
        } else if (n.sentencia instanceof NodoSalidaEst) {
            escribirSalidaEst((NodoSalidaEst) n.sentencia);
        } else if (n.sentencia instanceof NodoBreak) {
            escribirBreak((NodoBreak) n.sentencia);
        } else if (n.sentencia instanceof NodoReturn) {
            escribirReturn((NodoReturn) n.sentencia);
        }
    }

    private void escribirCrearVar(NodoCrearVar n) {
        switch (n.tipo) {
            case "int":
                if (n.esArray) {
                    espacioStack -= n.tamArray * 4;
                    TablaSimbolos.getInstancia().agregarVar(nFuncAct, n.id, n.tipo, true, nEstrucAct);
                } else {
                    espacioStack -= 4;
                    TablaSimbolos.getInstancia().agregarVar(nFuncAct, n.id, n.tipo);
                    escritor.printf("stack_0", args)
                }
        }
    }
}
