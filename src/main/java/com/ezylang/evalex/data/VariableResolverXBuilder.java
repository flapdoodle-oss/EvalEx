package com.ezylang.evalex.data;

import com.ezylang.evalex.ImmutableExpressionX;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

public class VariableResolverXBuilder {
	private final Map<String, Value<?>> variables =
		new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	private VariableResolverXBuilder() {

	}

	public static VariableResolverXBuilder newInstance() {
		return new VariableResolverXBuilder();
	}

	public VariableResolverXBuilder with(String variable, Value<?> value) {
		Value<?> old = variables.put(variable, value);
		if (old!=null) {
			throw new IllegalArgumentException("was already set to "+old);
		}
		return this;
	}

	public VariableResolverXBuilder withNull(String variable) {
		return with(variable, Value.ofNull());
	}

	@Deprecated
	public VariableResolverXBuilder with(String variable, ValueMap value) {
		return with(variable, Value.of(value));
	}

	@Deprecated
	public VariableResolverXBuilder with(String variable, ValueArray value) {
		return with(variable, Value.of(value));
	}

	@Deprecated
	public VariableResolverXBuilder with(String variable, BigDecimal value) {
		return with(variable, Value.of(value));
	}

	@Deprecated
	public VariableResolverXBuilder with(String variable, double value) {
		return with(variable, Value.of(value));
	}

	@Deprecated
	public VariableResolverXBuilder with(String variable, String value) {
		return with(variable, Value.of(value));
	}

	@Deprecated
	public <T> VariableResolverXBuilder with(String variable, Function<T, Value<?>> mapper, Collection<T> collection) {
		return with(variable, Value.of(mapper, collection));
	}

	@Deprecated
	public <T> VariableResolverXBuilder with(String variable, Function<T, Value<?>> mapper, Map<String, T> collection) {
		return with(variable, Value.of(mapper, collection));
	}

	public VariableResolverXBuilder and(String variable, Value<?> value) {
		return with(variable, value);
	}

	public VariableResolverXBuilder withValues(Map<String, ? extends Value<?>> values) {
		for (Map.Entry<String, ? extends Value<?>> entry : values.entrySet()) {
			with(entry.getKey(), entry.getValue());
		}
		return this;
	}


	public VariableResolverX build() {
		Map<String, Value<?>> clone = new TreeMap<>(variables);
		return clone::get;
	}
}
