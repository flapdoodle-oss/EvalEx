package com.ezylang.evalex.config;

import com.ezylang.evalex.functions.FunctionIfc;
import org.immutables.value.Value;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Value.Immutable
public abstract class MapBasedFunctionIfcResolver implements FunctionIfcResolver {
	protected abstract Map<String, FunctionIfc> functions();

	@Value.Lazy
	protected Map<String, String> lowerCaseToKey() {
		return functions().keySet().stream().collect(Collectors.toMap(String::toLowerCase, Function.identity()));
	}

	@Value.Auxiliary
	@Override
	public FunctionIfc getFunction(String functionName) {
		return functions().get(lowerCaseToKey().get(functionName.toLowerCase()));
	}

	public static ImmutableMapBasedFunctionIfcResolver.Builder builder() {
		return ImmutableMapBasedFunctionIfcResolver.builder();
	}
}
