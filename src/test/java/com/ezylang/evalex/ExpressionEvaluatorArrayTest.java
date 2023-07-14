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

import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpressionEvaluatorArrayTest extends BaseExpressionEvaluatorTest {

  @Test
  void testSimpleArray() throws ParseException, EvaluationException {
    List<BigDecimal> array = List.of(new BigDecimal(99));
    Expression expression1 = createExpression("a[0]");
    Expression expression = expression1;
    assertThat(expression.evaluate(VariableResolver.builder().with("a", array).build()).getStringValue()).isEqualTo("99");
  }

  @Test
  void testMultipleEntriesArray() throws ParseException, EvaluationException {
    List<BigDecimal> array = Arrays.asList(new BigDecimal(2), new BigDecimal(4), new BigDecimal(6));
    Expression expression1 = createExpression("a[0]+a[1]+a[2]");
    Expression expression = expression1;

    assertThat(expression.evaluate(VariableResolver.builder().with("a", array).build()).getStringValue()).isEqualTo("12");
  }

  @Test
  void testExpressionArray() throws ParseException, EvaluationException {
    List<BigDecimal> array = List.of(new BigDecimal(3));
    Expression expression1 = createExpression("a[4-x]");
    new BigDecimal(4);
    Expression expression = expression1;

    assertThat(expression.evaluate(VariableResolver.builder().with("a", array).and("x", new BigDecimal(4)).build()).getStringValue()).isEqualTo("3");
  }

  @Test
  void testNestedArray() throws ParseException, EvaluationException {
    List<BigDecimal> arrayA = List.of(new BigDecimal(3));
    List<BigDecimal> arrayB =
        Arrays.asList(new BigDecimal(2), new BigDecimal(4), new BigDecimal(6));
    Expression expression1 = createExpression("a[b[6-4]-x]");
    new BigDecimal(6);
    Expression expression =
      expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", arrayA)
      .and("b", arrayB)
      .and("x", new BigDecimal(6))
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("3");
  }

  @Test
  void testStringArray() throws ParseException, EvaluationException {
    List<String> array = Arrays.asList("Hello", "beautiful", "world");
    Expression expression1 = createExpression("a[0] + \" \" + a[1] + \" \" + a[2]");
    Expression expression = expression1;

    assertThat(expression.evaluate(VariableResolver.builder().with("a", array).build()).getStringValue()).isEqualTo("Hello beautiful world");
  }

  @Test
  void testBooleanArray() throws ParseException, EvaluationException {
    List<Boolean> array = Arrays.asList(true, true, false);
    Expression expression1 = createExpression("a[0] + \" \" + a[1] + \" \" + a[2]");
    Expression expression = expression1;

    assertThat(expression.evaluate(VariableResolver.builder().with("a", array).build()).getStringValue()).isEqualTo("true true false");
  }

  @Test
  void testArrayOfArray() throws EvaluationException, ParseException {
    List<BigDecimal> subArray1 = Arrays.asList(new BigDecimal(1), new BigDecimal(2));
    List<BigDecimal> subArray2 = Arrays.asList(new BigDecimal(4), new BigDecimal(8));

    List<List<BigDecimal>> array = Arrays.asList(subArray1, subArray2);

    Expression expression7 = createExpression("a[0][0]");
    Expression expression1 = expression7;
    Expression expression6 = createExpression("a[0][1]");
    Expression expression2 = expression6;
    Expression expression5 = createExpression("a[1][0]");
    Expression expression3 = expression5;
    Expression expression = createExpression("a[1][1]");
    Expression expression4 = expression;

    assertThat(expression1.evaluate(VariableResolver.builder().with("a", array).build()).getStringValue()).isEqualTo("1");
    assertThat(expression2.evaluate(VariableResolver.builder().with("a", array).build()).getStringValue()).isEqualTo("2");
    assertThat(expression3.evaluate(VariableResolver.builder().with("a", array).build()).getStringValue()).isEqualTo("4");
    assertThat(expression4.evaluate(VariableResolver.builder().with("a", array).build()).getStringValue()).isEqualTo("8");
  }

  @Test
  void testMixedArray() throws ParseException, EvaluationException {
    List<?> array = Arrays.asList("Hello", new BigDecimal(4), true);
    Expression expression5 = createExpression("a[0]");
    Expression expression1 = expression5;
    Expression expression4 = createExpression("a[1]");
    Expression expression2 = expression4;
    Expression expression = createExpression("a[2]");
    Expression expression3 = expression;

    assertThat(expression1.evaluate(VariableResolver.builder().with("a", array).build()).getStringValue()).isEqualTo("Hello");
    assertThat(expression2.evaluate(VariableResolver.builder().with("a", array).build()).getStringValue()).isEqualTo("4");
    assertThat(expression3.evaluate(VariableResolver.builder().with("a", array).build()).getStringValue()).isEqualTo("true");
  }

  @Test
  void testThrowsUnsupportedDataTypeForArray() {
    assertThatThrownBy(() -> {
      Expression expression = createExpression("a[0]");
      expression
        .evaluate(VariableResolver.builder().with("a", "aString").build());
    })
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Unsupported data types in operation");
  }

  @Test
  void testThrowsUnsupportedDataTypeForIndex() {
    assertThatThrownBy(
            () -> {
              List<?> array = List.of("Hello");
              Expression expression = createExpression("a[b]");
              expression.evaluate(VariableResolver.builder().with("a", array).and("b", "anotherString").build());
            })
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Unsupported data types in operation");
  }
}
