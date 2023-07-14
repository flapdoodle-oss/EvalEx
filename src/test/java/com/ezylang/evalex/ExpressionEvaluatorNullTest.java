/*
  Copyright 2012-2023 Udo Klimaschewski

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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpressionEvaluatorNullTest extends BaseExpressionEvaluatorTest {

  @Test
  void testNullEquals() throws ParseException, EvaluationException {
    Expression expression = createExpression("a == null");
    assertExpressionHasExpectedResult(expression, VariableResolver.builder().with("a", null).build(),"true");
    assertExpressionHasExpectedResult(expression,VariableResolver.builder().with("a", 99).build(), "false");
  }

  @Test
  void testNullNotEquals() throws ParseException, EvaluationException {
    Expression expression = new Expression("a != null");
    assertExpressionHasExpectedResult(expression, VariableResolver.builder().with("a", null).build(),"false");
    assertExpressionHasExpectedResult(expression, VariableResolver.builder().with("a", 99).build(),"true");
  }

  @Test
  void testHandleWithIf() throws EvaluationException, ParseException {
    Expression expression1 = createExpression("IF(a != null, a * 5, 1)");
    assertExpressionHasExpectedResult(expression1, VariableResolver.builder().with("a", null).build(),"1");
    assertExpressionHasExpectedResult(expression1, VariableResolver.builder().with("a", 3).build(),"15");

    Expression expression2 =
        createExpression("IF(a == null, \"Unknown name\", \"The name is \" + a)");
    assertExpressionHasExpectedResult(expression2, VariableResolver.builder().with("a", null).build(),"Unknown name");
    assertExpressionHasExpectedResult(expression2, VariableResolver.builder().with("a", "Max").build(),"The name is Max");
  }

  @Test
  void testHandleWithMaps() throws EvaluationException, ParseException {
    Expression expression = createExpression("a == null && b == null");
    Map<String, Object> values = new HashMap<>();
    values.put("a", null);
    values.put("b", null);

    assertExpressionHasExpectedResult(expression, VariableResolver.builder().withValues(values).build(),"true");
  }

  @Test
  void testFailWithNoHandling() {
    Expression expression5 = createExpression("a * 5");
    Expression expression1 = expression5;
    assertThatThrownBy(() -> expression1.evaluate(VariableResolver.builder().with("a", null).build()))
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Unsupported data types in operation");

    Expression expression4 = createExpression("FLOOR(a)");
    Expression expression2 = expression4;
    assertThatThrownBy(() -> expression2.evaluate(VariableResolver.builder().with("a", null).build())).isInstanceOf(NullPointerException.class);

    Expression expression = createExpression("a > 5");
    Expression expression3 = expression;
    assertThatThrownBy(() -> expression3.evaluate(VariableResolver.builder().with("a", null).build())).isInstanceOf(NullPointerException.class);
  }

  private void assertExpressionHasExpectedResult(Expression expression, VariableResolver variableResolver, String expectedResult)
      throws EvaluationException, ParseException {
		assertThat(expression.evaluate(variableResolver).getStringValue()).isEqualTo(expectedResult);
  }
}
