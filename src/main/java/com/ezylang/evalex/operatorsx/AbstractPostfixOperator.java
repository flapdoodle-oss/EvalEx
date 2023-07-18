package com.ezylang.evalex.operatorsx;

import com.ezylang.evalex.operators.OperatorType;
import com.ezylang.evalex.operators.Precedence;

public abstract class AbstractPostfixOperator extends AbstractBaseOperator implements PostfixOperator {
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
