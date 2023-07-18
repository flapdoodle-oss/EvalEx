package com.ezylang.evalex.data;

import java.util.Map;
import java.util.TreeMap;

public class VariableResolverXBuilder {
	private final Map<String, Value<?>> variables =
		new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	private VariableResolverXBuilder() {

	}

	public static VariableResolverXBuilder newInstance() {
		return new VariableResolverXBuilder();
	}

	public VariableResolverXBuilder with(String variable, Value<?> value) {
		variables.put(variable, value);
		return this;
	}
	
	public VariableResolverXBuilder and(String variable, Value<?> value) {
		return with(variable, value);
	}

	public VariableResolverXBuilder withValues(Map<String, Value<?>> values) {
		for (Map.Entry<String, Value<?>> entry : values.entrySet()) {
			with(entry.getKey(), entry.getValue());
		}
		return this;
	}


	public VariableResolverX build() {
		Map<String, Value<?>> clone = new TreeMap<>(variables);
		return clone::get;
	}
}
