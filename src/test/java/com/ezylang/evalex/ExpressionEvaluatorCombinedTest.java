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

import com.ezylang.evalex.data.ImmutableValueMap;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.ValueMap;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ExpressionEvaluatorCombinedTest extends BaseExpressionEvaluatorTest {

  @Test
  void testOrderPositionExample() throws ParseException, EvaluationException {
    ImmutableValueMap position = ValueMap.builder()
      .putValues("article", Value.of(3114))
      .putValues("amount", Value.of(3))
      .putValues("price", Value.of(new BigDecimal("14.95")))
      .build();

    ImmutableValueMap order = ValueMap.builder()
      .putValues("id", Value.of(12345))
      .putValues("name", Value.of("Mary"))
      .putValues("positions", Value.of(Value::of, position))
      .build();

    VariableResolver variableResolver = VariableResolver.builder()
      .with("order", Value.of(order))
      .and("x", Value.of(0))
      .build();
    assertThat(evaluate("order.positions[x].amount * order.positions[x].price", variableResolver)).isEqualTo("44.850");
  }
}
