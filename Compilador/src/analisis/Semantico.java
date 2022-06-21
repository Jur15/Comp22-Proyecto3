package analisis;

import informacion.TipoCompuesto;
import informacion.DetalleVariable;
import informacion.DetalleParametro;
import informacion.DetalleFuncion;
import analisis.arbolSintactico.*;
import analisis.excepcion.SemanticException;
import informacion.TipoIdentificador;
import java.util.ArrayList;

/**
 *
 * @author moral
 */
public class Semantico {

    public NodoPrograma arbol;
    public TablaSimbolos tabla;

    //Contadores y referencias
    private String funcAct, bloqueAct;
    private ArrayList<String> bloquesAlcanzables;
    private ArrayList<DetalleParametro> crearFunc_params;
    private int offsetMemoria, contadorIf, contadorWhile, contadorSwitch;

    public Semantico(NodoPrograma arbol, TablaSimbolos tabla) {
        this.arbol = arbol;
        this.tabla = tabla;
        reiniciarPropiedades(); //Inicia los contadores y referencias
    }

    private void reiniciarPropiedades() {
        funcAct = "";
        bloqueAct = "";
        bloquesAlcanzables = new ArrayList<>();
        crearFunc_params = new ArrayList<>();
        offsetMemoria = 0;
        contadorIf = 1;
        contadorWhile = 1;
        contadorSwitch = 1;
    }

    private void aumentarOffsetMemoria(TipoCompuesto tipoVar) {
        //Los arrays, strings y chars se almacenan en .data, no llevan offset
        String tipo = tipoVar.tipo;
        if (!tipoVar.esArray) {
            if (tipo.equals("String") || tipo.equals("char")) {
                return;
            }
            offsetMemoria += 4;
        }
    }

    public void validar() throws SemanticException {
        validarFuncMain(arbol.funcMain);
        if (arbol.funcUsuario != null) {
            validarFuncUsuario(arbol.funcUsuario);
        }
    }

    private void validarFuncMain(NodoFuncMain n) throws SemanticException {
        funcAct = "main";
        bloqueAct = "main";
        bloquesAlcanzables.add(bloqueAct);
        tabla.agregarBloque(bloqueAct);

        validarBloqueCod(n.bloqueCod);
    }

    private void validarFuncUsuario(NodoFuncUsuario n) throws SemanticException {
        validarCrearFuncion(n.funcion);
        if (n.funcUsuario != null) {
            validarFuncUsuario(n.funcUsuario);
        }
    }

    private void validarCrearFuncion(NodoCrearFuncion n) throws SemanticException {
        reiniciarPropiedades();
        funcAct = n.id.id;
        bloqueAct = funcAct;
        bloquesAlcanzables.add(bloqueAct);

        if (tabla.existeFuncion(funcAct)) {
            throw new SemanticException("La función " + funcAct + " ya está definida.");
        }
        if (n.parametros != null) {
            validarCrearParametros(n.parametros);
        }
        tabla.agregarBloque(funcAct);
        tabla.agregarFuncion(funcAct, n.tipo, crearFunc_params);
        validarBloqueCod(n.bloque);
    }

    private void validarCrearParametros(NodoCrearParametros n) throws SemanticException {
        validarCrearParametro(n.parametro);
        if (n.crearParametros != null) {
            validarCrearParametros(n.crearParametros);
        }
    }

    private void validarCrearParametro(NodoCrearParametro n) throws SemanticException {
        if (n.tipo.esArray) {
            if (n.tipo.tamArray < 0) {
                throw new SemanticException("El tamaño del array no puede ser negativo.");
            }
        }
        DetalleParametro param = new DetalleParametro(n.id, n.tipo);

        for (DetalleParametro d : crearFunc_params) {
            if (d.equals(param)) {
                throw new SemanticException("No pueden haber dos parametros iguales.");
            }
        }
        crearFunc_params.add(param);

    }

