package com.ezylang.evalex.operators;

/**
 * The operator type.
 */
public enum OperatorType {
	/**
	 * Unary prefix operator, like -x
	 */
	PREFIX_OPERATOR,
	/**
	 * Unary postfix operator,like x!
	 */
	POSTFIX_OPERATOR,
	/**
	 * Binary infix operator, like x+y
	 */
	INFIX_OPERATOR
}
