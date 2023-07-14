package com.ezylang.evalex.data;

import java.util.Map;
import java.util.TreeMap;

public class VariableResolverBuilder {
	private final Map<String, EvaluationValue> variables =
		new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	private VariableResolverBuilder() {

	}

	public static VariableResolverBuilder newInstance() {
		return new VariableResolverBuilder();
	}

	public VariableResolverBuilder with(String variable, Object value) {
		variables.put(variable, new EvaluationValue(value));
		return this;
	}
	
	public VariableResolverBuilder and(String variable, Object value) {
		return with(variable, value);
	}

	public VariableResolverBuilder withValues(Map<String, ?> values) {
		for (Map.Entry<String, ?> entry : values.entrySet()) {
			with(entry.getKey(), entry.getValue());
		}
		return this;
	}


	public VariableResolver build() {
		Map<String, EvaluationValue> clone = new TreeMap<>(variables);
		return variable -> clone.get(variable);
	}
}
