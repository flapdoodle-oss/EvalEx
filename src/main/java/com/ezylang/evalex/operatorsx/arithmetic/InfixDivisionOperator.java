/*
  Copyright 2012-2022 Udo Klimaschewski

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package com.ezylang.evalex.operatorsx.arithmetic;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.operators.Precedence;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;

/** Division of two numbers. */
public class InfixDivisionOperator extends
	com.ezylang.evalex.operatorsx.AbstractInfixOperator {

  public InfixDivisionOperator() {
    super(Precedence.OPERATOR_PRECEDENCE_MULTIPLICATIVE);
  }

  @Override
  public Value<?> evaluate(
      Expression expression, Token operatorToken, Value<?> leftOperand, Value<?> rightOperand)
      throws EvaluationException {
    Value.NumberValue left = requireValueType(operatorToken, leftOperand, Value.NumberValue.class);
    Value.NumberValue right = requireValueType(operatorToken, rightOperand, Value.NumberValue.class);

    if (right.wrapped().equals(BigDecimal.ZERO)) {
      throw new EvaluationException(operatorToken, "Division by zero");
    }

    return Value.of(left.wrapped().divide(right.wrapped(), expression.getConfiguration().getMathContext()));
  }
}
