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
package com.ezylang.evalex.functions.trigonometric;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.SingleArgumentFunction;
import com.ezylang.evalex.parser.Token;

import java.util.List;

/** Returns the arc hyperbolic cotangent. */
public class AcotHFunction extends SingleArgumentFunction {
  @Override
  public EvaluationValue evaluate(
		VariableResolver variableResolver, Expression expression, Token functionToken, List<EvaluationValue> parameterValues) {

    /* Formula: acoth(x) = log((x + 1) / (x - 1)) * 0.5 */
    double value = parameterValues.get(0).getNumberValue().doubleValue();
    return expression.convertDoubleValue(Math.log((value + 1) / (value - 1)) * 0.5);
  }
}
