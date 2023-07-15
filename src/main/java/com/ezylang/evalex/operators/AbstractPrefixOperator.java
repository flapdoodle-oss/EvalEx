package com.ezylang.evalex.operators;

public abstract class AbstractPrefixOperator extends AbstractBaseOperator {
	protected AbstractPrefixOperator(Precedence precedence, boolean leftAssociative) {
		super(OperatorType.PREFIX_OPERATOR, precedence, leftAssociative);
	}

	protected AbstractPrefixOperator(Precedence precedence) {
		super(OperatorType.PREFIX_OPERATOR, precedence);
	}

	protected AbstractPrefixOperator() {
		super(OperatorType.PREFIX_OPERATOR, Precedence.OPERATOR_PRECEDENCE_UNARY);
	}

}
