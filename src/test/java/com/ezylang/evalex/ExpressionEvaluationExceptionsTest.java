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
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.parserx.ASTNode;
import com.ezylang.evalex.parserx.Token;
import com.ezylang.evalex.parserx.TokenType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpressionEvaluationExceptionsTest {

  @Test
  void testUnexpectedToken() {
    ExpressionX expression = ExpressionX.of("1");

    assertThatThrownBy(
            () -> {
              ASTNode node = new ASTNode(Token.of(1, "(", TokenType.BRACE_OPEN));
              expression.evaluateSubtree(VariableResolverX.empty(), node);
            })
        .isInstanceOf(EvaluationException.class)
        .hasMessage(
            "Unexpected evaluation token: Token{startPosition=1, value=(, type=BRACE_OPEN}");
  }
}
