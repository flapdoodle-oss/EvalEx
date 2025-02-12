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

import com.ezylang.evalex.config.Configuration;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.operators.arithmetic.InfixPowerOfOperator;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExpressionEvaluatorPowerOfTest extends BaseExpressionEvaluatorTest {

  @Test
  void testPrecedenceDefault() throws ParseException, EvaluationException {
    assertThat(evaluate("-2^2")).isEqualTo("4.0");
  }

  @Test
  void testPrecedenceHigher() throws ParseException, EvaluationException {
    Configuration config =
        Configuration.defaultConfiguration()
          .withAdditionalOperators(Map.entry("^", new InfixPowerOfOperator() {
            @Override public int getPrecedence() {
              return 100;
            }
          }));

    Expression expression = Expression.of("-2^2", config);

    assertThat(expression.evaluate(VariableResolver.empty()).wrapped().toString()).isEqualTo("-4.0");
  }
}