    private void validarBloqueCod(NodoBloqueCod n) throws SemanticException {
        if (n.linea != null) {
            if (n.linea instanceof NodoSentencia) {
                validarSentencia((NodoSentencia) n.linea);
            } else {
                validarEstrucControl(n.linea);
            }
        }
        if (n.bloqueCod != null) {
            validarBloqueCod(n.bloqueCod);
        }
    }

    //---Sentencias---
    private void validarSentencia(NodoSentencia n) throws SemanticException {
        if (n.sentencia instanceof NodoCrearVar) {
            validarCrearVar((NodoCrearVar) n.sentencia);
        } else if (n.sentencia instanceof NodoCrearAsignVar) {
            validarCrearAsignVar((NodoCrearAsignVar) n.sentencia);
        } else if (n.sentencia instanceof NodoAsignVar) {
            validarAsignVar((NodoAsignVar) n.sentencia);
        } else if (n.sentencia instanceof NodoAsignElemArreg) {
            validarAsignElemArreg((NodoAsignElemArreg) n.sentencia);
        } else if (n.sentencia instanceof NodoLlamarFuncion) {
            validarLlamarFuncion((NodoLlamarFuncion) n.sentencia);
        } else if (n.sentencia instanceof NodoSalidaEst) {
            validarSalidaEst((NodoSalidaEst) n.sentencia);
        } else if (n.sentencia instanceof NodoBreak) {
            validarBreak((NodoBreak) n.sentencia);
        } else if (n.sentencia instanceof NodoReturn) {
            validarReturn((NodoReturn) n.sentencia);
        } else if (n.sentencia instanceof NodoExprBin) {
            calcularTipoExprBin((NodoExprBin) n.sentencia);
        } else if (n.sentencia instanceof NodoExprUna) {
            calcularTipoExprUna((NodoExprUna) n.sentencia);
        } else if (n.sentencia instanceof NodoExprRel) {
            calcularTipoExprRel((NodoExprRel) n.sentencia);
        } else if (n.sentencia instanceof NodoExprLog) {
            calcularTipoExprLog((NodoExprLog) n.sentencia);
        }
    }

    private void validarCrearVar(NodoCrearVar n) throws SemanticException {
        String nombreVar = n.id.id;
        //Valida que la variable no este definida
        if (tabla.existeVar(bloquesAlcanzables, nombreVar)) {
            throw new SemanticException("La variable " + nombreVar + " ya está definida.");
        }
        //Valida que el tamaño del array sea valido
        if (n.tipo.esArray) {
            if (n.tipo.tamArray < 0) {
                throw new SemanticException("El tamaño del array no puede ser negativo.");
            }
        }
        //Agrega la variable a la tabla de simbolos
        tabla.agregarVar(bloqueAct, nombreVar, n.tipo, offsetMemoria);
        aumentarOffsetMemoria(n.tipo);
    }

    private void validarCrearAsignVar(NodoCrearAsignVar n) throws SemanticException {
        //Valida la creacion
        validarCrearVar(n.nodoCrear);
        DetalleVariable detVar = tabla.getDetalleVariable(bloquesAlcanzables, n.nodoCrear.id.id); //Obtiene el detalle de la variable
        //Valida que el tipo de la variable no sea un arreglo
        if (detVar.tipo.esArray) {
            throw new SemanticException("No se puede asignar un valor a una variable de tipo arreglo.");
        }
        //Valida que el tipo del valor sea el mismo que la variable
        TipoCompuesto tipoValor = calcularTipoValor(n.valor);
        if (tipoValor != null) {
            if (!n.nodoCrear.tipo.equals(tipoValor)) {
                throw new SemanticException("El tipo de la variable es diferente al del valor asignado.");
            }
            //Guarda el valor en la tabla de simbolos
            detVar.valor = n.valor;
        } else {
            //El valor es null por lo que guarda el valor default
            if (detVar.tipo.tipo.equals("int")) {
                detVar.valor = 0;
            } else if (detVar.tipo.tipo.equals("float")) {
                detVar.valor = 0.0f;
            } else if (detVar.tipo.tipo.equals("boolean")) {
                detVar.valor = 0;
            } else if (detVar.tipo.tipo.equals("char")) {
                detVar.valor = '0';
            } else if (detVar.tipo.tipo.equals("String")) {
                detVar.valor = "";
            }
        }

    }

