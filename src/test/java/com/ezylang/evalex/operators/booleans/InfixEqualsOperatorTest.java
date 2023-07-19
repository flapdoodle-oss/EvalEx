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
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.parserx.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class InfixEqualsOperatorTest extends BaseEvaluationTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "1=1 : true",
        "1==1 : true",
        "0=0 : true",
        "1=0 : false",
        "0=1 : false",
        "21.678=21.678 : true",
        "\"abc\"=\"abc\" : true",
        "\"abc\"=\"xyz\" : false",
        "1+2=4-1 : true",
        "-5.2=-5.2 : true",
        "DT_DATE_TIME(2022,10,30)=DT_DATE_TIME(2022,10,30) : true",
        "DT_DATE_TIME(2022,10,30)=DT_DATE_TIME(2022,10,01) : false",
        "DT_DURATION_PARSE(\"PT24H\")=DT_DURATION_PARSE(\"P1D\") : true",
      })
  void testInfixEqualsLiterals(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testInfixEqualsVariables() throws EvaluationException, ParseException {
    Expression expression = new Expression("a=b");

    new BigDecimal("1.4");
    new BigDecimal("1.4");
    Expression expression5 = expression;
    assertThat(
            expression5.evaluate(VariableResolver.builder()
                .with("a", new BigDecimal("1.4"))
                .and("b", new BigDecimal("1.4"))
                .build())
                .getBooleanValue())
        .isTrue();

    Expression expression4 = expression;
    assertThat(expression4.evaluate(VariableResolver.builder().with("a", "Hello").and("b", "Hello").build()).getBooleanValue())
        .isTrue();

    Expression expression3 = expression;
    assertThat(expression3.evaluate(VariableResolver.builder().with("a", "Hello").and("b", "Goodbye").build()).getBooleanValue())
        .isFalse();

    Expression expression2 = expression;
    assertThat(expression2.evaluate(VariableResolver.builder().with("a", true).and("b", true).build()).getBooleanValue()).isTrue();

    Expression expression1 = expression;
    assertThat(expression1.evaluate(VariableResolver.builder().with("a", false).and("b", true).build()).getBooleanValue()).isFalse();
  }

  @Test
  void testInfixEqualsArrays() throws EvaluationException, ParseException {
    Expression expression = new Expression("a=b");

    assertThat(
            expression.evaluate(VariableResolver.builder()
                .with("a", Arrays.asList("a", "b", "c"))
                .and("b", Arrays.asList("a", "b", "c"))
                .build())
                .getBooleanValue())
        .isTrue();

    assertThat(
            expression.evaluate(VariableResolver.builder()
                .with("a", Arrays.asList("a", "b", "c"))
                .and("b", Arrays.asList("c", "b", "a"))
                .build())
                .getBooleanValue())
        .isFalse();
  }

  @Test
  void testInfixEqualsStructures() throws EvaluationException, ParseException {
    Expression expression = new Expression("a=b");

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

    assertThat(expression.evaluate(VariableResolver.builder().with("a", structure1).and("b", structure2).build()).getBooleanValue())
        .isTrue();

    assertThat(expression.evaluate(VariableResolver.builder().with("a", structure1).and("b", structure3).build()).getBooleanValue())
        .isFalse();
  }
}
