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

import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.FunctionIfc;
import com.ezylang.evalex.operators.AbstractInfixOperator;
import com.ezylang.evalex.operators.AbstractPostfixOperator;
import com.ezylang.evalex.operators.AbstractPrefixOperator;
import com.ezylang.evalex.operators.OperatorIfc;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * The expression configuration can be used to configure various aspects of expression parsing and
 * evaluation. <br>
 * A <code>Builder</code> is provided to create custom configurations, e.g.: <br>
 *
 * <pre>
 *   ExpressionConfiguration config = ExpressionConfiguration.builder().mathContext(MathContext.DECIMAL32).arraysAllowed(false).build();
 * </pre>
 *
 * <br>
 * Additional operators and functions can be added to an existing configuration:<br>
 *
 * <pre>
 *     ExpressionConfiguration.defaultConfiguration()
 *        .withAdditionalOperators(
 *            Map.entry("++", new PrefixPlusPlusOperator()),
 *            Map.entry("++", new PostfixPlusPlusOperator()))
 *        .withAdditionalFunctions(Map.entry("save", new SaveFunction()),
 *            Map.entry("update", new UpdateFunction()));
 * </pre>
 */
@Builder(toBuilder = true)
public class ExpressionConfiguration {

  /** The standard set constants for EvalEx. */
  public static final Map<String, EvaluationValue> StandardConstants =
      Collections.unmodifiableMap(getStandardConstants());

  /** Setting the decimal places to unlimited, will disable intermediate rounding. */
  public static final int DECIMAL_PLACES_ROUNDING_UNLIMITED = -1;

  /** The default math context has a precision of 68 and {@link RoundingMode#HALF_EVEN}. */
  public static final MathContext DEFAULT_MATH_CONTEXT =
      new MathContext(68, RoundingMode.HALF_EVEN);

  /** The operator dictionary holds all operators that will be allowed in an expression. */
  @Builder.Default
  @Getter
  @SuppressWarnings("unchecked")
  private OperatorIfcResolver operatorIfcResolver = OperatorIfcResolver.defaults();

  @Builder.Default
  @Getter
  @SuppressWarnings("unchecked")
  private OperatorResolver operatorResolver = OperatorResolver.defaults();

  // basic functions
  // trigonometric
  // string functions
  // date time functions
  /** The function dictionary holds all functions that will be allowed in an expression. */
  @Builder.Default
  @Getter
  @SuppressWarnings("unchecked")
  private FunctionIfcResolver functionIfcResolver = FunctionIfcResolver.defaults();

  /** The math context to use. */
  @Builder.Default @Getter private final MathContext mathContext = DEFAULT_MATH_CONTEXT;

  /**
   * Default constants will be added automatically to each expression and can be used in expression
   * evaluation.
   */
  @Builder.Default @Getter
  private final Map<String, EvaluationValue> defaultConstants = getStandardConstants();

  /** Support for arrays in expressions are allowed or not. */
  @Builder.Default @Getter private final boolean arraysAllowed = true;

  /** Support for structures in expressions are allowed or not. */
  @Builder.Default @Getter private final boolean structuresAllowed = true;

  /** Support for implicit multiplication, like in (a+b)(b+c) are allowed or not. */
  @Builder.Default @Getter private final boolean implicitMultiplicationAllowed = true;

  /**
   * If specified, all results from operations and functions will be rounded to the specified number
   * of decimal digits, using the MathContexts rounding mode.
   */
  @Builder.Default @Getter
  private final int decimalPlacesRounding = DECIMAL_PLACES_ROUNDING_UNLIMITED;

  /**
   * If set to true (default), then the trailing decimal zeros in a number result will be stripped.
   */
  @Builder.Default @Getter private final boolean stripTrailingZeros = true;

  /**
   * If set to true (default), then variables can be set that have the name of a constant. In that
   * case, the constant value will be removed and a variable value will be set.
   */
  @Builder.Default @Getter private final boolean allowOverwriteConstants = true;

