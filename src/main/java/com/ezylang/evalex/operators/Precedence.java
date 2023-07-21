package com.ezylang.evalex.operators;

public enum Precedence {
	/** Or operator precedence: || */
	OPERATOR_PRECEDENCE_OR(2),

	/** And operator precedence: && */
	OPERATOR_PRECEDENCE_AND(4),

	/** Equality operators precedence: =, ==, !=, <> */
	OPERATOR_PRECEDENCE_EQUALITY(7),

	/** Comparative operators precedence: <, >, <=, >= */
	OPERATOR_PRECEDENCE_COMPARISON(10),

	/** Additive operators precedence: + and - */
	OPERATOR_PRECEDENCE_ADDITIVE(20),

	/** Multiplicative operators precedence: *, /, % */
	OPERATOR_PRECEDENCE_MULTIPLICATIVE(30),

	/** Power operator precedence: ^ */
	OPERATOR_PRECEDENCE_POWER(40),

	/** Unary operators precedence: + and - as prefix */
	OPERATOR_PRECEDENCE_UNARY(60);
	private final int value;

	Precedence(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
