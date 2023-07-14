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

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpressionEvaluatorConstantsTest extends BaseExpressionEvaluatorTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "TRUE : true",
        "true : true",
        "False : false",
        "PI : "
            + " 3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679",
        "e : 2.71828182845904523536028747135266249775724709369995957496696762772407663",
      })
  void testDefaultConstants(String expression, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expression)).isEqualTo(expectedResult);
  }

  @Test
  void testCustomConstantsMixedCase() throws EvaluationException, ParseException {
    Map<String, EvaluationValue> constants =
        new HashMap<>() {
          {
            put("A", new EvaluationValue(new BigDecimal("2.5")));
            put("B", new EvaluationValue(new BigDecimal("3.9")));
          }
        };

    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().defaultConstants(constants).build();

    Expression expression = new Expression("a+B", configuration);

		assertThat(expression.evaluate(VariableResolver.empty()).getStringValue()).isEqualTo("6.4");
  }

  @Test
  void testOverwriteConstantsWith() throws EvaluationException, ParseException {
    Expression expression = new Expression("e");
		Expression expression1 = expression.withConstant("e", 9);
		assertThat(expression1.evaluate(VariableResolver.empty()).getStringValue()).isEqualTo("9");
  }

  @Test
  void testOverwriteConstantsWithValues() throws EvaluationException, ParseException {
    Map<String, Object> values = new HashMap<>();
    values.put("E", 6);
    Expression expression = new Expression("e");
		Expression expression1 = expression.withConstants(values);
		assertThat(expression1.evaluate(VariableResolver.builder().withValues(values).build()).getStringValue()).isEqualTo("6");
  }

  @Test
  void testOverwriteConstantsNotAllowed() {
    Expression expression =
        new Expression(
            "e", ExpressionConfiguration.builder().allowOverwriteConstants(false).build());
    assertThatThrownBy(() -> expression.withConstant("e", 9))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Can't set value for constant 'e'");
  }
}
