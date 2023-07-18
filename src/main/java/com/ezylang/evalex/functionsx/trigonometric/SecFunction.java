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
package com.ezylang.evalex.functionsx.trigonometric;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.ExpressionX;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.parser.Token;

import java.util.List;

/** Returns the secant (in degrees). */
public class SecFunction extends NonZeroFunction {
	@Override public Value<?> evaluate(VariableResolverX variableResolver, ExpressionX expression, com.ezylang.evalex.parserx.Token functionToken,
		Value.NumberValue parameterValue) throws EvaluationException {
    /* Formula: sec(x) = 1 / cos(x) */
    return Value.of(1.0 / Math.cos(Math.toRadians(parameterValue.wrapped().doubleValue())));
  }
}