  /** Set the default zone id. By default, the system default zone id is used. */
  @Builder.Default @Getter private final ZoneId defaultZoneId = ZoneId.systemDefault();
  /**
   * Convenience method to create a default configuration.
   *
   * @return A configuration with default settings.
   */
  public static ExpressionConfiguration defaultConfiguration() {
    return ExpressionConfiguration.builder().build();
  }

  /**
   * Adds additional operators to this configuration.
   *
   * @param operators variable number of arguments with a map entry holding the operator name and
   *     implementation. <br>
   *     Example:
   *     <pre>
   *                                                        ExpressionConfiguration.defaultConfiguration()
   *                                                           .withAdditionalOperators(
   *                                                               Map.entry("++", new PrefixPlusPlusOperator()),
   *                                                               Map.entry("++", new PostfixPlusPlusOperator()));
   *                                                        </pre>
   *
   * @return The modified configuration, to allow chaining of methods.
   */
  @SafeVarargs
  @Deprecated
  public final ExpressionConfiguration withAdditionalOperators(
      Map.Entry<String, OperatorIfc>... operators) {
    ImmutableMapBasedOperatorIfcResolver.Builder builder = MapBasedOperatorIfcResolver.builder();
    Arrays.stream(operators)
        .forEach(entry -> {
          OperatorIfc operator = entry.getValue();
          switch (operator.type()) {
            case INFIX_OPERATOR:
              builder.putInfixOperators(entry.getKey(), (AbstractInfixOperator) operator);
              break;
            case PREFIX_OPERATOR:
              builder.putPrefixOperators(entry.getKey(), (AbstractPrefixOperator) operator);
              break;
            case POSTFIX_OPERATOR:
              builder.putPostfixOperators(entry.getKey(), (AbstractPostfixOperator) operator);
              break;
          }
        });
    ImmutableMapBasedOperatorIfcResolver override = builder.build();
    this.operatorIfcResolver = override.andThen(operatorIfcResolver);
    return this;
  }

  /**
   * Adds additional functions to this configuration.
   *
   * @param functions variable number of arguments with a map entry holding the functions name and
   *     implementation. <br>
   *     Example:
   *     <pre>
   *                                                        ExpressionConfiguration.defaultConfiguration()
   *                                                           .withAdditionalFunctions(
   *                                                               Map.entry("save", new SaveFunction()),
   *                                                               Map.entry("update", new UpdateFunction()));
   *                                                        </pre>
   *
   * @return The modified configuration, to allow chaining of methods.
   */
  @SafeVarargs
  @Deprecated
  public final ExpressionConfiguration withAdditionalFunctionsIfc(
      Map.Entry<String, FunctionIfc>... functions) {
    ImmutableMapBasedFunctionIfcResolver.Builder builder = MapBasedFunctionIfcResolver.builder();

    Arrays.stream(functions)
        .forEach(entry -> builder.putFunctions(entry.getKey(), entry.getValue()));

    ImmutableMapBasedFunctionIfcResolver override = builder.build();
    this.functionIfcResolver = override.andThen(this.functionIfcResolver);
    
    return this;
  }

  @Deprecated
  public final ExpressionConfiguration withAdditionalFunctionIfc(String name, FunctionIfc function) {
    return withAdditionalFunctionsIfc(Map.entry(name, function));
  }

  private static Map<String, EvaluationValue> getStandardConstants() {

    Map<String, EvaluationValue> constants = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    constants.put("TRUE", EvaluationValue.of(true));
    constants.put("FALSE", EvaluationValue.of(false));
    constants.put(
        "PI",
      EvaluationValue.of(
          new BigDecimal(
              "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679")));
    constants.put(
        "E",
      EvaluationValue.of(
          new BigDecimal(
              "2.71828182845904523536028747135266249775724709369995957496696762772407663")));
    constants.put("NULL", EvaluationValue.of(null));

    return constants;
  }
}
