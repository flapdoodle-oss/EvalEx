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

import com.ezylang.evalex.config.TestConfigurationXProvider.DummyFunction;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.operatorsx.InfixOperator;
import com.ezylang.evalex.operatorsx.arithmetic.InfixPlusOperator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationTest {

  @Test
  void testDefaultSetup() {
    Configuration configuration = Configuration.defaultConfiguration();

    assertThat(configuration.getMathContext())
        .isEqualTo(ExpressionConfiguration.DEFAULT_MATH_CONTEXT);
    assertThat(configuration.getOperatorResolver())
        .isInstanceOf(OperatorResolver.class);
    assertThat(configuration.getFunctionResolver())
        .isInstanceOf(FunctionResolver.class);
    assertThat(configuration.isArraysAllowed()).isTrue();
    assertThat(configuration.isStructuresAllowed()).isTrue();
    assertThat(configuration.isImplicitMultiplicationAllowed()).isTrue();
//    assertThat(configuration.getDefaultConstants())
//        .containsAllEntriesOf(Configuration.StandardConstants);
//    assertThat(configuration.getDecimalPlacesRounding())
//        .isEqualTo(ExpressionConfiguration.DECIMAL_PLACES_ROUNDING_UNLIMITED);
//    assertThat(configuration.isStripTrailingZeros()).isTrue();
    assertThat(configuration.isAllowOverwriteConstants()).isTrue();
  }

  @SuppressWarnings("unchecked") @Test
  void testWithAdditionalOperators() {
    Configuration configuration =
        Configuration.defaultConfiguration()
            .withAdditionalOperators(
                Map.entry("ADDED1", new InfixPlusOperator()),
                Map.entry("ADDED2", new InfixPlusOperator()));

    assertThat(configuration.getOperatorResolver().hasOperator(InfixOperator.class, "ADDED1")).isTrue();
    assertThat(configuration.getOperatorResolver().hasOperator(InfixOperator.class, "ADDED2")).isTrue();
  }

  @Test
  void testWithAdditionalFunctions() {
    Configuration configuration =
        Configuration.defaultConfiguration()
            .withAdditionalFunctions(
                Map.entry("ADDED1", new DummyFunction()), Map.entry("ADDED2", new DummyFunction()));

    assertThat(configuration.getFunctionResolver().hasFunction("ADDED1")).isTrue();
    assertThat(configuration.getFunctionResolver().hasFunction("ADDED2")).isTrue();
  }

  @Test
  void testCustomMathContext() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().mathContext(MathContext.DECIMAL32).build();

    assertThat(configuration.getMathContext()).isEqualTo(MathContext.DECIMAL32);
  }

  @Test
  void testCustomOperatorDictionary() {
    OperatorIfcResolver mockedOperatorDictionary = Mockito.mock(OperatorIfcResolver.class);

    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().operatorIfcResolver(mockedOperatorDictionary).build();

    assertThat(configuration.getOperatorIfcResolver()).isEqualTo(mockedOperatorDictionary);
  }

  @Test
  void testCustomFunctionDictionary() {
    FunctionIfcResolver mockedFunctionDictionary = Mockito.mock(FunctionIfcResolver.class);

    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().functionIfcResolver(mockedFunctionDictionary).build();

    assertThat(configuration.getFunctionIfcResolver()).isEqualTo(mockedFunctionDictionary);
  }

  @Test
  void testCustomConstants() {
    Map<String, EvaluationValue> constants =
        new HashMap<>() {
          {
            put("A", EvaluationValue.of("a"));
            put("B", EvaluationValue.of("b"));
          }
        };
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().defaultConstants(constants).build();

    assertThat(configuration.getDefaultConstants()).containsAllEntriesOf(constants);
  }

  @Test
  void testArraysAllowed() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().arraysAllowed(false).build();

    assertThat(configuration.isArraysAllowed()).isFalse();
  }

  @Test
  void testStructuresAllowed() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().structuresAllowed(false).build();

    assertThat(configuration.isStructuresAllowed()).isFalse();
  }

  @Test
  void testImplicitMultiplicationAllowed() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().implicitMultiplicationAllowed(false).build();

    assertThat(configuration.isImplicitMultiplicationAllowed()).isFalse();
  }
}
