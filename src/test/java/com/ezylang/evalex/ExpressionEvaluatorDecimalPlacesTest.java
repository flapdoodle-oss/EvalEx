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
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExpressionEvaluatorDecimalPlacesTest extends BaseExpressionEvaluatorTest {

  @Test
  void testDefaultNoRoundingLiteral() throws ParseException, EvaluationException {
    assertThat(evaluate("2.12345")).isEqualTo("2.12345");
  }

  @Test
  void testDefaultNoRoundingVariable() throws ParseException, EvaluationException {
    Expression expression1 = new Expression("a");
    new BigDecimal("2.12345");
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", new BigDecimal("2.12345"))
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("2.12345");
  }

  @Test
  void testDefaultNoRoundingInfixOperator() throws ParseException, EvaluationException {
    assertThat(evaluate("2.12345+1.54321")).isEqualTo("3.66666");
  }

  @Test
  void testDefaultNoRoundingPrefixOperator() throws ParseException, EvaluationException {
    assertThat(evaluate("-2.12345")).isEqualTo("-2.12345");
  }

  @Test
  void testDefaultNoRoundingFunction() throws ParseException, EvaluationException {
    assertThat(evaluate("SUM(2.12345,1.54321)")).isEqualTo("3.66666");
  }

  @Test
  void testDefaultNoRoundingArray() throws ParseException, EvaluationException {
    List<BigDecimal> array = List.of(new BigDecimal("1.12345"));
    Expression expression1 = createExpression("a[0]");
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", array)
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("1.12345");
  }

  @Test
  void testDefaultNoRoundingStructure() throws ParseException, EvaluationException {
    Map<String, BigDecimal> structure =
        new HashMap<>() {
          {
            put("b", new BigDecimal("1.12345"));
          }
        };

    Expression expression1 = createExpression("a.b");
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", structure)
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("1.12345");
  }

  @Test
  void testCustomRoundingDecimalsLiteral() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(2).build();
    Expression expression = new Expression("2.12345", config);

    assertThat(expression.evaluate(VariableResolver.empty()).getStringValue()).isEqualTo("2.12");
  }

  @Test
  void testCustomRoundingDecimalsVariable() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(2).build();
    Expression expression1 = new Expression("a", config);
    new BigDecimal("2.126");
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", new BigDecimal("2.126"))
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("2.13");
  }

  @Test
  void testCustomRoundingDecimalsInfixOperator() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(3).build();
    Expression expression = new Expression("2.12345+1.54321", config);

    // literals are rounded first, the added and rounded again.
    assertThat(expression.evaluate(VariableResolver.empty()).getStringValue()).isEqualTo("3.666");
  }

  @Test
  void testCustomRoundingDecimalsPrefixOperator() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(3).build();
    Expression expression = new Expression("-2.12345", config);

    assertThat(expression.evaluate(VariableResolver.empty()).getStringValue()).isEqualTo("-2.123");
  }

  @Test
  void testCustomRoundingDecimalsFunction() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(3).build();
    Expression expression = new Expression("SUM(2.12345,1.54321)", config);

    // literals are rounded first, the added and rounded again.
    assertThat(expression.evaluate(VariableResolver.empty()).getStringValue()).isEqualTo("3.666");
  }

  @Test
  void testCustomRoundingDecimalsArray() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(3).build();
    List<BigDecimal> array = List.of(new BigDecimal("1.12345"));
    Expression expression1 = new Expression("a[0]", config);
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", array)
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("1.123");
  }

  @Test
  void testCustomRoundingStructure() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(3).build();
    Map<String, BigDecimal> structure =
        new HashMap<>() {
          {
            put("b", new BigDecimal("1.12345"));
          }
        };

    Expression expression1 = new Expression("a.b", config);
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", structure)
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("1.123");
  }

  @Test
  void testDefaultStripZeros() throws EvaluationException, ParseException {
    Expression expression = new Expression("9.000");
    assertThat(expression.evaluate(VariableResolver.empty()).getNumberValue()).isEqualTo("9");
  }

  @Test
  void testDoNotStripZeros() throws EvaluationException, ParseException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().stripTrailingZeros(false).build();

    Expression expression = new Expression("9.000", config);
    assertThat(expression.evaluate(VariableResolver.empty()).getNumberValue()).isEqualTo("9.000");
  }
}
