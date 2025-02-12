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

import com.ezylang.evalex.config.TestConfigurationProvider.DummyFunction;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.operators.InfixOperator;
import com.ezylang.evalex.operators.arithmetic.InfixPlusOperator;
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

//    assertThat(configuration.getMathContext())
//        .isEqualTo(Configuration.DEFAULT_MATH_CONTEXT);
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
//        .isEqualTo(Configuration.DECIMAL_PLACES_ROUNDING_UNLIMITED);
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
    Configuration configuration =
        Configuration.builder().mathContext(MathContext.DECIMAL32).build();

    assertThat(configuration.getMathContext()).isEqualTo(MathContext.DECIMAL32);
  }

  @Test
  void testCustomOperatorDictionary() {
    OperatorResolver mockedOperatorDictionary = Mockito.mock(OperatorResolver.class);

    Configuration configuration =
        Configuration.builder().operatorResolver(mockedOperatorDictionary).build();

    assertThat(configuration.getOperatorResolver()).isEqualTo(mockedOperatorDictionary);
  }

  @Test
  void testCustomFunctionDictionary() {
    FunctionResolver mockedFunctionDictionary = Mockito.mock(FunctionResolver.class);

    Configuration configuration =
        Configuration.builder().functionResolver(mockedFunctionDictionary).build();

    assertThat(configuration.getFunctionResolver()).isEqualTo(mockedFunctionDictionary);
  }

  @Test
  void testCustomConstants() {
    Map<String, Value<?>> constants =
        new HashMap<>() {
          {
            put("A", Value.of("a"));
            put("B", Value.of("b"));
          }
        };
    Configuration configuration =
        Configuration.builder().constantResolver(VariableResolver.builder()
          .withValues(constants)
          .build()).build();

    assertThat(configuration.getConstantResolver().getData("a")).isEqualTo(Value.of("a"));
  }
}
