package com.ezylang.evalex.functions;

public abstract class SingleArgumentFunction extends AbstractFunction {

	protected SingleArgumentFunction(FunctionParameterDefinition definition) {
		super(definition);
	}

	protected SingleArgumentFunction(String name) {
		this(FunctionParameterDefinition.of(name));
	}

	protected SingleArgumentFunction() {
		this("value");
	}
}
