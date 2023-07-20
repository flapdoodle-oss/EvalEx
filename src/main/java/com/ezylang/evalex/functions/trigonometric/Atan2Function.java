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

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.parser.Token;

/** Returns the angle of atan2 (in degrees). */
public class Atan2Function extends AbstractFunction.Tuple<Value.NumberValue, Value.NumberValue> {

	public Atan2Function() {
		super(FunctionParameterDefinition.of(Value.NumberValue.class, "y"), FunctionParameterDefinition.of(Value.NumberValue.class, "x"));
	}

	@Override public Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, Value.NumberValue y,
		Value.NumberValue x) throws EvaluationException {
    return Value.of(
        Math.toDegrees(
            Math.atan2(
                y.wrapped().doubleValue(),
                x.wrapped().doubleValue())));
  }
}
