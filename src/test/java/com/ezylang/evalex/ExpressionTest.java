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

import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.operatorsx.InfixOperator;
import com.ezylang.evalex.operatorsx.PostfixOperator;
import com.ezylang.evalex.operatorsx.PrefixOperator;
import com.ezylang.evalex.parserx.ASTNode;
import com.ezylang.evalex.parserx.ParseException;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpressionTest {

  @Test
  void testExpressionDefaults() {
    ExpressionX expression = ExpressionX.of("a+b");

    assertThat(expression.expressionString()).isEqualTo("a+b");
//    assertThat(expression.getConfiguration().getMathContext())
//        .isEqualTo(Configuration.DEFAULT_MATH_CONTEXT);
    assertThat(expression.getConfiguration().getFunctionResolver().hasFunction("SUM")).isTrue();
    assertThat(expression.getConfiguration().getOperatorResolver().hasOperator(InfixOperator.class, "+"))
        .isTrue();
		assertThat(expression.getConfiguration().getOperatorResolver().hasOperator(PrefixOperator.class, "+"))
        .isTrue();
    assertThat(expression.getConfiguration().getOperatorResolver().hasOperator(PostfixOperator.class, "+"))
        .isFalse();
  }

  @Test
  void testValidateOK() throws ParseException {
    ExpressionX.of("1+1").validate();
  }

  @Test
  void testValidateFail() {
    assertThatThrownBy(() -> ExpressionX.of("2#3").validate())
        .isInstanceOf(ParseException.class)
        .hasMessage("Undefined operator '#'");
  }

  @Test
  void testExpressionNode() throws ParseException, EvaluationException {
    ExpressionX expression = ExpressionX.of("a*b");
    ASTNode subExpression = expression.createExpressionNode("4+3");

    ExpressionX expression1 = expression;
    VariableResolverX variableResolver = VariableResolverX.builder()
      .with("a", Value.of(2)).and("b", Value.of(subExpression))
      .build();
    Value<?> result = expression1.evaluate(variableResolver);

    assertThat(result).isEqualTo(Value.of(14));
  }

  @Test
  void testWithValues() throws ParseException, EvaluationException {
    ExpressionX expression = ExpressionX.of("(a + b) * (a - b)");

    Map<String, Value<?>> values = new HashMap<>();
    values.put("a", Value.of(3.5));
    values.put("b", Value.of(2.5));

    ExpressionX expression1 = expression;
    VariableResolverX variableResolver = VariableResolverX.builder()
      .withValues(values)
      .build();
    Value.NumberValue result = (Value.NumberValue) expression1.evaluate(variableResolver);

    assertThat(result.wrapped()).isCloseTo(Value.of(6).wrapped(), Percentage.withPercentage(0.9999));
  }

  @Test
  void testWithValuesDoubleMap() throws ParseException, EvaluationException {
    ExpressionX expression = ExpressionX.of("a+b");

    Map<String, Value.NumberValue> values = new HashMap<>();
    values.put("a", Value.of(3.9));
    values.put("b", Value.of(3.1));

		ExpressionX expression1 = expression;
    VariableResolverX variableResolver = VariableResolverX.builder()
      .withValues(values)
      .build();
    Value<?> result = expression1.evaluate(variableResolver);

    assertThat(result).isEqualTo(Value.of(7));
  }

  @Test
  void testWithValuesStringMap() throws ParseException, EvaluationException {
    ExpressionX expression = ExpressionX.of("a+b+c");

    Map<String, Value.StringValue> values = new HashMap<>();
    values.put("a", Value.of("Hello"));
    values.put("b", Value.of(" "));
    values.put("c", Value.of("world"));

    ExpressionX expression1 = expression;
    VariableResolverX variableResolver = VariableResolverX.builder()
      .withValues(values)
      .build();
    Value<?> result = expression1.evaluate(variableResolver);

    assertThat(result.wrapped()).isEqualTo("Hello world");
  }

  @Test
  void testWithValuesMixedMap() throws ParseException, EvaluationException {
    ExpressionX expression = ExpressionX.of("a+b+c");

    Map<String, Value<?>> values = new HashMap<>();
    values.put("a", Value.of(true));
    values.put("b", Value.of(" "));
    values.put("c", Value.of(24.7));

    ExpressionX expression1 = expression;
    VariableResolverX variableResolver = VariableResolverX.builder()
      .withValues(values)
      .build();
		Value<?> result = expression1.evaluate(variableResolver);

    assertThat(result.wrapped()).isEqualTo("true 24.7");
  }

  @Test
  void testDefaultExpressionOwnsOwnConfigurationEntries() {
    ExpressionX expression1 = ExpressionX.of("1+1");
    ExpressionX expression2 = ExpressionX.of("1+1");

    assertThat(expression1.getConfiguration().getOperatorResolver())
        .isNotSameAs(expression2.getConfiguration().getOperatorResolver());
    assertThat(expression1.getConfiguration().getFunctionResolver())
        .isNotSameAs(expression2.getConfiguration().getFunctionResolver());
//    assertThat(expression1.getConfiguration().getConstantResolver())
//        .isNotSameAs(expression2.getConfiguration().getDefaultConstants());
  }

  @Test
  void testDoubleConverterDefaultMathContext() {
//    ExpressionX defaultMathContextExpression = ExpressionX.of("1");
    assertThat(Value.of(1.67987654321).wrapped())
        .isEqualByComparingTo("1.67987654321");
  }

//  @Test
//  void testDoubleConverterLimitedMathContext() {
//    ExpressionX limitedMathContextExpression =
//        ExpressionX.of(
//            "1", Configuration.builder().mathContext(new MathContext(3)).build());
//    assertThat(limitedMathContextExpression.convertDoubleValue(1.6789).getNumberValue())
//        .isEqualByComparingTo("1.68");
//  }

  @Test
  void testGetAllASTNodes() throws ParseException {
    ExpressionX expression = ExpressionX.of("1+2/3");
    List<ASTNode> nodes = expression.getAllASTNodes();
    assertThat(nodes.get(0).getToken().getValue()).isEqualTo("+");
    assertThat(nodes.get(1).getToken().getValue()).isEqualTo("1");
    assertThat(nodes.get(2).getToken().getValue()).isEqualTo("/");
    assertThat(nodes.get(3).getToken().getValue()).isEqualTo("2");
    assertThat(nodes.get(4).getToken().getValue()).isEqualTo("3");
  }

  @Test
  void testGetUsedVariables() throws ParseException {
    ExpressionX expression = ExpressionX.of("a/2*PI+MIN(e,b)");
    assertThat(expression.getUsedVariables()).containsExactlyInAnyOrder("a", "b");
  }

  @Test
  void testGetUsedVariablesLongNames() throws ParseException {
    ExpressionX expression = ExpressionX.of("var1/2*PI+MIN(var2,var3)");
    assertThat(expression.getUsedVariables()).containsExactlyInAnyOrder("var1", "var2", "var3");
  }

  @Test
  void testGetUsedVariablesNoVariables() throws ParseException {
    ExpressionX expression = ExpressionX.of("1/2");
    assertThat(expression.getUsedVariables()).isEmpty();
  }

  @Test
  void testGetUsedVariablesCaseSensitivity() throws ParseException {
    ExpressionX expression = ExpressionX.of("a+B*b-A/PI*(1/2)*pi+e-E+a");
    assertThat(expression.getUsedVariables()).containsExactlyInAnyOrder("a", "b");
  }

  @Test
  void testGetUndefinedVariables() throws ParseException {
    ExpressionX expression1 = ExpressionX.of("a+A+b+B+c+C+E+e+PI+x");
    ExpressionX expression = expression1;
    VariableResolverX variableResolver = VariableResolverX.builder()
      .with("x", Value.of(1))
      .build();
    assertThat(expression.getUndefinedVariables(variableResolver)).containsExactlyInAnyOrder("a", "b", "c");
  }
}
