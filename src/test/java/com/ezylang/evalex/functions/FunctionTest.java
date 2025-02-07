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
package com.ezylang.evalex.functions;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.parser.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FunctionTest {

  @Test
  void testParameterDefinition() {
    Function function = new CorrectFunctionDefinitionFunction();

    assertThat(function.parameterDefinitions().get(0).getName()).isEqualTo("default");
    assertThat(function.parameterDefinitions().get(0).isLazy()).isFalse();
    assertThat(function.parameterDefinitions().get(0).isVarArg()).isFalse();

    assertThat(function.parameterDefinitions().get(1).getName()).isEqualTo("lazy");
    assertThat(function.parameterDefinitions().get(1).isLazy()).isTrue();
    assertThat(function.parameterDefinitions().get(1).isVarArg()).isFalse();

    assertThat(function.parameterDefinitions().get(2).getName()).isEqualTo("vararg");
    assertThat(function.parameterDefinitions().get(2).isLazy()).isFalse();
    assertThat(function.parameterDefinitions().get(2).isVarArg()).isTrue();
  }

  @Test
  void testParameterIsLazy() {
    Function function = new CorrectFunctionDefinitionFunction();

    assertThat(function.parameterIsLazy(0)).isFalse();
    assertThat(function.parameterIsLazy(1)).isTrue();
    assertThat(function.parameterIsLazy(2)).isFalse();
    assertThat(function.parameterIsLazy(3)).isFalse();
    assertThat(function.parameterIsLazy(4)).isFalse();
  }

  @Test
  void testVarargNotAllowed() {
    assertThatThrownBy(WrongVarargFunctionDefinitionFunction::new)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Only last parameter may be defined as variable argument");
  }

  private static class CorrectFunctionDefinitionFunction extends com.ezylang.evalex.functions.AbstractFunction {

    protected CorrectFunctionDefinitionFunction() {
      super(
        com.ezylang.evalex.functions.FunctionParameterDefinition.of(Value.class,"default"),
        com.ezylang.evalex.functions.FunctionParameterDefinition.lazyWith(Value.class,"lazy"),
        com.ezylang.evalex.functions.FunctionParameterDefinition.varArgWith(Value.class,"vararg")
      );
    }

    @Override public Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, List<Value<?>> parameterValues)
      throws EvaluationException {
      return Value.of("OK");
    }
  }

  private static class WrongVarargFunctionDefinitionFunction extends AbstractFunction {
    public WrongVarargFunctionDefinitionFunction() {
      super(
        com.ezylang.evalex.functions.FunctionParameterDefinition.of(Value.class,"default"),
        com.ezylang.evalex.functions.FunctionParameterDefinition.varArgWith(Value.class, "vararg"),
        com.ezylang.evalex.functions.FunctionParameterDefinition.of(Value.class,"another")
      );
    }
    @Override public Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, List<Value<?>> parameterValues)
      throws EvaluationException {
      return Value.of("OK");
    }
  }
}
