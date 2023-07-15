package com.ezylang.evalex.functions.validations;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.Token;

public interface ParameterValidator {
	void validate(Token token, EvaluationValue parameterValue) throws EvaluationException;
}