    private void validarAsignVar(NodoAsignVar n) throws SemanticException {
        String nombreVar = n.id.id;
        if (tabla.existeVar(bloquesAlcanzables, nombreVar)) {
            DetalleVariable detVar = tabla.getDetalleVariable(bloquesAlcanzables, nombreVar); //Obtiene los detalles de la variable
            //Valida que el tipo de la variable no sea un arreglo
            if (detVar.tipo.esArray) {
                throw new SemanticException("No se puede asignar un valor a una variable de tipo arreglo.");
            }
            //Valida que el tipo del valor sea el mismo que la variable
            TipoCompuesto tipoValor = calcularTipoValor(n.valor);
            if (tipoValor != null) {
                if (!detVar.tipo.equals(tipoValor)) {
                    throw new SemanticException("El tipo de la variable es diferente al del valor asignado.");
                }
                //Guarda el valor en la tabla de simbolos
                detVar.valor = n.valor;
            } else {
                //El valor es null por lo que guarda el valor default
                if (detVar.tipo.tipo.equals("int")) {
                    detVar.valor = 0;
                } else if (detVar.tipo.tipo.equals("float")) {
                    detVar.valor = 0.0f;
                } else if (detVar.tipo.tipo.equals("boolean")) {
                    detVar.valor = 0;
                } else if (detVar.tipo.tipo.equals("char")) {
                    detVar.valor = '0';
                } else if (detVar.tipo.tipo.equals("String")) {
                    detVar.valor = "";
                }
            }

        } else {
            throw new SemanticException("La variable " + nombreVar + "no está definida.");
        }
    }

    private void validarAsignElemArreg(NodoAsignElemArreg n) throws SemanticException {
        //Valida que el arreglo exista y que el tipo del valor asignado sea el del arreglo
        TipoCompuesto tipoElemArreg = calcularTipoValor(n.elemento);
        TipoCompuesto tipoValor = calcularTipoValor(n.valor);
        boolean tipoNulo = tipoValor != null;
        if (!tipoNulo) {
            if (!tipoElemArreg.equals(tipoValor)) {
                throw new SemanticException("El tipo del valor es diferente al tipo del arreglo.");
            }
        }
        //Guarda el valor en la tabla de simbolos, en la posicion especificada
        DetalleVariable detVar = tabla.getDetalleVariable(bloquesAlcanzables, n.elemento.id.id);
        if (tipoNulo) {
            //El valor es null por lo que guarda el valor default
            if (tipoElemArreg.tipo.equals("int")) {
                ((ArrayList<Object>) detVar.valor).set(n.elemento.posicion, 0);
            } else {
                ((ArrayList<Object>) detVar.valor).set(n.elemento.posicion, '0');
            }
        } else {
            ((ArrayList<Object>) detVar.valor).set(n.elemento.posicion, n.valor);
        }

    }

    private void validarLlamarFuncion(NodoLlamarFuncion n) throws SemanticException {
        //Valida que la funcion exista
        String nombreFunc = n.id.id;
        if (tabla.existeFuncion(nombreFunc)) {
            //Valida los parametros dados con los definidos
            NodoParametros paramAct = n.parametros; //Enlace a parametro actual
            DetalleFuncion detFunc = tabla.getDetalleFuncion(nombreFunc);
            ArrayList<DetalleParametro> detParams = detFunc.parametros;
            //Por cada parametro definido en la tabla de simbolos...
            for (DetalleParametro paramDefinido : detParams) {
                //...valida que se envie un valor
                if (paramAct == null) {
                    throw new SemanticException("La cantidad de parametros no es correcta.");
                }
                //Valida que los parametros sean del mismo tipo
                TipoCompuesto t1 = paramDefinido.tipo;
                TipoCompuesto t2 = calcularTipoValor(paramAct.parametro.valor);
                if (!t1.equals(t2)) {
                    throw new SemanticException("El parametro no es del tipo esperado.");
                }
                paramAct = paramAct.parametros;
            }
            //Valida que no hayan parametros de más
            if (paramAct != null) {
                throw new SemanticException("La cantidad de parametros no es correcta.");
            }
        } else {
            throw new SemanticException("La funcion " + nombreFunc + " no está definida.");
        }
    }

