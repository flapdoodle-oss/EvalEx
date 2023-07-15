package com.ezylang.evalex.operators;

public abstract class AbstractBaseOperator implements OperatorIfc {

	private final OperatorType type;
	private final int precedence;
	private final boolean leftAssociative;

	protected AbstractBaseOperator(OperatorType type, int precedence, boolean leftAssociative) {
		this.type = type;
		this.precedence = precedence;
		this.leftAssociative = leftAssociative;
	}

	protected AbstractBaseOperator(OperatorType type, Precedence precedence, boolean leftAssociative) {
		this(type, precedence.value(), leftAssociative);
	}

	protected AbstractBaseOperator(OperatorType type, Precedence precedence) {
		this(type, precedence, true);
	}

	@Override
	public OperatorType type() {
		return type;
	}

	@Override
	public int getPrecedence() {
		return precedence;
	}

	@Override
	public boolean isLeftAssociative() {
		return leftAssociative;
	}

}
