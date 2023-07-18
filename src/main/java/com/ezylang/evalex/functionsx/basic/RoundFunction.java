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

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.ExpressionX;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.functionsx.AbstractFunction;
import com.ezylang.evalex.functionsx.FunctionParameterDefinition;
import com.ezylang.evalex.parserx.Token;

/**
 * Rounds the given value to the specified scale, using the {@link java.math.MathContext} of the
 * expression configuration.
 */
public class RoundFunction extends AbstractFunction.Tuple<Value.NumberValue, Value.NumberValue> {

  public RoundFunction() {
    super(FunctionParameterDefinition.of(Value.NumberValue.class, "value"),
      FunctionParameterDefinition.of(Value.NumberValue.class, "scale"));
  }

  @Override public Value<?> evaluate(VariableResolverX variableResolver, ExpressionX expression, Token functionToken, Value.NumberValue value,
    Value.NumberValue precision) throws EvaluationException {
    return Value.of(
        value
            .wrapped()
            .setScale(
                precision.wrapped().intValue(),
                expression.getConfiguration().getMathContext().getRoundingMode()));
  }
}
