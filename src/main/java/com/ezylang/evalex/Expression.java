package com.ezylang.evalex;

import com.ezylang.evalex.config.Configuration;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.Function;
import com.ezylang.evalex.operators.InfixOperator;
import com.ezylang.evalex.operators.PostfixOperator;
import com.ezylang.evalex.operators.PrefixOperator;
import com.ezylang.evalex.parser.*;
import de.flapdoodle.types.Either;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@org.immutables.value.Value.Immutable
public abstract class Expression {
	public abstract Configuration getConfiguration();

	public abstract String expressionString();

//	@org.immutables.value.Value.Default
//	public Map<String, Value<?>> getConstants() {
//		TreeMap<String, Value<?>> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
//		map.putAll(getConfiguration().getDefaultConstants());
//		return Collections.unmodifiableMap(map);
//	}
	@org.immutables.value.Value.Default
	public VariableResolver getConstantResolver() {
		return getConfiguration().getConstantResolver();
	}

	@org.immutables.value.Value.Auxiliary
	public Expression withConstant(String variable, Value<?> value) {
		if (getConstantResolver().getData(variable)==null || getConfiguration().isAllowOverwriteConstants()) {
			return ImmutableExpression.builder().from(this)
//					.putAllConstants(getConstants())
					.constantResolver(VariableResolver.builder()
							.with(variable, value)
							.build().andThen(getConstantResolver()))
//					.putConstants(variable, value)
					.build();
		} else {
			throw new UnsupportedOperationException(
					String.format("Can't set value for constant '%s'", variable));
		}
	}

	/**
	 * Returns the root ode of the parsed abstract syntax tree.
	 *
	 * @return The abstract syntax tree root node.
	 * @throws ParseException If there were problems while parsing the expression.
	 */
	@org.immutables.value.Value.Derived
	public Either<ASTNode, ParseException> getAbstractSyntaxTree() {
		Tokenizer tokenizer = new Tokenizer(expressionString(), getConfiguration());
		try {
			ShuntingYardConverter converter =
					new ShuntingYardConverter(expressionString(), tokenizer.parse(), getConfiguration());
			return Either.left(converter.toAbstractSyntaxTree());
		} catch (ParseException px) {
			return Either.right(px);
		}
	}

	@Deprecated
	@org.immutables.value.Value.Auxiliary
	public void validate() throws ParseException {
		if (!getAbstractSyntaxTree().isLeft()) throw getAbstractSyntaxTree().right();
	}

	public List<ASTNode> getAllASTNodes() throws ParseException {
		Either<ASTNode, ParseException> tree = getAbstractSyntaxTree();
		if (tree.isLeft())
			return getAllASTNodesForNode(tree.left());
			else throw tree.right();
	}

	private List<ASTNode> getAllASTNodesForNode(ASTNode node) {
		List<ASTNode> nodes = new ArrayList<>();
		nodes.add(node);
		for (ASTNode child : node.getParameters()) {
			nodes.addAll(getAllASTNodesForNode(child));
		}
		return nodes;
	}

	public Set<String> getUsedVariables() throws ParseException {
		Set<String> variables = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

		for (ASTNode node : getAllASTNodes()) {
			if (node.getToken().getType() == TokenType.VARIABLE_OR_CONSTANT
				&& !getConstantResolver().has(node.getToken().getValue())) {
				variables.add(node.getToken().getValue());
			}
		}

		return variables;
	}

	public Set<String> getUndefinedVariables(VariableResolver variableResolver) throws ParseException {
		Set<String> variables = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		for (String variable : getUsedVariables()) {
			if (variableResolver.getData(variable) == null) {
				variables.add(variable);
			}
		}
		return variables;
	}



	@org.immutables.value.Value.Auxiliary
	public ASTNode createExpressionNode(String expression) throws ParseException {
		Tokenizer tokenizer = new Tokenizer(expression, getConfiguration());
		ShuntingYardConverter converter =
			new ShuntingYardConverter(expression, tokenizer.parse(), getConfiguration());
		return converter.toAbstractSyntaxTree();
	}


	/**
	 * Evaluates the expression by parsing it (if not done before) and the evaluating it.
	 *
	 * @return The evaluation result value.
	 * @throws EvaluationException If there were problems while evaluating the expression.
	 * @throws ParseException If there were problems while parsing the expression.
	 */
	public Value<?> evaluate(VariableResolver variableResolver) throws EvaluationException, ParseException {
		Either<ASTNode, ParseException> ast = getAbstractSyntaxTree();
		if (ast.isLeft()) {
			return evaluateSubtree(variableResolver, ast.left());
		} else throw ast.right();
	}

	/**
	 * Evaluates only a subtree of the abstract syntax tree.
	 *
	 * @param startNode The {@link ASTNode} to start evaluation from.
	 * @return The evaluation result value.
	 * @throws EvaluationException If there were problems while evaluating the expression.
	 */
	public Value<?> evaluateSubtree(VariableResolver variableResolver, ASTNode startNode) throws EvaluationException {
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

	private Value<?> getVariableOrConstant(VariableResolver variableResolver, Token token) throws EvaluationException {
		Value<?> result = getConstantResolver().getData(token.getValue());
		if (result == null) {
			result = variableResolver.getData(token.getValue());
		}
		if (result == null) {
			throw new EvaluationException(
				token, String.format("Variable or constant value for '%s' not found", token.getValue()));
		}
		return result;
	}

	private Value<?> evaluateFunction(VariableResolver variableResolver, ASTNode startNode, Token token)
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

	private Value<?> evaluateArrayIndex(VariableResolver variableResolver, ASTNode startNode) throws EvaluationException {
		Value<?> array = evaluateSubtree(variableResolver, startNode.getParameters().get(0));
		Value<?> index = evaluateSubtree(variableResolver, startNode.getParameters().get(1));

		if (array instanceof Value.ArrayValue && index instanceof Value.NumberValue) {
			return ((Value.ArrayValue) array).wrapped().get(((Value.NumberValue) index).wrapped().intValue());
		} else {
			throw EvaluationException.ofUnsupportedDataTypeInOperation(startNode.getToken());
		}
	}

	private Value<?> evaluateStructureSeparator(VariableResolver variableResolver, ASTNode startNode) throws EvaluationException {
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

	public static Expression of(String expressionString) {
		return ImmutableExpression.builder()
			.expressionString(expressionString)
			.configuration(Configuration.defaultConfiguration())
			.build();
	}

	public static Expression of(String expressionString, Configuration configuration) {
		return ImmutableExpression.builder()
				.expressionString(expressionString)
				.configuration(configuration)
				.build();
	}
}
