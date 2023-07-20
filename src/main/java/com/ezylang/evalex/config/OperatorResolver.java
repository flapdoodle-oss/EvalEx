package com.ezylang.evalex.config;

import com.ezylang.evalex.operators.Operator;
import com.ezylang.evalex.operators.arithmetic.*;
import com.ezylang.evalex.operators.booleans.*;

public interface OperatorResolver {
	default <T extends Operator> boolean hasOperator(Class<T> type, String operatorString) {
		return getOperator(type, operatorString) != null;
	}

	<T extends Operator> T getOperator(Class<T> type, String operatorString);

	default OperatorResolver andThen(OperatorResolver fallback) {
		OperatorResolver that = this;

		return new OperatorResolver() {
			@Override
			public <T extends Operator> T getOperator(Class<T> type, String operatorString) {
				T operator = that.getOperator(type, operatorString);
				if (operator==null) {
					return fallback.getOperator(type, operatorString);
				}
				return operator;
			}
		};
	}

	static OperatorResolver defaults() {
		return MapBasedOperatorResolver.builder()
			// arithmetic
			.putPrefixOperators("+", new PrefixPlusOperator())
			.putPrefixOperators("-", new PrefixMinusOperator())
			.putInfixOperators("+", new InfixPlusOperator())
			.putInfixOperators("-", new InfixMinusOperator())
			.putInfixOperators("*", new InfixMultiplicationOperator())
			.putInfixOperators("/", new InfixDivisionOperator())
			.putInfixOperators("^", new InfixPowerOfOperator())
			.putInfixOperators("%", new InfixModuloOperator())
			// booleans
			.putInfixOperators("=", new InfixEqualsOperator())
			.putInfixOperators("==", new InfixEqualsOperator())
			.putInfixOperators("!=", new InfixNotEqualsOperator())
			.putInfixOperators("<>", new InfixNotEqualsOperator())
			.putInfixOperators(">", new InfixGreaterOperator())
			.putInfixOperators(">=", new InfixGreaterEqualsOperator())
			.putInfixOperators("<", new InfixLessOperator())
			.putInfixOperators("<=", new InfixLessEqualsOperator())
			.putInfixOperators("&&", new InfixAndOperator())
			.putInfixOperators("||", new InfixOrOperator())
			.putPrefixOperators("!", new PrefixNotOperator())
			.build();
	}
}
