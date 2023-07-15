package com.ezylang.evalex.operators;

public abstract class AbstractPostfixOperator extends AbstractBaseOperator {
	protected AbstractPostfixOperator(Precedence precedence, boolean leftAssociative) {
		super(OperatorType.POSTFIX_OPERATOR, precedence, leftAssociative);
	}

	protected AbstractPostfixOperator(Precedence precedence) {
		super(OperatorType.POSTFIX_OPERATOR, precedence);
	}

	protected AbstractPostfixOperator() {
		super(OperatorType.POSTFIX_OPERATOR, Precedence.OPERATOR_PRECEDENCE_UNARY);
	}
}
