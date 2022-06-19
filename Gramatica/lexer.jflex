package analisis;
import java_cup.runtime.*;
import analisis.excepcion.LexicalException;

%%

%public
%class Lexer
%unicode
%cup
%line

%{
	//Almacena el valor al leer strings
	StringBuffer string = new StringBuffer();

	//Fabricas de simbolos
	private Symbol symbol(int type) {
		return new ExtendedSymbol(type, yyline+1);
	}
	private Symbol symbol(int type, Object value) {
		return new ExtendedSymbol(type, yyline+1, value);
	}
%}

//----Declaraciones----
FinLinea = \n|\r|\r\n
EspacioBlanco = {FinLinea} | [ \t\f]

//Comentarios
CaracterComentario = [^\r\n]

Comentario = {ComentarioMulti} | {ComentarioLinea}
ComentarioMulti = "/*" [^*] ~"*/" | "/*" "*"+ "/"
ComentarioLinea = "//" {CaracterComentario}* {FinLinea}

//Identificadores
Identificador = [:jletter:][:jletterdigit:]*

//Enteros
Entero = 0 | {EnteroPositivo} | {EnteroNegativo}
EnteroPositivo = [1-9][0-9]*
EnteroNegativo = -{EnteroPositivo}

//Flotantes
Flotante = {FlotanteCero} | {FlotantePositivo} | {FlotanteNegativo}
FlotanteCero = 0 \. [0-9]+
FlotantePositivo = {EnteroPositivo} \. [0-9]+
FlotanteNegativo = - ({FlotanteCero}|{FlotantePositivo})

