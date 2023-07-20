package com.ezylang.evalex.functionsx.trigonometric;

import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.functionsx.AbstractFunction;
import com.ezylang.evalex.functionsx.FunctionParameterDefinition;
import com.ezylang.evalex.functionsx.validations.NonZeroNumberValidator;

public abstract class NonZeroFunction extends AbstractFunction.Single<Value.NumberValue> {
	public NonZeroFunction() {
		super(FunctionParameterDefinition.of(Value.NumberValue.class, "value").withValidators(new NonZeroNumberValidator()));
	}
}
