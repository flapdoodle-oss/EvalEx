package com.ezylang.evalex.config;

import org.immutables.value.Value;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Value.Immutable
public abstract class MapBasedFunctionResolver implements FunctionResolver {
	protected abstract Map<String, com.ezylang.evalex.functions.Function> functions();

	@Value.Lazy
	protected Map<String, String> lowerCaseToKey() {
		return functions().keySet().stream().collect(Collectors.toMap(String::toLowerCase, Function.identity()));
	}

	@Value.Auxiliary
	@Override
	public com.ezylang.evalex.functions.Function getFunction(String functionName) {
		return functions().get(lowerCaseToKey().get(functionName.toLowerCase()));
	}

	public static ImmutableMapBasedFunctionResolver of(Map.Entry<String, com.ezylang.evalex.functions.Function> ...entries) {
		ImmutableMapBasedFunctionResolver.Builder builder = ImmutableMapBasedFunctionResolver.builder();
		for (Map.Entry<String, com.ezylang.evalex.functions.Function> entry : entries) {
			builder.putFunctions(entry);
		}
		return builder.build();
	}

	public static ImmutableMapBasedFunctionResolver.Builder builder() {
		return ImmutableMapBasedFunctionResolver.builder();
	}
}
