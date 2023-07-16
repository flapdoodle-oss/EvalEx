package com.ezylang.evalex.config;

import com.ezylang.evalex.operatorsx.*;
import com.ezylang.evalex.operatorsx.Operator;
import org.immutables.value.Value;

import java.util.Map;

@Value.Immutable
public abstract class MapBasedOperatorResolver implements OperatorResolver {

	protected abstract Map<String, AbstractInfixOperator> infixOperators();
	protected abstract Map<String, AbstractPrefixOperator> prefixOperators();
	protected abstract Map<String, AbstractPostfixOperator> postfixOperators();

	@Override
	public <T extends Operator> T getOperator(Class<T> type, String operatorString) {
		if (AbstractInfixOperator.class.isAssignableFrom(type)) {
			return type.cast(infixOperators().get(operatorString));
		}
		if (AbstractPrefixOperator.class.isAssignableFrom(type)) {
			return type.cast(prefixOperators().get(operatorString));
		}
		if (AbstractPostfixOperator.class.isAssignableFrom(type)) {
			return type.cast(postfixOperators().get(operatorString));
		}
		throw new IllegalArgumentException("operator type unknown: "+type+"("+operatorString+")");
	}

	public static ImmutableMapBasedOperatorResolver.Builder builder() {
		return ImmutableMapBasedOperatorResolver.builder();
	}
}
