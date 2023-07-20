package com.ezylang.evalex.operators;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.parser.Token;

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

	public static abstract class Typed<L extends Value<?>> extends AbstractPostfixOperator {
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
		public final Value<?> evaluate(Expression expression, Token operatorToken, Value<?> operand) throws EvaluationException {
			return evaluateTyped(expression, operatorToken, requireValueType(operatorToken, operand, type));
		}

		protected abstract Value<?> evaluateTyped(Expression expression, Token operatorToken, L operand) throws EvaluationException;
	}
}
