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
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpressionEvaluatorSimpleVariablesTest extends BaseExpressionEvaluatorTest {

  @Test
  void testSingleStringVariable() throws ParseException, EvaluationException {
    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", "hello")
      .build();
    Value<?> result = createExpression("a").evaluate(variableResolver);
    assertThat(result.wrapped()).isEqualTo("hello");
  }

  @Test
  void testSingleNumberVariable() throws ParseException, EvaluationException {
    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", BigDecimal.valueOf(9))
      .build();
    Value<?> result = createExpression("a").evaluate(variableResolver);
    assertThat(result.wrapped()).isEqualTo(BigDecimal.valueOf(9));
  }

  @Test
  void testNumbers() throws ParseException, EvaluationException {
    VariableResolver variableResolver = VariableResolver.builder()
      .with("a", BigDecimal.valueOf(9))
      .with("b", BigDecimal.valueOf(5))
      .build();
    Value<?> result = createExpression("(a+b)*(a-b)").evaluate(variableResolver);
    assertThat(result.wrapped()).isEqualTo(BigDecimal.valueOf(56));
  }

  @Test
  void testStrings() throws ParseException, EvaluationException {
    VariableResolver variableResolver = VariableResolver.builder()
      .with("prefix", "Hello")
      .with("infix", " ")
      .with("postfix", "world")
      .build();
    Value<?> result = createExpression("prefix+infix+postfix").evaluate(variableResolver);
    assertThat(result.wrapped()).isEqualTo("Hello world");
  }

  @Test
  void testStringNumberCombined() throws ParseException, EvaluationException {
    VariableResolver variableResolver = VariableResolver.builder()
      .with("prefix", "Hello")
      .with("infix", BigDecimal.valueOf(2))
      .with("postfix", "world")
      .build();
    Value<?> result = createExpression("prefix+infix+postfix").evaluate(variableResolver);
    assertThat(result.wrapped()).isEqualTo("Hello2world");
  }

  @Test
  void testUnknownVariable() {
    assertThatThrownBy(() -> createExpression("a").evaluate(VariableResolver.empty()))
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Variable or constant value for 'a' not found");
  }
}
