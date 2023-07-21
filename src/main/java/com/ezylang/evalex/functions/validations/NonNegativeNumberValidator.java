package com.ezylang.evalex.functions.validations;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.parser.Token;

public class NonNegativeNumberValidator implements ParameterValidator<Value.NumberValue> {
	@Override public void validate(Token token, Value.NumberValue parameterValue) throws EvaluationException {
		if (((Value.NumberValue) parameterValue).wrapped().signum() < 0) {
			throw new EvaluationException(token, "Parameter must not be negative");
		}
	}
}
