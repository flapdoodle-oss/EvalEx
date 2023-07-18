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
package com.ezylang.evalex.functionsx.basic;

import com.ezylang.evalex.ExpressionX;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.functionsx.AbstractFunction;
import com.ezylang.evalex.functionsx.FunctionParameterDefinition;
import com.ezylang.evalex.functionsx.validations.NonNegativeNumberValidator;
import com.ezylang.evalex.functionsx.validations.NonZeroNumberValidator;

/** The base 10 logarithm of a value */
public class Log10Function extends AbstractFunction.Single<Value.NumberValue> {

  public Log10Function() {
    super(FunctionParameterDefinition.of(Value.NumberValue.class, "value")
      .withValidators(new NonZeroNumberValidator(), new NonNegativeNumberValidator()));
  }

  @Override public Value<?> evaluate(VariableResolverX variableResolver, ExpressionX expression, com.ezylang.evalex.parserx.Token functionToken,
    Value.NumberValue parameterValue) {
    double d = parameterValue.wrapped().doubleValue();
    
    return Value.of(Math.log10(d));
  }
}
