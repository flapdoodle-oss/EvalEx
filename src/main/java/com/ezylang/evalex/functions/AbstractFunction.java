package com.ezylang.evalex.functions;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractFunction implements FunctionIfc {
	private final List<FunctionParameterDefinition> parameterDefinitions;

	protected AbstractFunction(FunctionParameterDefinition definition, FunctionParameterDefinition ... definitions) {
		this.parameterDefinitions = Collections.unmodifiableList(Stream.concat(Stream.of(definition), Stream.of(definitions)).collect(Collectors.toList()));
		List<FunctionParameterDefinition> functionParameterDefinitions = this.parameterDefinitions;

		for (int i = 0; i < functionParameterDefinitions.size() -1; i++) {
			FunctionParameterDefinition it = functionParameterDefinitions.get(i);
			if (it.isVarArg()) {
				throw new IllegalArgumentException(
					"Only last parameter may be defined as variable argument");
			}
		}
	}

	@Override
	public List<FunctionParameterDefinition> parameterDefinitions() {
		return parameterDefinitions;
	}
}
