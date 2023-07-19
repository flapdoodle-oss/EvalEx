package com.ezylang.evalex.operatorsx;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.ExpressionX;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.operators.OperatorType;
import com.ezylang.evalex.operators.Precedence;
import com.ezylang.evalex.parserx.Token;

public abstract class AbstractPrefixOperator extends AbstractBaseOperator implements PrefixOperator {
	protected AbstractPrefixOperator(Precedence precedence, boolean leftAssociative) {
		super(OperatorType.PREFIX_OPERATOR, precedence, leftAssociative);
	}

	protected AbstractPrefixOperator(Precedence precedence) {
		super(OperatorType.PREFIX_OPERATOR, precedence);
	}

	public static abstract class Typed<L extends Value<?>> extends AbstractPrefixOperator {
		private final Class<L> type;

		protected Typed(Precedence precedence, boolean leftAssociative, Class<L> type) {
			super(precedence, leftAssociative);
			this.type = type;
		}

		protected Typed(Precedence precedence, Class<L> type) {
			super(precedence);
			this.type = type;
		}

		@Override
		public final Value<?> evaluate(ExpressionX expression, Token operatorToken, Value<?> operand) throws EvaluationException {
			return evaluateTyped(expression, operatorToken, requireValueType(operatorToken, operand, type));
		}

		protected abstract Value<?> evaluateTyped(ExpressionX expression, Token operatorToken, L operand) throws EvaluationException;
	}
}
