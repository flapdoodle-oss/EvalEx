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
import com.ezylang.evalex.functionsx.FunctionParameterDefinition;
import com.ezylang.evalex.parserx.Token;

import java.math.BigDecimal;
import java.util.List;

/** Returns the sum value of all parameters. */
public class SumFunction extends AbstractFunction.SingleVararg<Value.NumberValue> {

  public SumFunction() {
    super(FunctionParameterDefinition.varArgWith(Value.NumberValue.class, "value"));
  }

  @Override public Value<?> evaluateVarArg(VariableResolverX variableResolver, ExpressionX expression, Token functionToken,
    List<Value.NumberValue> parameterValues) {
    BigDecimal sum = BigDecimal.ZERO;
    for (Value.NumberValue parameter : parameterValues) {
      sum = sum.add(parameter.wrapped(), expression.getConfiguration().getMathContext());
    }
    return Value.of(sum);
  }
}
