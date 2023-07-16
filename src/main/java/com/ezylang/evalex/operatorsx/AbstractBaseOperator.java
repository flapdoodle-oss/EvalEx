package com.ezylang.evalex.operatorsx;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.operators.OperatorType;
import com.ezylang.evalex.operators.Precedence;
import com.ezylang.evalex.parser.Token;

public abstract class AbstractBaseOperator implements Operator {

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

	protected static <T extends Value<?>> T requireValueType(Token operatorToken, Value<?> value, Class<T> type) throws EvaluationException {
		if (type.isInstance(value)) {
			return type.cast(value);
		}
		throw new EvaluationException(operatorToken, "type missmatch: "+value+" is not a "+type);
	}
}
