/*
  Copyright 2012-2023 Udo Klimaschewski

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
package com.ezylang.evalex.functions.datetime;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.functions.SingleArgumentFunction;
import com.ezylang.evalex.parser.Token;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

public abstract class AbstractDateTimeParseFunction extends SingleArgumentFunction {
  protected AbstractDateTimeParseFunction() {
    super(FunctionParameterDefinition.varArgWith("value"));
  }
  
  @Override
  public EvaluationValue evaluate(
		VariableResolver variableResolver, Expression expression, Token functionToken, List<EvaluationValue> parameterValues) {
    ZoneId zoneId = expression.getConfiguration().getDefaultZoneId();
    Instant instant;

    if (parameterValues.size() < 2) {
      instant = parse(parameterValues.get(0).getStringValue(), null, zoneId);
    } else {
      instant =
          parse(parameterValues.get(0).getStringValue(), parameterValues.get(1).getStringValue(), zoneId);
    }
    return EvaluationValue.of(instant);
  }

  protected abstract Instant parse(String value, String format, ZoneId zoneId);
}
