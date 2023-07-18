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
package com.ezylang.evalex.parserx;

import com.ezylang.evalex.functionsx.Function;
import com.ezylang.evalex.operatorsx.Operator;
import com.ezylang.evalex.parser.Nullable;
import com.ezylang.evalex.parser.TokenType;
import org.immutables.value.Value;

/**
 * A token represents a singe part of an expression, like an operator, number literal, or a brace.
 * Each token has a unique type, a value (its representation) and a position (starting with 1) in
 * the original expression string.
 *
 * <p>For operators and functions, the operator and function definition is also set during parsing.
 */
@Value.Immutable
public interface Token {

  @Value.Parameter
  int getStartPosition();

  @Value.Parameter
  String getValue();

  @Value.Parameter
  TokenType getType();

  @Nullable
  @Value.Auxiliary
  Function getFunctionDefinition();

  @Nullable
  @Value.Auxiliary
  Operator getOperatorDefinition();

  @Value.Auxiliary
  default <T extends Operator> T operatorDefinition(Class<T> operatorType) {
    Operator def = getOperatorDefinition();
    if (operatorType.isInstance(def)) {
      return operatorType.cast(def);
    }
    throw new IllegalArgumentException("operator definition does not match: "+operatorType+" -> "+def);
  }

  static Token of(int startPosition, String value, TokenType type) {
    return ImmutableToken.of(startPosition, value, type);
  }

  static Token of(int startPosition, String value, TokenType type, Function functionDefinition) {
    return ImmutableToken.of(startPosition, value, type).withFunctionDefinition(functionDefinition);
  }

  static Token of(int startPosition, String value, TokenType type, Operator operatorDefinition) {
    return ImmutableToken.of(startPosition, value, type).withOperatorDefinition(operatorDefinition);
  }
}
