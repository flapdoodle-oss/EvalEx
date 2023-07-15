package com.ezylang.evalex.functions.trigonometric;

import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.functions.SingleArgumentFunction;
import com.ezylang.evalex.functions.validations.NonZeroNumberValidator;

public abstract class NonZeroFunction extends SingleArgumentFunction {
	public NonZeroFunction() {
		super(FunctionParameterDefinition.of("value").withValidators(new NonZeroNumberValidator()));
	}
}
