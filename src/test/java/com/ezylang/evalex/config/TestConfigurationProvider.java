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
package com.ezylang.evalex.config;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.functions.SingleArgumentFunction;
import com.ezylang.evalex.operators.*;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class TestConfigurationProvider {

  public static final ExpressionConfiguration StandardConfigurationWithAdditionalTestOperators =
      ExpressionConfiguration.defaultConfiguration()
          .withAdditionalOperators(
              Map.entry("++", new PrefixPlusPlusOperator()),
              Map.entry("++", new PostfixPlusPlusOperator()),
              Map.entry("?", new PostfixQuestionOperator()))
          .withAdditionalFunctionsIfc(Map.entry("TEST", new DummyFunction()));

  public static class DummyFunction extends SingleArgumentFunction {
    public DummyFunction() {
      super(FunctionParameterDefinition.varArgWith("input"));
    }
    
    @Override
    public EvaluationValue evaluate(
			VariableResolver variableResolver, Expression expression, Token functionToken, List<EvaluationValue> parameterValues) {
      // dummy implementation
      return EvaluationValue.of("OK");
    }
  }

  public static class PrefixPlusPlusOperator extends AbstractPrefixOperator {

    public PrefixPlusPlusOperator() {
      super(Precedence.OPERATOR_PRECEDENCE_UNARY, false);
    }
    
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      // dummy implementation
      EvaluationValue operand = operands[0];
      return EvaluationValue.of(operand.getNumberValue().add(BigDecimal.ONE));
    }
  }

  public static class PostfixPlusPlusOperator extends AbstractPostfixOperator {

    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      // dummy implementation
      EvaluationValue operand = operands[0];
      return EvaluationValue.of(operand.getNumberValue().add(BigDecimal.ONE));
    }
  }

  public static class PostfixQuestionOperator  extends AbstractPostfixOperator {

    public PostfixQuestionOperator() {
      super(Precedence.OPERATOR_PRECEDENCE_UNARY, false);
    }

    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      // dummy implementation
      return EvaluationValue.of("?");
    }
  }
}
