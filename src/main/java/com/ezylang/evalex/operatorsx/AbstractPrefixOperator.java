package com.ezylang.evalex.operatorsx;

import com.ezylang.evalex.operators.OperatorType;
import com.ezylang.evalex.operators.Precedence;

public abstract class AbstractPrefixOperator extends AbstractBaseOperator implements PrefixOperator {
	protected AbstractPrefixOperator(Precedence precedence, boolean leftAssociative) {
		super(OperatorType.PREFIX_OPERATOR, precedence, leftAssociative);
	}

	protected AbstractPrefixOperator(Precedence precedence) {
		super(OperatorType.PREFIX_OPERATOR, precedence);
	}
}
