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
package com.ezylang.evalex.functionsx.datetime;

import com.ezylang.evalex.ExpressionX;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.functionsx.AbstractFunction;

import java.math.BigDecimal;
import java.time.Duration;

public class DurationFromDaysFunction extends AbstractFunction.Single<Value.NumberValue> {

  public DurationFromDaysFunction() {
    super(Value.NumberValue.class);
  }
  
  @Override public Value<?> evaluate(VariableResolverX variableResolver, ExpressionX expression, com.ezylang.evalex.parserx.Token functionToken,
    Value.NumberValue parameterValue) {
    BigDecimal days = parameterValue.wrapped();
    return Value.of(Duration.ofDays(days.longValue()));
  }
}
