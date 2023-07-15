package com.ezylang.evalex.functions.validations;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.Token;

public class NonNegativeNumberValidator implements ParameterValidator {
	@Override public void validate(Token token, EvaluationValue parameterValue) throws EvaluationException {
		if (parameterValue.getNumberValue().signum() < 0) {
			throw new EvaluationException(token, "Parameter must not be negative");
		}
	}
}
