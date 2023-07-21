package com.ezylang.evalex.data;

public interface VariableResolver {
	Value<?> getData(String variable);

	static VariableResolverBuilder builder() {
		return VariableResolverBuilder.newInstance();
	}

	static VariableResolver empty() {
		return variable -> null;
	}

	default VariableResolver andThen(VariableResolver fallback) {
		VariableResolver that = this;
		return variable -> {
			Value<?> ret = that.getData(variable);
			return ret!=null ? ret : fallback.getData(variable);
		};
	}

	default boolean has(String name) {
		return getData(name) != null;
	}
}
