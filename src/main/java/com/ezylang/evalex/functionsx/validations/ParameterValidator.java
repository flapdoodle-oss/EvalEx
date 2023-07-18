package com.ezylang.evalex.functionsx.validations;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.parserx.Token;

public interface ParameterValidator<T extends Value<?>> {
	void validate(Token token, T parameterValue) throws EvaluationException;
}
