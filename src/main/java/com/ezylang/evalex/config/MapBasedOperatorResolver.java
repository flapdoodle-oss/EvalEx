package com.ezylang.evalex.config;

import com.ezylang.evalex.operators.AbstractInfixOperator;
import com.ezylang.evalex.operators.AbstractPostfixOperator;
import com.ezylang.evalex.operators.AbstractPrefixOperator;
import com.ezylang.evalex.operators.Operator;
import org.immutables.value.Value;

import java.util.Map;

@Value.Immutable
public abstract class MapBasedOperatorResolver implements OperatorResolver {

	protected abstract Map<String, AbstractInfixOperator> infixOperators();
	protected abstract Map<String, AbstractPrefixOperator> prefixOperators();
	protected abstract Map<String, AbstractPostfixOperator> postfixOperators();

	@Override
	public <T extends Operator> T getOperator(Class<T> type, String operatorString) {
		if (type.isAssignableFrom(AbstractInfixOperator.class)) {
			return type.cast(infixOperators().get(operatorString));
		}
		if (type.isAssignableFrom(AbstractPrefixOperator.class)) {
			return type.cast(prefixOperators().get(operatorString));
		}
		if (type.isAssignableFrom(AbstractPostfixOperator.class)) {
			return type.cast(postfixOperators().get(operatorString));
		}
		throw new IllegalArgumentException("operator type unknown: "+type+"("+operatorString+")");
	}

	public static ImmutableMapBasedOperatorResolver.Builder builder() {
		return ImmutableMapBasedOperatorResolver.builder();
	}

	public static ImmutableMapBasedOperatorResolver of(Map.Entry<String, Operator> ... operators) {
		ImmutableMapBasedOperatorResolver.Builder builder = ImmutableMapBasedOperatorResolver.builder();
		for (Map.Entry<String, Operator> entry : operators) {
			Operator value = entry.getValue();
			boolean foundType=false;
			if (value instanceof AbstractInfixOperator) {
				builder.putInfixOperators(entry.getKey(), (AbstractInfixOperator) value);
				foundType=true;
			}
			if (value instanceof AbstractPrefixOperator) {
				builder.putPrefixOperators(entry.getKey(), (AbstractPrefixOperator) value);
				foundType=true;
			}
			if (value instanceof AbstractPostfixOperator) {
				builder.putPostfixOperators(entry.getKey(), (AbstractPostfixOperator) value);
				foundType=true;
			}
			if (!foundType) {
				throw new IllegalArgumentException("unknown type: "+value);
			}
		}
		return builder.build();
	}
}
