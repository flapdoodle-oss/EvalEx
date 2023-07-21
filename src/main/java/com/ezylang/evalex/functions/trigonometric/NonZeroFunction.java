package com.ezylang.evalex.functions.trigonometric;

import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.functions.validations.NonZeroNumberValidator;

public abstract class NonZeroFunction extends AbstractFunction.Single<Value.NumberValue> {
	public NonZeroFunction() {
		super(FunctionParameterDefinition.of(Value.NumberValue.class, "value").withValidators(new NonZeroNumberValidator()));
	}
}
