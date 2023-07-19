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

import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.parserx.ParseException;
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
    ExpressionX expression1 = ExpressionX.of("a");
    new BigDecimal("2.12345");
    ExpressionX expression = expression1;

    VariableResolverX variableResolver = VariableResolverX.builder()
      .with("a", Value.of(new BigDecimal("2.12345")))
      .build();
    assertThat(evaluate("a", variableResolver)).isEqualTo("2.12345");
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
    VariableResolverX variableResolver = VariableResolverX.builder()
      .with("a", Value::of, array)
      .build();
    assertThat(evaluate("a[0]", variableResolver)).isEqualTo("1.12345");
  }

  @Test
  void testDefaultNoRoundingStructure() throws ParseException, EvaluationException {
    Map<String, BigDecimal> structure =
        new HashMap<>() {
          {
            put("b", new BigDecimal("1.12345"));
          }
        };

    VariableResolverX variableResolver = VariableResolverX.builder()
      .with("a", Value::of, structure)
      .build();
    assertThat(evaluate("a.b", variableResolver)).isEqualTo("1.12345");
  }

//  @Test
//  void testCustomRoundingDecimalsLiteral() throws ParseException, EvaluationException {
//    Configuration config =
//        Configuration.builder().decimalPlacesRounding(2).build();
//    ExpressionX expression = ExpressionX.of("2.12345", config);
//
//    assertThat(expression.evaluate(VariableResolverX.empty()).getStringValue()).isEqualTo("2.12");
//  }
//
//  @Test
//  void testCustomRoundingDecimalsVariable() throws ParseException, EvaluationException {
//    Configuration config =
//        Configuration.builder().decimalPlacesRounding(2).build();
//    ExpressionX expression1 = ExpressionX.of("a", config);
//    new BigDecimal("2.126");
//    ExpressionX expression = expression1;
//
//    VariableResolverX variableResolver = VariableResolverX.builder()
//      .with("a", new BigDecimal("2.126"))
//      .build();
//    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("2.13");
//  }

//  @Test
//  void testCustomRoundingDecimalsInfixOperator() throws ParseException, EvaluationException {
//    Configuration config =
//        Configuration.builder().decimalPlacesRounding(3).build();
//    ExpressionX expression = ExpressionX.of("2.12345+1.54321", config);
//
//    // literals are rounded first, the added and rounded again.
//    assertThat(expression.evaluate(VariableResolverX.empty()).getStringValue()).isEqualTo("3.666");
//  }
//
//  @Test
//  void testCustomRoundingDecimalsPrefixOperator() throws ParseException, EvaluationException {
//    Configuration config =
//        Configuration.builder().decimalPlacesRounding(3).build();
//    ExpressionX expression = ExpressionX.of("-2.12345", config);
//
//    assertThat(expression.evaluate(VariableResolverX.empty()).getStringValue()).isEqualTo("-2.123");
//  }

//  @Test
//  void testCustomRoundingDecimalsFunction() throws ParseException, EvaluationException {
//    Configuration config =
//        Configuration.builder().decimalPlacesRounding(3).build();
//    ExpressionX expression = ExpressionX.of("SUM(2.12345,1.54321)", config);
//
//    // literals are rounded first, the added and rounded again.
//    assertThat(expression.evaluate(VariableResolverX.empty()).getStringValue()).isEqualTo("3.666");
//  }
//
//  @Test
//  void testCustomRoundingDecimalsArray() throws ParseException, EvaluationException {
//    Configuration config =
//        Configuration.builder().decimalPlacesRounding(3).build();
//    List<BigDecimal> array = List.of(new BigDecimal("1.12345"));
//    ExpressionX expression1 = ExpressionX.of("a[0]", config);
//    ExpressionX expression = expression1;
//
//    VariableResolverX variableResolver = VariableResolverX.builder()
//      .with("a", array)
//      .build();
//    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("1.123");
//  }
//
//  @Test
//  void testCustomRoundingStructure() throws ParseException, EvaluationException {
//    Configuration config =
//        Configuration.builder().decimalPlacesRounding(3).build();
//    Map<String, BigDecimal> structure =
//        new HashMap<>() {
//          {
//            put("b", new BigDecimal("1.12345"));
//          }
//        };
//
//    ExpressionX expression1 = ExpressionX.of("a.b", config);
//    ExpressionX expression = expression1;
//
//    VariableResolverX variableResolver = VariableResolverX.builder()
//      .with("a", structure)
//      .build();
//    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("1.123");
//  }
//
//  @Test
//  void testDefaultStripZeros() throws EvaluationException, ParseException {
//    ExpressionX expression = ExpressionX.of("9.000");
//    assertThat(expression.evaluate(VariableResolverX.empty()).getNumberValue()).isEqualTo("9");
//  }
//
//  @Test
//  void testDoNotStripZeros() throws EvaluationException, ParseException {
//    Configuration config =
//        Configuration.builder().stripTrailingZeros(false).build();
//
//    ExpressionX expression = ExpressionX.of("9.000", config);
//    assertThat(expression.evaluate(VariableResolverX.empty()).getNumberValue()).isEqualTo("9.000");
//  }
}