//Strings
CaracterString = [^\r\n\"\\]

//Chars
CaracterChar = [^\r\n\'\\]

//Estados adicionales
%state STRING, CHAR


%%
//----Reglas lexicas----

<YYINITIAL> {
	//Palabras clave
	"break"  	{return symbol(ParserSym.BREAK);}
	"case"   	{return symbol(ParserSym.CASE);}
	"default"  	{return symbol(ParserSym.DEFAULT);}
	"else"   	{return symbol(ParserSym.ELSE);}
	"if"     	{return symbol(ParserSym.IF);}
	"main"   	{return symbol(ParserSym.MAIN);}
	"null"		{return symbol(ParserSym.NULL);}
	"return" 	{return symbol(ParserSym.RETURN);}
	"switch" 	{return symbol(ParserSym.SWITCH);}
	"while"  	{return symbol(ParserSym.WHILE);}
	
	//Tipos
	"bool"		{return symbol(ParserSym.TIPO_BOOL);}
	"char"		{return symbol(ParserSym.TIPO_CHAR);}
	"float"		{return symbol(ParserSym.TIPO_FLOAT);}
	"int"		{return symbol(ParserSym.TIPO_INT);}
	"string"	{return symbol(ParserSym.TIPO_STRING);}
	
	//Entrada y salida estandar
	"readInt"	{return symbol(ParserSym.READ_INT);}
	"readFloat"	{return symbol(ParserSym.READ_FLOAT);}
	"print"		{return symbol(ParserSym.PRINT);}
	
	//Separadores
	"("			{return symbol(ParserSym.PAREN_A);}
	")"			{return symbol(ParserSym.PAREN_C);}
	"{"			{return symbol(ParserSym.CURS_A);}
	"}"			{return symbol(ParserSym.CURS_C);}
	"["			{return symbol(ParserSym.CUAD_A);}
	"]"			{return symbol(ParserSym.CUAD_C);}
	"#"			{return symbol(ParserSym.HASHTAG);}
	":"			{return symbol(ParserSym.DOSPUNTOS);}
	","			{return symbol(ParserSym.COMA);}
	
	//Operadores
	"="			{return symbol(ParserSym.IGUAL);}
	"+"			{return symbol(ParserSym.MAS);}
	"-"			{return symbol(ParserSym.MENOS);}
	"*"			{return symbol(ParserSym.POR);}
	"/"			{return symbol(ParserSym.DIV);}
	"^"			{return symbol(ParserSym.POTENCIA);}
	"~"			{return symbol(ParserSym.COMPLEMENTO);}
	"&"			{return symbol(ParserSym.AND);}
	"|"			{return symbol(ParserSym.OR);}
	"!"			{return symbol(ParserSym.NOT);}
	"=="		{return symbol(ParserSym.ESIGUAL);}
	"!="		{return symbol(ParserSym.DIFERENTE);}
	">"			{return symbol(ParserSym.MAYOR);}
	"<"			{return symbol(ParserSym.MENOR);}
	">="		{return symbol(ParserSym.MAYORIGUAL);}
	"<="		{return symbol(ParserSym.MENORIGUAL);}
	"++"		{return symbol(ParserSym.MASMAS);}
	"--"		{return symbol(ParserSym.MENOSMENOS);}
	
	//Literales
	//-Numeros
	{Entero}	{return symbol(ParserSym.LIT_ENTERO, Integer.valueOf(yytext()));}
	{Flotante}	{return symbol(ParserSym.LIT_FLOTANTE, Float.valueOf(yytext()));}
	
	//-Booleanos
	"true"		{return symbol(ParserSym.LIT_BOOLEANO, true);}
	"false"		{return symbol(ParserSym.LIT_BOOLEANO, false);}
	
	//-Chars (Cambio de seccion)
	\' 			{yybegin(CHAR);}
	
	//-Strings (Cambio de seccion)
	\" 			{yybegin(STRING); string.setLength(0);}
	
	//Identificador
	{Identificador} 	{return symbol(ParserSym.IDENTIF, yytext());}
	
	//Comentarios
	{Comentario}		{ /* Ignorar */ }
	
	//Espacio en blanco
	{EspacioBlanco}		{ /* Ignorar */ }
}

<STRING> {
	\"					{yybegin(YYINITIAL);
						 return symbol(ParserSym.LIT_STRING, string.toString());}
	{CaracterString}+	{string.append( yytext() );}
	
	//Secuencias de escape de caracteres
	"\\b"				{string.append( '\b' );}
	"\\t"				{string.append( '\t' );}
	"\\n"				{string.append( '\n' );}
	"\\f"				{string.append( '\f' );}
	"\\r"				{string.append( '\r' );}
	"\\\""				{string.append( '\"' );}
	"\\'"				{string.append( '\'' );}
	"\\\\"				{string.append( '\\' );}
	
	//Errores
	\\.	{throw new RuntimeException("Secuencia de escape ilegal <" + yytext() + ">");}
	{FinLinea}	{throw new RuntimeException("String incompleta al final de linea");}
}

<CHAR> {
	{CaracterChar}\'	{yybegin(YYINITIAL);
						 return symbol(ParserSym.LIT_CHAR, yytext().charAt(0));}
	
	//Secuencias de escape de caracteres
	"\\b"\'				{yybegin(YYINITIAL);
						 return symbol(ParserSym.LIT_CHAR, '\b');}
	"\\t"\'				{yybegin(YYINITIAL);
						 return symbol(ParserSym.LIT_CHAR, '\t');}
	"\\n"\'				{yybegin(YYINITIAL);
						 return symbol(ParserSym.LIT_CHAR, '\n');}
	"\\f"\'				{yybegin(YYINITIAL);
						 return symbol(ParserSym.LIT_CHAR, '\f');}
	"\\r"\'				{yybegin(YYINITIAL);
						 return symbol(ParserSym.LIT_CHAR, '\r');}
	"\\\""\'			{yybegin(YYINITIAL);
						 return symbol(ParserSym.LIT_CHAR, '\"');}
	"\\'"\'				{yybegin(YYINITIAL);
						 return symbol(ParserSym.LIT_CHAR, '\'');}	
	"\\\\"\'			{yybegin(YYINITIAL);
						 return symbol(ParserSym.LIT_CHAR, '\\');}

	//Errores
	\\.	{throw new RuntimeException("Secuencia de escape ilegal <" + yytext() + ">");}
	{FinLinea}	{throw new RuntimeException("Literal caracter incompleto al final de linea");}
}

//Error: caracter invalido
[^]		{throw new RuntimeException("Caracter ilegal <" + yytext() + "> en la linea " + (yyline+1));}

//Fin del archivo
<<EOF>>					{return symbol(ParserSym.EOF);}
