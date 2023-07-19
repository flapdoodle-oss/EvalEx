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
package com.ezylang.evalex;

import com.ezylang.evalex.config.Configuration;
import com.ezylang.evalex.config.TestConfigurationXProvider;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.parserx.ParseException;

import java.math.BigDecimal;

public abstract class BaseExpressionEvaluatorTest {

  final Configuration configuration =
      TestConfigurationXProvider.StandardConfigurationWithAdditionalTestOperators;

  protected String evaluate(String expressionString) throws ParseException, EvaluationException {
    ExpressionX expression = createExpression(expressionString);
    return expression.evaluate(VariableResolverX.empty()).wrapped().toString();
  }

  protected String evaluate(String expressionString, VariableResolverX variableResolver) throws ParseException, EvaluationException {
    ExpressionX expression = createExpression(expressionString);
    return expression.evaluate(variableResolver).wrapped().toString();
  }

  ExpressionX createExpression(String expressionString) {
    return ExpressionX.of(expressionString, configuration);
  }

  protected static Value.NumberValue numberValueOf(String doubleAsString) {
    return Value.of(new BigDecimal(doubleAsString));
  }
}
