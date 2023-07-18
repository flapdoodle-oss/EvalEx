package com.ezylang.evalex.functionsx.trigonometric;

import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.functionsx.AbstractFunction;

public abstract class AbstractNumberFunction extends AbstractFunction.Single<Value.NumberValue> {
	public AbstractNumberFunction() {
		super(Value.NumberValue.class);
	}

	public AbstractNumberFunction(String name) {
		super(Value.NumberValue.class, name);
	}
}
