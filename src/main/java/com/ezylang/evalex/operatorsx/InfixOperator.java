package com.ezylang.evalex.operatorsx;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.parser.Token;

public interface InfixOperator extends Operator {
	Value<?> evaluate(Expression expression, Token operatorToken, Value<?> leftOperand, Value<?> rightOperand) throws EvaluationException;
}
