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
import com.ezylang.evalex.functions.validations.ParameterValidator;
import com.ezylang.evalex.parser.ASTNode;
import com.ezylang.evalex.parser.Token;
import org.immutables.builder.Builder;
import org.immutables.value.Value;

import java.util.List;

/** Definition of a function parameter. */
@Value.Immutable
public interface FunctionParameterDefinition<T extends com.ezylang.evalex.data.Value<?>> {

  @Builder.Parameter
  Class<T> parameterType();

  /** Name of the parameter, useful for error messages etc. */
  String getName();

  /**
   * Whether this parameter is a variable argument parameter (can be repeated).
   *
   * @see com.ezylang.evalex.functions.basic.MinFunction for an example.
   */
  @Value.Default
  default boolean isVarArg() {
    return false;
  }

  @Value.Default
  default boolean isOptional() { return false; }

  /**
   * Set to true, the parameter will not be evaluated in advance, but the corresponding {@link
   * ASTNode} will be passed as a parameter value.
   *
   * @see com.ezylang.evalex.functions.basic.IfFunction for an example.
   */
  @Value.Default
  default boolean isLazy() {
    return false;
  }

  List<ParameterValidator<T>> validators();

  static <T extends com.ezylang.evalex.data.Value<?>> ImmutableFunctionParameterDefinition.Builder<T> builder(Class<T> type) {
    return ImmutableFunctionParameterDefinition.builder(type);
  }

  static <T extends com.ezylang.evalex.data.Value<?>> ImmutableFunctionParameterDefinition<T> of(Class<T> type, String name) {
    return builder(type).name(name).build();
  }

  static <T extends com.ezylang.evalex.data.Value<?>> ImmutableFunctionParameterDefinition<T> varArgWith(Class<T> type, String name) {
    return builder(type).name(name).isVarArg(true).build();
  }

  static <T extends com.ezylang.evalex.data.Value<?>> ImmutableFunctionParameterDefinition<T> optionalWith(Class<T> type, String name) {
    return builder(type).name(name).isOptional(true).build();
  }

  static <T extends com.ezylang.evalex.data.Value<?>> ImmutableFunctionParameterDefinition<T> lazyWith(Class<T> type, String name) {
    return builder(type).name(name).isLazy(true).build();
  }

  @Value.Auxiliary
  default void validatePreEvaluation(Token token, com.ezylang.evalex.data.Value<?> parameterValue) throws EvaluationException {
    if (parameterType().isInstance(parameterValue)) {
      T value = parameterType().cast(parameterValue);
      for (ParameterValidator<T> validator : validators()) {
        validator.validate(token, value);
      }
    } else {
      throw EvaluationException.ofUnsupportedDataTypeInOperation(token);
    }
  }
}
