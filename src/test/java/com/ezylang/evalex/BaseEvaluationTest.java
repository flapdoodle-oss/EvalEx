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
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.data.Percentage;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class BaseEvaluationTest {

  protected void assertExpressionHasExpectedResult(String expression, String expectedResult)
    throws EvaluationException, ParseException {
    assertThat(
      evaluate(
        expression,
        TestConfigurationXProvider.StandardConfigurationWithAdditionalTestOperators).wrapped().toString())
      .isEqualTo(expectedResult);
  }

  protected void assertExpressionHasExpectedResult(String expression, Value<?> expectedResult)
      throws EvaluationException, ParseException {
    assertThat(
            evaluate(
                    expression,
                    TestConfigurationXProvider.StandardConfigurationWithAdditionalTestOperators))
        .isEqualTo(expectedResult);
  }

  protected void assertExpressionHasExpectedResult(String expression, Value.NumberValue expectedResult)
          throws EvaluationException, ParseException {
    assertThat(
            evaluate(
                    expression,
                    TestConfigurationXProvider.StandardConfigurationWithAdditionalTestOperators))
            .isInstanceOf(Value.NumberValue.class)
            .extracting(Value::wrapped, InstanceOfAssertFactories.BIG_DECIMAL)
            .isCloseTo(expectedResult.wrapped(), Percentage.withPercentage(0.99999));
  }

  protected void assertExpressionHasExpectedResult(
    String expression, String expectedResult, Configuration expressionConfiguration)
    throws EvaluationException, ParseException {
    assertThat(evaluate(expression, expressionConfiguration).wrapped().toString())
      .isEqualTo(expectedResult);
  }

  protected void assertExpressionHasExpectedResult(
      String expression, Value<?> expectedResult, Configuration expressionConfiguration)
      throws EvaluationException, ParseException {
    assertThat(evaluate(expression, expressionConfiguration))
        .isEqualTo(expectedResult);
  }

  protected void assertExpressionThrowsException(
      String expression, String message, Configuration expressionConfiguration) {
    assertThatThrownBy(() -> evaluate(expression, expressionConfiguration)).hasMessage(message);
  }

  private Value<?> evaluate(String expressionString, Configuration configuration)
      throws EvaluationException, ParseException {
    ExpressionX expression = ExpressionX.of(expressionString, configuration);

    return expression.evaluate(VariableResolverX.empty());
  }

  protected static Value.NumberValue numberValueOf(String doubleAsString) {
    return Value.of(new BigDecimal(doubleAsString));
  }
}
