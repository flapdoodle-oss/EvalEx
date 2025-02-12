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
package com.ezylang.evalex.operators.arithmetic;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.operators.AbstractInfixOperator;
import com.ezylang.evalex.operators.Precedence;
import com.ezylang.evalex.parser.Token;

import java.time.Duration;

/** Subtraction of two numbers. */
public class InfixMinusOperator extends AbstractInfixOperator {

  public InfixMinusOperator() {
    super(Precedence.OPERATOR_PRECEDENCE_ADDITIVE);
  }

  @Override
  public Value<?> evaluate(
      Expression expression, Token operatorToken, Value<?> leftOperand, Value<?> rightOperand)
      throws EvaluationException {

    return evaluate(operatorToken, leftOperand, rightOperand)
      .using(Value.NumberValue.class, Value.NumberValue.class,
        (l, r) -> Value.of(l.wrapped().subtract(r.wrapped(), expression.getConfiguration().getMathContext())))
      .using(Value.DateTimeValue.class, Value.DateTimeValue.class,
        (l, r) -> Value.of(Duration.ofMillis(l.wrapped().toEpochMilli() - r.wrapped().toEpochMilli())))
      .using(Value.DateTimeValue.class, Value.DurationValue.class,
        (l, r) -> Value.of(l.wrapped().minus(r.wrapped())))
      .using(Value.DurationValue.class, Value.DurationValue.class,
        (l, r) -> Value.of(l.wrapped().minus(r.wrapped())))
      // TODO remove this
      .using(Value.DateTimeValue.class, Value.NumberValue.class,
        (l, r) -> Value.of(l.wrapped().minus(Duration.ofMillis(r.wrapped().longValue()))))
      .get();
  }
}
