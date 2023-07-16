package com.ezylang.evalex.functionsx.validations;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.functionsx.validations.ParameterValidator;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;

public class NonZeroNumberValidator implements ParameterValidator<Value.NumberValue> {
	@Override public void validate(Token token, Value.NumberValue parameterValue) throws EvaluationException {
		if (parameterValue.wrapped().equals(BigDecimal.ZERO)) {
			throw new EvaluationException(token, "Parameter must not be zero");
		}
	}
}
