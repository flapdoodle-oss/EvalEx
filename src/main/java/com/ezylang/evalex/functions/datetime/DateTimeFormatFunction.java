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

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.parser.Token;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateTimeFormatFunction extends AbstractFunction {

	public DateTimeFormatFunction() {
		super(
			FunctionParameterDefinition.of(Value.DateTimeValue.class, "value"),
			FunctionParameterDefinition.optionalWith(Value.StringValue.class, "format")
		);
	}

	@Override public Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, List<Value<?>> parameterValues)
		throws EvaluationException {
		ZoneId zoneId = expression.getConfiguration().getDefaultZoneId();

		LocalDateTime dateTimeValue = ((Value.DateTimeValue) (parameterValues.get(0))).wrapped().atZone(zoneId).toLocalDateTime();

		String formatted;
		if (parameterValues.size() < 2) {
			formatted = dateTimeValue.toString();
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(((Value.StringValue) parameterValues.get(1)).wrapped());
			formatted = dateTimeValue.format(formatter);
		}
		return Value.of(formatted);
	}
}
