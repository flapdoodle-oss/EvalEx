package com.ezylang.evalex.data;

public interface VariableResolverX {
	Value<?> getData(String variable);

	static VariableResolverXBuilder builder() {
		return VariableResolverXBuilder.newInstance();
	}

	static VariableResolverX empty() {
		return variable -> null;
	}

	default VariableResolverX andThen(VariableResolverX fallback) {
		VariableResolverX that = this;
		return variable -> {
			Value<?> ret = that.getData(variable);
			return ret!=null ? ret : fallback.getData(variable);
		};
	}

	default boolean has(String name) {
		return getData(name) != null;
	}
}
