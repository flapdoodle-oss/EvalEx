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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpressionEvaluatorStructureTest extends BaseExpressionEvaluatorTest {

  @Test
  void testStructureScientificNumberDistinction() throws EvaluationException, ParseException {
    Map<String, BigDecimal> structure =
        new HashMap<>() {
          {
            put("environment_id", new BigDecimal(12345));
          }
        };
    Expression expression1 = new Expression("order.environment_id");
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("order", structure)
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("12345");
  }

  @Test
  void testStructureScientificNumberDistinctionMultiple()
      throws EvaluationException, ParseException {
    Map<String, Object> structure1 = new HashMap<>();
    Map<String, Object> structure2 = new HashMap<>();
    Map<String, Object> structure3 = new HashMap<>();

    structure3.put("e", new BigDecimal("765"));
    structure2.put("var_x", structure3);
    structure1.put("e_id_e", structure2);

    Expression expression1 = new Expression("order.e_id_e.var_x.e");
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("order", structure1)
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("765");
  }

  @Test
  void testSimpleStructure() throws ParseException, EvaluationException {
    Map<String, BigDecimal> structure =
        new HashMap<>() {
          {
            put("b", new BigDecimal(99));
          }
        };

    Expression expression1 = createExpression("a.b");
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", structure)
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("99");
  }

  @Test
  void testTripleStructure() throws ParseException, EvaluationException {
    Map<String, Map<String, BigDecimal>> structure = new HashMap<>();

    Map<String, BigDecimal> subStructure =
        new HashMap<>() {
          {
            put("c", new BigDecimal(95));
          }
        };

    structure.put("b", subStructure);

    Expression expression1 = createExpression("a.b.c");
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", structure)
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("95");
  }

  @Test
  void testThrowsUnsupportedDataTypeForStructure() {
    assertThatThrownBy(() -> {
      VariableResolver variableResolver = VariableResolver.builder().with("a", "aString").build();
      Expression expression = createExpression("a.b");
      expression.evaluate(variableResolver);
    })
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Unsupported data types in operation");
  }

  @Test
  void testThrowsFieldNotFound() {
    Map<String, BigDecimal> testStructure = new HashMap<>();
    testStructure.put("field1", new BigDecimal(3));

    assertThatThrownBy(
            () -> {
              VariableResolver variableResolver = VariableResolver.builder()
                .with("a", testStructure)
                .build();
              Expression expression = createExpression("a.field1 + a.field2");
              expression.evaluate(variableResolver);
            })
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Field 'field2' not found in structure")
        .extracting("startPosition")
        .isEqualTo(14);
  }

  @Test
  void testStructureWithSpaceInName() throws EvaluationException, ParseException {
    Map<String, BigDecimal> testStructure = new HashMap<>();
    testStructure.put("field 1", new BigDecimal(88));

    Expression expression1 = createExpression("a.\"field 1\"");
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", testStructure)
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("88");
  }

  @Test
  void testTripleStructureWithSpaces() throws ParseException, EvaluationException {
    Map<String, Object> structure = new HashMap<>();
    Map<String, Object> subStructure = new HashMap<>();
    subStructure.put("prop c", 99);
    structure.put("prop b", List.of(subStructure));

    Expression expression1 = createExpression("a.\"prop b\"[0].\"prop c\"");
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", structure)
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("99");
  }

  @Test
  void testStructureWithSpaceInNameAndArrayAccess() throws EvaluationException, ParseException {
    Map<String, List<Integer>> structure =
        new HashMap<>() {
          {
            put("b prop", Arrays.asList(1, 2, 3));
          }
        };

    Expression expression1 = createExpression("a.\"b prop\"[1]");
    Expression expression = expression1;

    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", structure)
      .build();
    assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo("2");
  }
}
