package com.ezylang.evalex.data;

public interface VariableResolverX {
	Value<?> getData(String variable);

	static VariableResolverXBuilder builder() {
		return VariableResolverXBuilder.newInstance();
	}

	static VariableResolverX empty() {
		return variable -> null;
	}
}
