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
package com.ezylang.evalex.functions.datetime;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.functions.SingleArgumentFunction;
import com.ezylang.evalex.parser.Token;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateTimeFormatFunction extends SingleArgumentFunction {

  public DateTimeFormatFunction() {
    super(FunctionParameterDefinition.varArgWith("value"));
  }

  @Override
  public EvaluationValue evaluate(
		VariableResolver variableResolver, Expression expression, Token functionToken, List<EvaluationValue> parameterValues) {
    String formatted;
    ZoneId zoneId = expression.getConfiguration().getDefaultZoneId();
    if (parameterValues.size() < 2) {
      formatted = parameterValues.get(0).getDateTimeValue().atZone(zoneId).toLocalDateTime().toString();
    } else {
      DateTimeFormatter formatter =
          DateTimeFormatter.ofPattern(parameterValues.get(1).getStringValue());
      formatted =
          parameterValues.get(0).getDateTimeValue().atZone(zoneId).toLocalDateTime().format(formatter);
    }
    return EvaluationValue.of(formatted);
  }
}
