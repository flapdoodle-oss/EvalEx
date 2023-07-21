package com.ezylang.evalex.operators.booleans;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.operators.AbstractInfixOperator;
import com.ezylang.evalex.operators.Precedence;
import com.ezylang.evalex.parser.Token;

public abstract class AbstractInfixComparableOperator extends AbstractInfixOperator {

	protected AbstractInfixComparableOperator(Precedence precedence, boolean leftAssociative) {
		super(precedence, leftAssociative);
	}

	protected AbstractInfixComparableOperator(Precedence precedence) {
		super(precedence);
	}

	@Override
	public Value<?> evaluate(Expression expression, Token operatorToken, Value<?> leftOperand, Value<?> rightOperand) throws EvaluationException {
		if (leftOperand.getClass() == rightOperand.getClass()) {
			if (leftOperand instanceof Value.ComparableValue) {
				return evaluateComparable(expression, operatorToken, (Value.ComparableValue) leftOperand, (Value.ComparableValue) rightOperand);
			}
			throw new EvaluationException(operatorToken, "not comparable: "+leftOperand+", "+rightOperand);
		}
		throw new EvaluationException(operatorToken, "different types: "+leftOperand+", "+rightOperand);
	}

	protected abstract <T extends Comparable<T>, V extends Value.ComparableValue<T>> Value<?> evaluateComparable(Expression expression, Token operatorToken, V leftOperand, V rightOperand);
}
