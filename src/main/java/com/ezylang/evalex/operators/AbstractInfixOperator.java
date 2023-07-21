package com.ezylang.evalex.operators;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.parser.Token;

import java.util.Optional;
import java.util.function.BiFunction;

public abstract class AbstractInfixOperator extends AbstractBaseOperator implements InfixOperator {
	protected AbstractInfixOperator(Precedence precedence, boolean leftAssociative) {
		super(OperatorType.INFIX_OPERATOR, precedence, leftAssociative);
	}

	protected AbstractInfixOperator(Precedence precedence) {
		super(OperatorType.INFIX_OPERATOR, precedence);
	}

	protected static <L extends Value<?>, R extends Value<?>> Optional<Value<?>> evaluate(Class<L> leftType, Class<R> rightType, Value<?> leftOperand, Value<?> rightOperand, BiFunction<L, R, Value<?>> function) {
		if (leftType.isInstance(leftOperand) && rightType.isInstance(rightOperand)) {
			return Optional.of(function.apply(leftType.cast(leftOperand), rightType.cast(rightOperand)));
		}
		return Optional.empty();
	}

	protected static Evaluator evaluate(Token operatorToken, Value<?> leftOperand, Value<?> rightOperand) {
		return new Evaluator(operatorToken, leftOperand, rightOperand);
	}

	public static class Evaluator {
		private final Token operatorToken;
		private final Value<?> leftOperand;
		private final Value<?> rightOperand;
		private Optional<Value<?>> result=Optional.empty();

		public Evaluator(Token operatorToken, Value<?> leftOperand, Value<?> rightOperand) {
			this.operatorToken = operatorToken;
			this.leftOperand = leftOperand;
			this.rightOperand = rightOperand;
		}

		public interface Evaluation<L,R> {
			Value<?> evaluate(L left, R right) throws EvaluationException;
		}

		public <L extends Value<?>, R extends Value<?>> Evaluator using(Class<L> leftType, Class<R> rightType, Evaluation<L, R> function)
			throws EvaluationException {
			if (result.isEmpty() && leftType.isInstance(leftOperand) && rightType.isInstance(rightOperand)) {
				result = Optional.of(function.evaluate(leftType.cast(leftOperand), rightType.cast(rightOperand)));
			}
			return this;
		}

		public Value<?> get() throws EvaluationException {
			if (!result.isPresent()) {
				throw new EvaluationException(operatorToken, "could not evaluate "+leftOperand+", "+rightOperand);
			}
			return result.get();
		}
	}

	public static abstract class Typed<L extends Value<?>, R extends Value<?>> extends AbstractInfixOperator {

		private final Class<L> leftType;
		private final Class<R> rightType;

		protected Typed(Precedence precedence, boolean leftAssociative, Class<L> leftType, Class<R> rightType) {
			super(precedence, leftAssociative);
			this.leftType = leftType;
			this.rightType = rightType;
		}

		protected Typed(Precedence precedence, Class<L> leftType, Class<R> rightType) {
			super(precedence);
			this.leftType = leftType;
			this.rightType = rightType;
		}

		@Override
		public final Value<?> evaluate(Expression expression, Token operatorToken, Value<?> leftOperand, Value<?> rightOperand) throws EvaluationException {
			return evaluateTyped(expression, operatorToken, requireValueType(operatorToken, leftOperand, leftType), requireValueType(operatorToken, rightOperand, rightType));
		}

		protected abstract Value<?> evaluateTyped(Expression expression, Token operatorToken, L leftOperand, R rightOperand) throws EvaluationException;
	}
}
