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
package com.ezylang.evalex.operators.booleans;

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class InfixNotEqualsOperatorTest extends BaseEvaluationTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "1!=1 : false",
        "1<>1 : false",
        "0!=0 : false",
        "1!=0 : true",
        "0!=1 : true",
        "21.678!=21.678 : false",
        "\"abc\"!=\"abc\" : false",
        "\"abc\"!=\"xyz\" : true",
        "1+2!=4-1 : false",
        "-5.2!=-5.2 :false",
        "DT_DATE_TIME(2022,10,30)!=DT_DATE_TIME(2022,10,30) : false",
        "DT_DATE_TIME(2022,10,30)!=DT_DATE_TIME(2022,10,28) : true",
        "DT_DURATION_PARSE(\"P2D\")!=DT_DURATION_PARSE(\"PT24H\") : true"
      })
  void testInfixNotEqualsLiterals(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testInfixNotEqualsVariables() throws EvaluationException, ParseException {
    Expression expression = Expression.of("a!=b");

    assertThat(
            expression.evaluate(VariableResolver.builder()
                .with("a", new BigDecimal("1.4"))
                .with("b", new BigDecimal("1.4"))
                .build())
                .wrapped())
        .isEqualTo(false);

    assertThat(expression.evaluate(VariableResolver.builder()
      .with("a", "Hello")
      .with("b", "Hello")
      .build()).wrapped())
        .isEqualTo(false);

    assertThat(expression.evaluate(VariableResolver.builder()
      .with("a", "Hello").with("b", "Goodbye")
      .build()).wrapped())
        .isEqualTo(true);

    assertThat(expression.evaluate(VariableResolver.builder()
      .with("a", true).with("b", true)
      .build()).wrapped()).isEqualTo(false);

    assertThat(expression.evaluate(VariableResolver.builder()
      .with("a", false).with("b", true)
      .build()).wrapped()).isEqualTo(true);
  }

  @Test
  void testInfixNotEqualsArrays() throws EvaluationException, ParseException {
    Expression expression = Expression.of("a!=b");

    assertThat(
            expression.evaluate(VariableResolver.builder()
                .with("a", Value::of, Arrays.asList("a", "b", "c"))
                .with("b", Value::of, Arrays.asList("a", "b", "c"))
                .build())
                .wrapped())
        .isEqualTo(false);

    assertThat(
            expression.evaluate(VariableResolver.builder()
                .with("a",Value::of,  Arrays.asList("a", "b", "c"))
                .with("b", Value::of, Arrays.asList("c", "b", "a"))
                .build())
                .wrapped())
        .isEqualTo(true);
  }

  @Test
  void testInfixNotEqualsStructures() throws EvaluationException, ParseException {
    Expression expression = Expression.of("a!=b");

    Map<String, BigDecimal> structure1 =
        new HashMap<>() {
          {
            put("a", new BigDecimal(35));
            put("b", new BigDecimal(99));
          }
        };

    Map<String, BigDecimal> structure2 =
        new HashMap<>() {
          {
            put("a", new BigDecimal(35));
            put("b", new BigDecimal(99));
          }
        };

    Map<String, BigDecimal> structure3 =
        new HashMap<>() {
          {
            put("a", new BigDecimal(45));
            put("b", new BigDecimal(99));
          }
        };

    assertThat(expression.evaluate(VariableResolver.builder()
      .with("a", Value::of, structure1).with("b", Value::of, structure2)
      .build()).wrapped())
        .isEqualTo(false);

    assertThat(expression.evaluate(VariableResolver.builder()
      .with("a", Value::of, structure1).with("b", Value::of, structure3)
      .build()).wrapped())
        .isEqualTo(true);
  }
}
