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
package com.ezylang.evalex.functionsx.basic;

import com.ezylang.evalex.ExpressionX;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.functionsx.AbstractFunction;
import com.ezylang.evalex.parserx.Token;

import java.math.BigDecimal;

/** Factorial function, calculates the factorial of a base value. */
public class FactFunction extends AbstractFunction.Single<Value.NumberValue> {

  public FactFunction() {
    super(Value.NumberValue.class, "base");
  }

  @Override public Value<?> evaluate(VariableResolverX variableResolver, ExpressionX expression, Token functionToken, Value.NumberValue parameterValue) {
    int number = parameterValue.wrapped().intValue();
    BigDecimal factorial = BigDecimal.ONE;
    for (int i = 1; i <= number; i++) {
      factorial =
          factorial.multiply(
              new BigDecimal(i, expression.getConfiguration().getMathContext()),
              expression.getConfiguration().getMathContext());
    }
    return Value.of(factorial);
  }
}
