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
package com.ezylang.evalex.parser;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.operators.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class TokenizerLiteralOperatorsTest extends BaseParserTest {

  @BeforeEach
  public void setup() {
    configuration =
        configuration.withAdditionalOperators(
            Map.entry("AND", new AndOperator()),
            Map.entry("OR", new OrOperator()),
            Map.entry("NOT", new NotOperator()),
            Map.entry("DENIED", new DeniedOperator()));
  }

  @Test
  void testAndOrNot() throws ParseException {
    assertAllTokensParsedCorrectly(
        "NOT a AND b DENIED OR NOT(c)",
      Token.of(1, "NOT", TokenType.PREFIX_OPERATOR),
      Token.of(5, "a", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(7, "AND", TokenType.INFIX_OPERATOR),
      Token.of(11, "b", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(13, "DENIED", TokenType.POSTFIX_OPERATOR),
      Token.of(20, "OR", TokenType.INFIX_OPERATOR),
      Token.of(23, "NOT", TokenType.PREFIX_OPERATOR),
      Token.of(26, "(", TokenType.BRACE_OPEN),
      Token.of(27, "c", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(28, ")", TokenType.BRACE_CLOSE));
  }

  static class AndOperator extends AbstractInfixOperator {
    protected AndOperator() {
      super(Precedence.OPERATOR_PRECEDENCE_AND);
    }

    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      return EvaluationValue.of(operands[0].getBooleanValue() && operands[1].getBooleanValue());
    }
  }

  static class OrOperator extends AbstractInfixOperator {
    protected OrOperator() {
      super(Precedence.OPERATOR_PRECEDENCE_OR);
    }
    
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      return EvaluationValue.of(operands[0].getBooleanValue() || operands[1].getBooleanValue());
    }
  }

  static class NotOperator extends AbstractPrefixOperator {
    protected NotOperator() {
      super(Precedence.OPERATOR_PRECEDENCE_UNARY, false);
    }

    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      return EvaluationValue.of(!operands[0].getBooleanValue());
    }
  }

  static class DeniedOperator extends AbstractPostfixOperator {
    protected DeniedOperator() {
        super(Precedence.OPERATOR_PRECEDENCE_UNARY);
      }

      @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      return EvaluationValue.of(!operands[0].getBooleanValue());
    }
  }
}
