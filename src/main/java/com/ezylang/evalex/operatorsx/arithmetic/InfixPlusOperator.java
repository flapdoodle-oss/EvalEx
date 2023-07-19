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
import com.ezylang.evalex.ExpressionX;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.operators.Precedence;
import com.ezylang.evalex.operatorsx.AbstractInfixOperator;
import com.ezylang.evalex.parserx.Token;

import java.time.Duration;

/**
 * Addition of numbers and strings. If one operand is a string, a string concatenation is performed.
 */
public class InfixPlusOperator extends AbstractInfixOperator {

  public InfixPlusOperator() {
    super(Precedence.OPERATOR_PRECEDENCE_ADDITIVE);
  }

  @Override public Value<?> evaluate(ExpressionX expression, Token operatorToken, Value<?> leftOperand, Value<?> rightOperand) throws EvaluationException {
    return evaluate(operatorToken, leftOperand, rightOperand)
      .using(Value.NumberValue.class, Value.NumberValue.class, (l, r) -> Value.of(l.wrapped().add(r.wrapped(), expression.getConfiguration().getMathContext())))
      .using(Value.DateTimeValue.class, Value.DurationValue.class, (l, r) -> Value.of(l.wrapped().plus(r.wrapped())))
      .using(Value.DurationValue.class, Value.DurationValue.class, (l, r) -> Value.of(l.wrapped().plus(r.wrapped())))
      .using(Value.DateTimeValue.class, Value.NumberValue.class, (l, r) -> Value.of(l.wrapped().plus(Duration.ofMillis(r.wrapped().longValue()))))
      .using(Value.class, Value.class, (l, r) -> Value.of(l.wrapped().toString()+r.wrapped()))
      .get();
  }
}
