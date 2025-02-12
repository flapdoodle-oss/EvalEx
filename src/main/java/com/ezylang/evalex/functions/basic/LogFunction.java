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
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.functions.validations.NonNegativeNumberValidator;
import com.ezylang.evalex.functions.validations.NonZeroNumberValidator;
import com.ezylang.evalex.parser.Token;

/** The natural logarithm (base e) of a value */
public class LogFunction extends AbstractFunction.Single<Value.NumberValue> {

  public LogFunction() {
    super(FunctionParameterDefinition.of( Value.NumberValue.class,"value")
      .withValidators(new NonNegativeNumberValidator(), new NonZeroNumberValidator()));
  }
  @Override public Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, Value.NumberValue parameterValue) {
    double d = parameterValue.wrapped().doubleValue();

    return Value.of(Math.log(d));
  }
}
