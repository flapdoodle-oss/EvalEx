package com.ezylang.evalex.operatorsx;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.parser.Token;

public interface PrefixOperator extends Operator {
	Value<?> evaluate(Expression expression, Token operatorToken, Value<?> operand) throws EvaluationException;
}
