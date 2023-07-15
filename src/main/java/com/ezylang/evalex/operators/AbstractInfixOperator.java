package com.ezylang.evalex.operators;

public abstract class AbstractInfixOperator extends AbstractBaseOperator {
	protected AbstractInfixOperator(Precedence precedence, boolean leftAssociative) {
		super(OperatorType.INFIX_OPERATOR, precedence, leftAssociative);
	}

	protected AbstractInfixOperator(Precedence precedence) {
		super(OperatorType.INFIX_OPERATOR, precedence);
	}
}
