package com.ezylang.evalex.operatorsx;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.ExpressionX;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.parserx.Token;

public interface PrefixOperator extends Operator {
	Value<?> evaluate(ExpressionX expression, Token operatorToken, Value<?> operand) throws EvaluationException;
}