    private void validarSalidaEst(NodoSalidaEst n) throws SemanticException {
        TipoCompuesto t = calcularTipoValor(n.valor);
        if (t.esArray) {
            throw new SemanticException("No se puede escribir un arreglo en la salida estándar.");
        }
        if (t.tipo.equals("boolean")) {
            throw new SemanticException("No se puede escribir un booleano en la salida estándar.");
        }
        //Guarda el tipo en el nodo
        n.tipo = t;
    }

    private void validarBreak(NodoBreak n) throws SemanticException {
        boolean adentroEstruc = false;
        //Verifica que exista una estructura while o switch entre los bloques alcanzables
        for (String bloque : bloquesAlcanzables) {
            if (bloque.contains("_while_") || bloque.contains("_switch_")) {
                adentroEstruc = true;
                n.bloqueRetorno = bloque;
            }
        }
        if (!adentroEstruc) {
            throw new SemanticException("Instruccion break fuera de while/switch.");
        }
    }

    private void validarReturn(NodoReturn n) throws SemanticException {
        DetalleFuncion detF = tabla.getDetalleFuncion(funcAct);
        String tipoEsperado = detF.tipo;
        TipoCompuesto tipoValor = calcularTipoValor(n.valor);
        //Valida el tipo del valor a retornar
        if (tipoValor.esArray) {
            throw new SemanticException("El tipo del valor de retorno no es válido.");
        }
        if (!tipoEsperado.equals(tipoValor.tipo)) {
            throw new SemanticException("El tipo del valor de retorno no es correcto para la funcion " + funcAct + ".");
        }
    }

    //---Estructuras de control---
    private void validarEstrucControl(NodoSintactico n) throws SemanticException {
        if (n instanceof NodoEstrucIf) {
            validarEstrucIf((NodoEstrucIf) n);
        } else if (n instanceof NodoEstrucIfElse) {
            validarEstrucIfElse((NodoEstrucIfElse) n);
        } else if (n instanceof NodoEstrucWhile) {
            validarEstrucWhile((NodoEstrucWhile) n);
        } else if (n instanceof NodoEstrucSwitch) {
            validarEstrucSwitch((NodoEstrucSwitch) n);
        }
    }

    private void validarCondicion(NodoCondicion n) throws SemanticException {
        TipoCompuesto t = calcularTipoValor(n.condicion);
        if (!t.tipo.equals("boolean")) {
            throw new SemanticException("El valor de una condicion de estructura de control debe ser de tipo booleano.");
        }
    }

    private void validarEstrucIf(NodoEstrucIf n) throws SemanticException {
        //Valida la condicion
        validarCondicion(n.condicion);
        //Crea el nuevo bloque de memoria y aumenta el contador
        String bloqueAnterior = bloqueAct;
        String nombreBloque = funcAct + "_if_" + contadorIf;
        contadorIf++;
        bloqueAct = nombreBloque;
        bloquesAlcanzables.add(nombreBloque);
        tabla.agregarBloque(nombreBloque);
        //Valida el bloque de codigo
        validarBloqueCod(n.bloque);
        //Remueve el bloque de memoria y restaura el bloque anterior
        bloquesAlcanzables.remove(nombreBloque);
        bloqueAct = bloqueAnterior;
    }

