package com.ezylang.evalex;

import com.ezylang.evalex.config.Configuration;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolverX;
import com.ezylang.evalex.functionsx.Function;
import com.ezylang.evalex.operatorsx.InfixOperator;
import com.ezylang.evalex.operatorsx.PostfixOperator;
import com.ezylang.evalex.operatorsx.PrefixOperator;
import com.ezylang.evalex.parserx.ASTNode;
import com.ezylang.evalex.parserx.Token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.*;

public abstract class ExpressionX {
	public abstract Configuration getConfiguration();

	public abstract String expressionString();

	@org.immutables.value.Value.Derived
	public Map<String, Value<?>> getConstants() {
		TreeMap<String, Value<?>> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		map.putAll(getConfiguration().getDefaultConstants());
		return Collections.unmodifiableMap(map);
	}

	/**
	 * Evaluates only a subtree of the abstract syntax tree.
	 *
	 * @param startNode The {@link ASTNode} to start evaluation from.
	 * @return The evaluation result value.
	 * @throws EvaluationException If there were problems while evaluating the expression.
	 */
	public Value<?> evaluateSubtree(VariableResolverX variableResolver, ASTNode startNode) throws EvaluationException {
		Token token = startNode.getToken();
		Value<?> result;
		switch (token.getType()) {
			case NUMBER_LITERAL:
				result = numberOfString(token.getValue(), getConfiguration().getMathContext());
				break;
			case STRING_LITERAL:
				result = Value.of(token.getValue());
				break;
			case VARIABLE_OR_CONSTANT:
				result = getVariableOrConstant(variableResolver, token);
				if (result instanceof Value.ExpressionValue) {
					result = evaluateSubtree(variableResolver, ((Value.ExpressionValue) result).wrapped());
				}
				break;
			case PREFIX_OPERATOR:
				result =
					token
						.operatorDefinition(PrefixOperator.class)
						.evaluate(this, token, evaluateSubtree(variableResolver, startNode.getParameters().get(0)));
				break;
			case POSTFIX_OPERATOR:
				result =
					token
						.operatorDefinition(PostfixOperator.class)
						.evaluate(this, token, evaluateSubtree(variableResolver, startNode.getParameters().get(0)));
				break;
			case INFIX_OPERATOR:
				result =
					token
						.operatorDefinition(InfixOperator.class)
						.evaluate(
							this,
							token,
							evaluateSubtree(variableResolver, startNode.getParameters().get(0)),
							evaluateSubtree(variableResolver, startNode.getParameters().get(1)));
				break;
			case ARRAY_INDEX:
				result = evaluateArrayIndex(variableResolver, startNode);
				break;
			case STRUCTURE_SEPARATOR:
				result = evaluateStructureSeparator(variableResolver, startNode);
				break;
			case FUNCTION:
				result = evaluateFunction(variableResolver, startNode, token);
				break;
			default:
				throw new EvaluationException(token, "Unexpected evaluation token: " + token);
		}

//		return result.isNumberValue() ? roundAndStripZerosIfNeeded(result) : result;
		return result;
	}

	private Value<?> getVariableOrConstant(VariableResolverX variableResolver, Token token) throws EvaluationException {
		Value<?> result = getConstants().get(token.getValue());
		if (result == null) {
			result = variableResolver.getData(token.getValue());
		}
		if (result == null) {
			throw new EvaluationException(
				token, String.format("Variable or constant value for '%s' not found", token.getValue()));
		}
		return result;
	}

	private Value<?> evaluateFunction(VariableResolverX variableResolver, ASTNode startNode, Token token)
		throws EvaluationException {
		List<Value<?>> parameterResults = new ArrayList<>();
		for (int i = 0; i < startNode.getParameters().size(); i++) {
			if (token.getFunctionDefinition().parameterIsLazy(i)) {
				parameterResults.add(Value.of(startNode.getParameters().get(i)));
			} else {
				parameterResults.add(evaluateSubtree(variableResolver, startNode.getParameters().get(i)));
			}
		}

		Function function = token.getFunctionDefinition();

//    function.validatePreEvaluation(token, parameterResults);

		return function.evaluateUnvalidated(variableResolver,this, token, parameterResults);
	}

	private Value<?> evaluateArrayIndex(VariableResolverX variableResolver, ASTNode startNode) throws EvaluationException {
		Value<?> array = evaluateSubtree(variableResolver, startNode.getParameters().get(0));
		Value<?> index = evaluateSubtree(variableResolver, startNode.getParameters().get(1));

		if (array instanceof Value.ArrayValue && index instanceof Value.NumberValue) {
			return ((Value.ArrayValue) array).wrapped().get(((Value.NumberValue) index).wrapped().intValue());
		} else {
			throw EvaluationException.ofUnsupportedDataTypeInOperation(startNode.getToken());
		}
	}

	private Value<?> evaluateStructureSeparator(VariableResolverX variableResolver, ASTNode startNode) throws EvaluationException {
		Value<?> structure = evaluateSubtree(variableResolver, startNode.getParameters().get(0));
		Token nameToken = startNode.getParameters().get(1).getToken();
		String name = nameToken.getValue();

		if (structure instanceof Value.StructureValue) {
			Value.StructureValue structure1 = (Value.StructureValue) structure;
			if (!structure1.wrapped().containsKey(name)) {
				throw new EvaluationException(
					nameToken, String.format("Field '%s' not found in structure", name));
			}
			return structure1.wrapped().get(name);
		} else {
			throw EvaluationException.ofUnsupportedDataTypeInOperation(startNode.getToken());
		}
	}




	/**
	 * moved from somewhere
	 */

	private static Value.NumberValue numberOfString(String value, MathContext mathContext) {
		if (value.startsWith("0x") || value.startsWith("0X")) {
			BigInteger hexToInteger = new BigInteger(value.substring(2), 16);
			return Value.of(new BigDecimal(hexToInteger, mathContext));
		} else {
			return Value.of(new BigDecimal(value, mathContext));
		}
	}

}
