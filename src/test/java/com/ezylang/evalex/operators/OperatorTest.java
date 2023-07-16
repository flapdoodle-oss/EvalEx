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
package com.ezylang.evalex.operators;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.Token;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OperatorTest {

  @Test
  void testPrefixOperator() {
    OperatorIfc operator = new CorrectPrefixOperator();

    assertThat(operator.getPrecedence()).isEqualTo(Precedence.OPERATOR_PRECEDENCE_UNARY.value());
    assertThat(operator.isLeftAssociative()).isFalse();

    assertThat(operator.type()).isEqualTo(OperatorType.PREFIX_OPERATOR);
  }

  @Test
  void testPostfixOperator() {
    OperatorIfc operator = new CorrectPostfixOperator();

    assertThat(operator.getPrecedence()).isEqualTo(88);
    assertThat(operator.isLeftAssociative()).isTrue();

    assertThat(operator.type()).isEqualTo(OperatorType.POSTFIX_OPERATOR);
  }

  @Test
  void testInfixOperator() {
    OperatorIfc operator = new CorrectInfixOperator();

    assertThat(operator.getPrecedence()).isEqualTo(Precedence.OPERATOR_PRECEDENCE_MULTIPLICATIVE.value());
    assertThat(operator.isLeftAssociative()).isTrue();

    assertThat(operator.type()).isEqualTo(OperatorType.INFIX_OPERATOR);
  }

  private static class CorrectPrefixOperator extends DummyAnnotationOperator {
    protected CorrectPrefixOperator() {
      super(OperatorType.PREFIX_OPERATOR, Precedence.OPERATOR_PRECEDENCE_UNARY, false);
    }
  }

  private static class CorrectPostfixOperator extends DummyAnnotationOperator {
    protected CorrectPostfixOperator() {
      super(OperatorType.POSTFIX_OPERATOR, 88, true);
    }
  }

  private static class CorrectInfixOperator extends DummyAnnotationOperator {

    protected CorrectInfixOperator() {
      super(OperatorType.INFIX_OPERATOR, Precedence.OPERATOR_PRECEDENCE_MULTIPLICATIVE, true);
    }
  }

  private static class DummyAnnotationOperator extends AbstractBaseOperator {
    protected DummyAnnotationOperator(OperatorType type, int precedence, boolean leftAssociative) {
      super(type, precedence, leftAssociative);
    }

    protected DummyAnnotationOperator(OperatorType type, Precedence precedence, boolean leftAssociative) {
      super(type, precedence, leftAssociative);
    }

    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      return EvaluationValue.of("OK");
    }
  }
}
