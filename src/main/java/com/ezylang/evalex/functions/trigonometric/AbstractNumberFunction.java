package com.ezylang.evalex.functions.trigonometric;

import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.functions.AbstractFunction;

public abstract class AbstractNumberFunction extends AbstractFunction.Single<Value.NumberValue> {
	public AbstractNumberFunction() {
		super(Value.NumberValue.class);
	}

	public AbstractNumberFunction(String name) {
		super(Value.NumberValue.class, name);
	}
}
