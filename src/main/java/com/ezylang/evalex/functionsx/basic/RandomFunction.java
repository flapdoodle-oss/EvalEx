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

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.ExpressionX;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.functionsx.Function;
import com.ezylang.evalex.functionsx.FunctionParameterDefinition;
import com.ezylang.evalex.parserx.Token;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

/** Random function produces a random value between 0 and 1. */
public class RandomFunction implements Function {

  @Override
  public List<FunctionParameterDefinition<?>> parameterDefinitions() {
    return Collections.emptyList();
  }

  @Override public Value<?> evaluate(VariableResolverX variableResolver, ExpressionX expression, Token functionToken, List<Value<?>> parameterValues)
    throws EvaluationException {
    SecureRandom secureRandom = new SecureRandom();

    return Value.of(secureRandom.nextDouble());
  }
}