    private void validarEstrucIfElse(NodoEstrucIfElse n) throws SemanticException {
        //Valida la condicion
        validarCondicion(n.condicion);

        String bloqueAnterior = bloqueAct;
        String nombreBloque = funcAct + "_if_" + contadorIf;
        contadorIf++;
        //---Bloque If---
        //Crea el nuevo bloque de memoria y aumenta el contador
        bloqueAct = nombreBloque;
        bloquesAlcanzables.add(nombreBloque);
        tabla.agregarBloque(nombreBloque);
        //Valida el bloque de codigo
        validarBloqueCod(n.bloqueIf);
        //Remueve el bloque de memoria
        bloquesAlcanzables.remove(nombreBloque);

        //---Bloque Else---
        //Crea el nuevo bloque de memoria
        nombreBloque += "_else";
        bloqueAct = nombreBloque;
        bloquesAlcanzables.add(nombreBloque);
        tabla.agregarBloque(nombreBloque);
        //Valida el bloque de codigo
        validarBloqueCod(n.bloqueElse);
        //Remueve el bloque de memoria y restaura el bloque anterior
        bloquesAlcanzables.remove(nombreBloque);
        bloqueAct = bloqueAnterior;
    }

    private void validarEstrucWhile(NodoEstrucWhile n) throws SemanticException {
        //Valida la condicion
        validarCondicion(n.condicion);
        //Crea el nuevo bloque de memoria y aumenta el contador
        String bloqueAnterior = bloqueAct;
        String nombreBloque = funcAct + "_while_" + contadorWhile;
        contadorWhile++;
        bloqueAct = nombreBloque;
        bloquesAlcanzables.add(nombreBloque);
        tabla.agregarBloque(nombreBloque);
        //Valida el bloque de codigo
        validarBloqueCod(n.bloque);
        //Remueve el bloque de memoria y restaura el bloque anterior
        bloquesAlcanzables.remove(nombreBloque);
        bloqueAct = bloqueAnterior;
    }

    private void validarEstrucSwitch(NodoEstrucSwitch n) throws SemanticException {
        //Crea el nuevo bloque de memoria y aumenta el contador
        String bloqueAnterior = bloqueAct;
        String nombreBloque = funcAct + "_switch_" + contadorSwitch;
        contadorSwitch++;
        bloqueAct = nombreBloque;
        bloquesAlcanzables.add(nombreBloque);
        tabla.agregarBloque(nombreBloque);

        //Valida que el tipo del identificador sea igual al de los casos
        TipoCompuesto tipoIdent = calcularTipoVar(n.id.id);
        if (tipoIdent.esArray) {
            throw new SemanticException("No se puede utilizar un arreglo en una estructura switch.");
        }
        if (!n.tipo.equals(tipoIdent.tipo)) {
            throw new SemanticException("El tipo de la variable no es igual al de los casos del switch.");
        }

        //Validar los casos
        if (n.casos instanceof NodoCasosInt) {
            validarCasosInt((NodoCasosInt) n.casos);
        } else if (n.casos instanceof NodoCasosFloat) {
            validarCasosFloat((NodoCasosFloat) n.casos);
        } else if (n.casos instanceof NodoCasosBool) {
            validarCasosBool((NodoCasosBool) n.casos);
        } else if (n.casos instanceof NodoCasosChar) {
            validarCasosChar((NodoCasosChar) n.casos);
        } else if (n.casos instanceof NodoCasosString) {
            validarCasosString((NodoCasosString) n.casos);
        }
        //Validar el caso default
        validarCasoDefault(n.casoDefault);

        //Remueve el bloque de memoria y restaura el bloque anterior
        bloquesAlcanzables.remove(nombreBloque);
        bloqueAct = bloqueAnterior;

    }

    private void validarCasosInt(NodoCasosInt n) throws SemanticException {
        validarBloqueCod(n.bloque);
        //Valida los demas casos
        if (n.nodoCasos != null) {
            validarCasosInt(n.nodoCasos);
        }
    }

