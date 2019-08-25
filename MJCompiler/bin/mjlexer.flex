package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROG, yytext()); }
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"break" 		{ return new_symbol(sym.BREAK, yytext()); }
"enum" 		{ return new_symbol(sym.ENUM, yytext()); }
"const" 		{ return new_symbol(sym.CONST, yytext()); }
"new" 		{ return new_symbol(sym.NEW, yytext()); }
"read" 		{ return new_symbol(sym.READ, yytext()); }
"for" 		{ return new_symbol(sym.FOR, yytext()); }
"continue" 		{ return new_symbol(sym.CONTINUE, yytext()); }
"=" 		{ return new_symbol(sym.EQUAL, yytext()); }
";" 		{ return new_symbol(sym.SEMI, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"(" 		{ return new_symbol(sym.LPAREN, yytext()); }
")" 		{ return new_symbol(sym.RPAREN, yytext()); }
"{" 		{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }
"["			{ return new_symbol(sym.LBRACKET, yytext()); }
"]"			{ return new_symbol(sym.RBRACKET, yytext()); }
"=="			{ return new_symbol(sym.LOGICEQ, yytext()); }
"*" 		{ return new_symbol(sym.MULTIPLY, yytext()); }
"/"			{ return new_symbol(sym.DIV, yytext()); }
"%"			{ return new_symbol(sym.MODUO, yytext()); }
"!="			{ return new_symbol(sym.LOGICNEQ, yytext()); }
">"			{ return new_symbol(sym.GREATER, yytext()); }
">="			{ return new_symbol(sym.GREATEREQ, yytext()); }
"<"			{ return new_symbol(sym.LESSER, yytext()); }
"<="			{ return new_symbol(sym.LESSEREQ, yytext()); }
"&&"			{ return new_symbol(sym.LOGICAND, yytext()); }
"||"			{ return new_symbol(sym.LOGICOR, yytext()); }
"++"			{ return new_symbol(sym.INCREMENT, yytext()); }
"--"			{ return new_symbol(sym.DECREMENT, yytext()); }
"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-" 		{ return new_symbol(sym.MINUS, yytext()); }
"." 		{ return new_symbol(sym.DOT, yytext()); }

<YYINITIAL> "//" 		     { yybegin(COMMENT); }
<COMMENT> .      { yybegin(COMMENT); }
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

[0-9]+  { return new_symbol(sym.NUMBER, new Integer (yytext())); }
"true"|"false" { return new_symbol(sym.BOOLS,new Boolean(yytext().equals("true")? true : false )); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{return new_symbol (sym.IDENT, yytext()); }
"'"[\040-\176]"'" {return new_symbol (sym.CHARCONST, new Character (yytext().charAt(1)));}


. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)+" na poziciji "+yycolumn); }






