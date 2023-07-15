package com.ezylang.evalex.config;

import com.ezylang.evalex.operators.*;
import org.immutables.value.Value;

import java.util.Map;

@Value.Immutable
public abstract class MapBasedOperatorResolver implements OperatorResolver {

	protected abstract Map<String, AbstractInfixOperator> infixOperators();
	protected abstract Map<String, AbstractPrefixOperator> prefixOperators();
	protected abstract Map<String, AbstractPostfixOperator> postfixOperators();

	@Override
	public OperatorIfc getOperator(OperatorType type, String operatorString) {
		switch (type) {
			case INFIX_OPERATOR:
				return infixOperators().get(operatorString);
			case PREFIX_OPERATOR:
				return prefixOperators().get(operatorString);
			case POSTFIX_OPERATOR:
				return postfixOperators().get(operatorString);
		}
		throw new IllegalArgumentException("operator type unknown: "+type+"("+operatorString+")");
	}

	public static ImmutableMapBasedOperatorResolver.Builder builder() {
		return ImmutableMapBasedOperatorResolver.builder();
	}
}