    private void validarCasosFloat(NodoCasosFloat n) throws SemanticException {
        validarBloqueCod(n.bloque);
        //Valida los demas casos
        if (n.nodoCasos != null) {
            validarCasosFloat(n.nodoCasos);
        }
    }

    private void validarCasosBool(NodoCasosBool n) throws SemanticException {
        validarBloqueCod(n.bloque);
        //Valida los demas casos
        if (n.nodoCasos != null) {
            validarCasosBool(n.nodoCasos);
        }
    }

    private void validarCasosChar(NodoCasosChar n) throws SemanticException {
        validarBloqueCod(n.bloque);
        //Valida los demas casos
        if (n.nodoCasos != null) {
            validarCasosChar(n.nodoCasos);
        }
    }

    private void validarCasosString(NodoCasosString n) throws SemanticException {
        validarBloqueCod(n.bloque);
        //Valida los demas casos
        if (n.nodoCasos != null) {
            validarCasosString(n.nodoCasos);
        }
    }

    private void validarCasoDefault(NodoCasoDefault n) throws SemanticException {
        validarBloqueCod(n.bloque);
    }

    //---Valores---
    private TipoCompuesto calcularTipoValor(Object n) throws SemanticException {
        if (n == null) {
            return null;
        }
        if (n instanceof NodoValorInt) {
            return new TipoCompuesto("int");
        } else if (n instanceof NodoValorFloat) {
            return new TipoCompuesto("float");
        } else if (n instanceof Boolean) {
            return new TipoCompuesto("boolean");
        } else if (n instanceof Character) {
            return new TipoCompuesto("char");
        } else if (n instanceof String) {
            return new TipoCompuesto("String");
        } else if (n instanceof TipoIdentificador) {
            String idVar = ((TipoIdentificador) n).id;
            return calcularTipoVar(idVar);
        } else if (n instanceof NodoLlamarFuncion) {
            String idFunc = ((NodoLlamarFuncion) n).id.id;
            String tipoFunc = tabla.getDetalleFuncion(idFunc).tipo;
            return new TipoCompuesto(tipoFunc);
        } else if (n instanceof NodoElemArreg) {
            String idVar = ((NodoElemArreg) n).id.id;
            TipoCompuesto tipoArreglo = calcularTipoVar(idVar);
            //Verifica que la variable sea un arreglo
            if (!tipoArreglo.esArray) {
                throw new SemanticException("La variable " + idVar + " no es un arreglo.");
            }
            //Verifica que el indice este en el rango valido
            int indice = ((NodoElemArreg) n).posicion;
            if (indice < 0) {
                throw new SemanticException("Indice negativo para elemento de arreglo.");
            }
            if (indice >= tipoArreglo.tamArray) {
                throw new SemanticException("Indice fuera del rango del arreglo " + idVar + ".");
            }
            return new TipoCompuesto(tipoArreglo.tipo);
        } else if (n instanceof NodoExprBin) {
            return calcularTipoExprBin((NodoExprBin) n);
        } else if (n instanceof NodoExprUna) {
            return calcularTipoExprUna((NodoExprUna) n);
        } else if (n instanceof NodoExprRel) {
            return calcularTipoExprRel((NodoExprRel) n);
        } else if (n instanceof NodoExprLog) {
            return calcularTipoExprLog((NodoExprLog) n);
        }
        return null;
    }

    private TipoCompuesto calcularTipoVar(String id) throws SemanticException {
        if (tabla.existeVar(bloquesAlcanzables, id)) {
            return tabla.getDetalleVariable(bloquesAlcanzables, id).tipo;
        } else {
            throw new SemanticException("La variable " + id + " no está definida.");
        }
    }

