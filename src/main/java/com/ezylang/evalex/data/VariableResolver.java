package com.ezylang.evalex.data;

public interface VariableResolver {
	EvaluationValue getData(String variable);

	static VariableResolverBuilder builder() {
		return VariableResolverBuilder.newInstance();
	}

	static VariableResolver empty() {
		return variable -> null;
	}
}
