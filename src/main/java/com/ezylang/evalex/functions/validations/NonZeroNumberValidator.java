package com.ezylang.evalex.functions.validations;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;

public class NonZeroNumberValidator implements ParameterValidator {
	@Override public void validate(Token token, EvaluationValue parameterValue) throws EvaluationException {
		if (parameterValue.getNumberValue().equals(BigDecimal.ZERO)) {
			throw new EvaluationException(token, "Parameter must not be zero");
		}
	}
}