    private TipoCompuesto calcularTipoExprBin(NodoExprBin n) throws SemanticException {
        //Retorna el tipo de la expresion si ya fue calculado antes
        if (n.tipo != null) {
            return n.tipo;
        }
        TipoCompuesto result = new TipoCompuesto("int");
        //Obtiene y valida el tipo de los operandos
        TipoCompuesto tipo1 = calcularTipoValor(n.operando1);
        TipoCompuesto tipo2 = calcularTipoValor(n.operando2);
        if (!validarTipoOperandoBin(tipo1)) {
            throw new SemanticException("El tipo del operando no es válido para una expresión binaria.");
        }
        if (!validarTipoOperandoBin(tipo2)) {
            throw new SemanticException("El tipo del operando no es válido para una expresión binaria.");
        }
        //Calcula el tipo resultante
        if (tipo1.equals(tipo2)) {
            result = tipo1;
        } else if (tipo1.tipo.equals("float") || tipo2.tipo.equals("float")) {
            result = new TipoCompuesto("float");
        }
        //Guarda el tipo calculado en la expresion
        n.tipo = result;
        return result;
    }

    private boolean validarTipoOperandoBin(TipoCompuesto t) {
        if (t.esArray) {
            return false;
        }
        if (t.tipo.equals("int") || t.tipo.equals("float")) {
            return true;
        }
        return false;
    }

    private TipoCompuesto calcularTipoExprUna(NodoExprUna n) throws SemanticException {
        //Retorna el tipo de la expresion si ya fue calculado antes
        if (n.tipo != null) {
            return n.tipo;
        }
        TipoCompuesto result;
        //Obtiene y valida el tipo del operando
        TipoCompuesto tipo = calcularTipoValor(n.operando);
        if (tipo.esArray) {
            throw new SemanticException("El tipo del operando no es válido para una expresión unaria.");
        }
        if (tipo.tipo.equals("int")) {
            result = tipo;
        } else if (tipo.tipo.equals("float") & n.operador.operador.equals("-")) {
            result = tipo;
        } else {
            throw new SemanticException("El tipo del operando no es válido para una expresión unaria.");
        }
        //Guarda el tipo calculado en la expresion
        n.tipo = result;
        return result;
    }

    private TipoCompuesto calcularTipoExprRel(NodoExprRel n) throws SemanticException {
        //Obtiene y valida el tipo de los operandos
        TipoCompuesto tipo1 = calcularTipoValor(n.operando1);
        TipoCompuesto tipo2 = calcularTipoValor(n.operando2);
        //Valida que los operandos sean del mismo tipo
        if (!tipo1.equals(tipo2)) {
            throw new SemanticException("Los operandos de la expresión relacional no son del mismo tipo.");
        }
        //Valida que los operandos sean del tipo aceptado para la operacion
        boolean modoIgualDiferente = (n.operador.operador.equals("==") || n.operador.operador.equals("!="));
        if (!validarTipoOperandoRel(tipo1, modoIgualDiferente)) {
            throw new SemanticException("El tipo de los operandos no es válido para una expresión relacional.");
        }
        return new TipoCompuesto("boolean");
    }

    private boolean validarTipoOperandoRel(TipoCompuesto t, boolean modoIgualDiferente) {
        if (t.esArray) {
            return false;
        }
        if (t.tipo.equals("int") || t.tipo.equals("float")) {
            return true;
        }
        if (t.tipo.equals("boolean") & modoIgualDiferente) {
            return true;
        }
        return false;
    }

    private TipoCompuesto calcularTipoExprLog(NodoExprLog n) throws SemanticException {
        //Obtiene y valida el tipo de los operandos
        TipoCompuesto tipo1 = calcularTipoValor(n.operando1);
        TipoCompuesto tipo2 = calcularTipoValor(n.operando2);
        //Valida que los operandos sean del mismo tipo
        if (!tipo1.equals(tipo2)) {
            throw new SemanticException("Los operandos de la expresión lógica no son del mismo tipo.");
        }
        //Valida que los operandos sean de tipo booleano
        if (!tipo1.tipo.equals("boolean")) {
            throw new SemanticException("El tipo de los operandos no es válido para una expresión lógica.");
        }
        return tipo1;
    }
}
