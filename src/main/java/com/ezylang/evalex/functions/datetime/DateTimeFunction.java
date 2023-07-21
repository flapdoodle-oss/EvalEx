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
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.functions.validations.NonNegativeNumberValidator;
import com.ezylang.evalex.parser.Token;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

// das ist eigentlich 3+vararg
public class DateTimeFunction extends AbstractFunction.SingleVararg<Value.NumberValue> {
  public DateTimeFunction() {
    super(FunctionParameterDefinition.varArgWith(Value.NumberValue.class,"values")
      .withValidators(new NonNegativeNumberValidator()));
  }
  @Override public Value<?> evaluateVarArg(VariableResolver variableResolver, Expression expression, Token functionToken,
    List<Value.NumberValue> parameterValues) {
    int year = parameterValues.get(0).wrapped().intValue();
    int month = parameterValues.get(1).wrapped().intValue();
    int day = parameterValues.get(2).wrapped().intValue();
    int hour = parameterValues.size() >= 4 ? parameterValues.get(3).wrapped().intValue() : 0;
    int minute = parameterValues.size() >= 5 ? parameterValues.get(4).wrapped().intValue() : 0;
    int second = parameterValues.size() >= 6 ? parameterValues.get(5).wrapped().intValue() : 0;
    int nanoOfs = parameterValues.size() >= 7 ? parameterValues.get(6).wrapped().intValue() : 0;

    ZoneId zoneId = expression.getConfiguration().getDefaultZoneId();
    return Value.of(
        LocalDateTime.of(year, month, day, hour, minute, second, nanoOfs)
            .atZone(zoneId)
            .toInstant());
  }
}
