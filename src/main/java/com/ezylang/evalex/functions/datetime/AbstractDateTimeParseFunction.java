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
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.parser.Token;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

public abstract class AbstractDateTimeParseFunction extends AbstractFunction.SingleVararg<Value.StringValue> {
  protected AbstractDateTimeParseFunction() {
    super(FunctionParameterDefinition.varArgWith(Value.StringValue.class, "value"));
  }

  @Override public Value<?> evaluateVarArg(VariableResolver variableResolver, Expression expression, Token functionToken,
    List<Value.StringValue> parameterValues) {
    ZoneId zoneId = expression.getConfiguration().getDefaultZoneId();
    Instant instant;

    if (parameterValues.size() < 2) {
      instant = parse(parameterValues.get(0).wrapped(), null, zoneId);
    } else {
      instant =
          parse(parameterValues.get(0).wrapped(), parameterValues.get(1).wrapped(), zoneId);
    }
    return Value.of(instant);
  }

  protected abstract Instant parse(String value, String format, ZoneId zoneId);
}
