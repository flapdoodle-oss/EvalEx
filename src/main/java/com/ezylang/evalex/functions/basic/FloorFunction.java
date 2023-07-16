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
package com.ezylang.evalex.functions.basic;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.SingleArgumentFunction;
import com.ezylang.evalex.parser.Token;

import java.math.RoundingMode;
import java.util.List;

/** Rounds the given value an integer using the rounding mode {@link RoundingMode#FLOOR} */
public class FloorFunction extends SingleArgumentFunction {
  @Override
  public EvaluationValue evaluate(
		VariableResolver variableResolver, Expression expression, Token functionToken, List<EvaluationValue> parameterValues) {

    EvaluationValue value = parameterValues.get(0);

    return EvaluationValue.of(value.getNumberValue().setScale(0, RoundingMode.FLOOR));
  }
}
