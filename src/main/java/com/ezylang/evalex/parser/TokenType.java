package com.ezylang.evalex.parser;

public enum TokenType {
	BRACE_OPEN,
	BRACE_CLOSE,
	COMMA,
	STRING_LITERAL,
	NUMBER_LITERAL,
	VARIABLE_OR_CONSTANT,
	INFIX_OPERATOR,
	PREFIX_OPERATOR,
	POSTFIX_OPERATOR,
	FUNCTION,
	FUNCTION_PARAM_START,
	ARRAY_OPEN,
	ARRAY_CLOSE,
	ARRAY_INDEX,
	STRUCTURE_SEPARATOR
}
