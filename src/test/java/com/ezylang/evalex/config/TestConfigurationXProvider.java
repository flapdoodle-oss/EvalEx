package com.ezylang.evalex.config;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.ExpressionX;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.functionsx.AbstractFunction;
import com.ezylang.evalex.functionsx.FunctionParameterDefinition;
import com.ezylang.evalex.operators.Precedence;
import com.ezylang.evalex.operatorsx.AbstractPostfixOperator;
import com.ezylang.evalex.operatorsx.AbstractPrefixOperator;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.ExpressionX;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.functionsx.FunctionParameterDefinition;
import com.ezylang.evalex.functions.SingleArgumentFunction;
import com.ezylang.evalex.functionsx.AbstractFunction;
import com.ezylang.evalex.operators.Precedence;
import com.ezylang.evalex.operatorsx.*;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class TestConfigurationXProvider {

	public static final Configuration StandardConfigurationWithAdditionalTestOperators =
		Configuration.defaultConfiguration()
			.withAdditionalOperators(
				Map.entry("++", new PrefixPlusPlusOperator()),
				Map.entry("++", new PostfixPlusPlusOperator()),
				Map.entry("?", new PostfixQuestionOperator()))
			.withAdditionalFunctions(Map.entry("TEST", new DummyFunction()));

	public static class DummyFunction extends AbstractFunction.SingleVararg<Value.StringValue> {
		public DummyFunction() {
			super(FunctionParameterDefinition.varArgWith(Value.StringValue.class, "input"));
		}

		@Override public Value<?> evaluateVarArg(VariableResolverX variableResolver, ExpressionX expression, com.ezylang.evalex.parserx.Token functionToken,
			List<Value.StringValue> parameterValues) {
			// dummy implementation
			return Value.of("OK");
		}
	}

	public static class PrefixPlusPlusOperator extends AbstractPrefixOperator {

		public PrefixPlusPlusOperator() {
			super(Precedence.OPERATOR_PRECEDENCE_UNARY, false);
		}

		@Override public Value<?> evaluate(ExpressionX expression, com.ezylang.evalex.parserx.Token operatorToken, Value<?> operand) throws EvaluationException {
			// dummy implementation
			return Value.of(numberValue(operatorToken, operand).wrapped().add(BigDecimal.ONE));
		}
	}

	public static class PostfixPlusPlusOperator extends AbstractPostfixOperator {

		@Override public Value<?> evaluate(ExpressionX expression, com.ezylang.evalex.parserx.Token operatorToken, Value<?> operand) throws EvaluationException {
			return Value.of(numberValue(operatorToken, operand).wrapped().add(BigDecimal.ONE));
		}
	}

	public static class PostfixQuestionOperator extends AbstractPostfixOperator {

		public PostfixQuestionOperator() {
			super(Precedence.OPERATOR_PRECEDENCE_UNARY, false);
		}

		@Override public Value<?> evaluate(ExpressionX expression, com.ezylang.evalex.parserx.Token operatorToken, Value<?> operand) throws EvaluationException {
			return Value.of("?");
		}
	}
}
