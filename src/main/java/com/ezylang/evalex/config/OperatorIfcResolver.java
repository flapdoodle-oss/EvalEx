package com.ezylang.evalex.config;

import com.ezylang.evalex.operators.OperatorIfc;
import com.ezylang.evalex.operators.OperatorType;
import com.ezylang.evalex.operators.arithmetic.*;
import com.ezylang.evalex.operators.booleans.*;

public interface OperatorIfcResolver {
	default boolean hasOperator(OperatorType type, String operatorString) {
		return getOperator(type, operatorString) != null;
	}

	OperatorIfc getOperator(OperatorType type, String operatorString);

	default OperatorIfcResolver andThen(OperatorIfcResolver fallback) {
		OperatorIfcResolver that = this;

		return (type, operatorString) -> {
			OperatorIfc operator = that.getOperator(type, operatorString);
			if (operator==null) {
				return fallback.getOperator(type, operatorString);
			}
			return operator;
		};
	}

	static OperatorIfcResolver defaults() {
		return MapBasedOperatorIfcResolver.builder()
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
