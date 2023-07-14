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

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.FunctionIfc;
import com.ezylang.evalex.parser.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.*;

/**
 * Main class that allow creating, parsing, passing parameters and evaluating an expression string.
 *
 * @see <a href="https://github.com/ezylang/EvalEx">EvalEx Homepage</a>
 */
public class Expression {
  @Getter private final ExpressionConfiguration configuration;

  @Getter private final String expressionString;

  @Getter
  private final Map<String, EvaluationValue> constants =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  private ASTNode abstractSyntaxTree;

  /**
   * Creates a new expression with the default configuration. The expression is not parsed until it
   * is first evaluated or validated.
   *
   * @param expressionString A string holding an expression.
   */
  public Expression(String expressionString) {
    this(expressionString, ExpressionConfiguration.defaultConfiguration());
  }

  /**
   * Creates a new expression with a custom configuration. The expression is not parsed until it is
   * first evaluated or validated.
   *
   * @param expressionString A string holding an expression.
   */
  public Expression(String expressionString, ExpressionConfiguration configuration) {
    this.expressionString = expressionString;
    this.configuration = configuration;
    this.constants.putAll(configuration.getDefaultConstants());
  }

  /**
   * Evaluates the expression by parsing it (if not done before) and the evaluating it.
   *
   * @return The evaluation result value.
   * @throws EvaluationException If there were problems while evaluating the expression.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public EvaluationValue evaluate(VariableResolver variableResolver) throws EvaluationException, ParseException {
    return evaluateSubtree(variableResolver, getAbstractSyntaxTree());
  }

  /**
   * Evaluates only a subtree of the abstract syntax tree.
   *
   * @param startNode The {@link ASTNode} to start evaluation from.
   * @return The evaluation result value.
   * @throws EvaluationException If there were problems while evaluating the expression.
   */
  public EvaluationValue evaluateSubtree(VariableResolver variableResolver, ASTNode startNode) throws EvaluationException {
    Token token = startNode.getToken();
    EvaluationValue result;
    switch (token.getType()) {
      case NUMBER_LITERAL:
        result = EvaluationValue.numberOfString(token.getValue(), configuration.getMathContext());
        break;
      case STRING_LITERAL:
        result = new EvaluationValue(token.getValue());
        break;
      case VARIABLE_OR_CONSTANT:
        result = getVariableOrConstant(variableResolver, token);
        if (result.isExpressionNode()) {
          result = evaluateSubtree(variableResolver, result.getExpressionNode());
        }
        break;
      case PREFIX_OPERATOR:
      case POSTFIX_OPERATOR:
        result =
            token
                .getOperatorDefinition()
                .evaluate(this, token, evaluateSubtree(variableResolver, startNode.getParameters().get(0)));
        break;
      case INFIX_OPERATOR:
        result =
            token
                .getOperatorDefinition()
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

    return result.isNumberValue() ? roundAndStripZerosIfNeeded(result) : result;
  }

  private EvaluationValue getVariableOrConstant(VariableResolver variableResolver, Token token) throws EvaluationException {
    EvaluationValue result = constants.get(token.getValue());
    if (result == null) {
      result = variableResolver.getData(token.getValue());
    }
    if (result == null) {
      throw new EvaluationException(
          token, String.format("Variable or constant value for '%s' not found", token.getValue()));
    }
    return result;
  }

  private EvaluationValue evaluateFunction(VariableResolver variableResolver, ASTNode startNode, Token token)
      throws EvaluationException {
    List<EvaluationValue> parameterResults = new ArrayList<>();
    for (int i = 0; i < startNode.getParameters().size(); i++) {
      if (token.getFunctionDefinition().isParameterLazy(i)) {
        parameterResults.add(new EvaluationValue(startNode.getParameters().get(i)));
      } else {
        parameterResults.add(evaluateSubtree(variableResolver, startNode.getParameters().get(i)));
      }
    }

    EvaluationValue[] parameters = parameterResults.toArray(new EvaluationValue[0]);

    FunctionIfc function = token.getFunctionDefinition();

    function.validatePreEvaluation(token, parameters);

    return function.evaluate(variableResolver,this, token, parameters);
  }

  private EvaluationValue evaluateArrayIndex(VariableResolver variableResolver, ASTNode startNode) throws EvaluationException {
    EvaluationValue array = evaluateSubtree(variableResolver, startNode.getParameters().get(0));
    EvaluationValue index = evaluateSubtree(variableResolver, startNode.getParameters().get(1));

    if (array.isArrayValue() && index.isNumberValue()) {
      return array.getArrayValue().get(index.getNumberValue().intValue());
    } else {
      throw EvaluationException.ofUnsupportedDataTypeInOperation(startNode.getToken());
    }
  }

  private EvaluationValue evaluateStructureSeparator(VariableResolver variableResolver, ASTNode startNode) throws EvaluationException {
    EvaluationValue structure = evaluateSubtree(variableResolver, startNode.getParameters().get(0));
    Token nameToken = startNode.getParameters().get(1).getToken();
    String name = nameToken.getValue();

    if (structure.isStructureValue()) {
      if (!structure.getStructureValue().containsKey(name)) {
        throw new EvaluationException(
            nameToken, String.format("Field '%s' not found in structure", name));
      }
      return structure.getStructureValue().get(name);
    } else {
      throw EvaluationException.ofUnsupportedDataTypeInOperation(startNode.getToken());
    }
  }

  /**
   * Rounds the given value, if the decimal places are configured. Also strips trailing decimal
   * zeros, if configured.
   *
   * @param value The input value.
   * @return The rounded value, or the input value if rounding is not configured or possible.
   */
  private EvaluationValue roundAndStripZerosIfNeeded(EvaluationValue value) {
    BigDecimal bigDecimal = value.getNumberValue();
    if (configuration.getDecimalPlacesRounding()
        != ExpressionConfiguration.DECIMAL_PLACES_ROUNDING_UNLIMITED) {
      bigDecimal =
          bigDecimal.setScale(
              configuration.getDecimalPlacesRounding(),
              configuration.getMathContext().getRoundingMode());
    }
    if (configuration.isStripTrailingZeros()) {
      bigDecimal = bigDecimal.stripTrailingZeros();
    }
    return new EvaluationValue(bigDecimal);
  }

  /**
   * Returns the root ode of the parsed abstract syntax tree.
   *
   * @return The abstract syntax tree root node.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public ASTNode getAbstractSyntaxTree() throws ParseException {
    if (abstractSyntaxTree == null) {
      Tokenizer tokenizer = new Tokenizer(expressionString, configuration);
      ShuntingYardConverter converter =
          new ShuntingYardConverter(expressionString, tokenizer.parse(), configuration);
      abstractSyntaxTree = converter.toAbstractSyntaxTree();
    }

    return abstractSyntaxTree;
  }

  /**
   * Validates the expression by parsing it and throwing an exception, if the parser fails.
   *
   * @throws ParseException If there were problems while parsing the expression.
   */
  public void validate() throws ParseException {
    getAbstractSyntaxTree();
  }

  public Expression withConstant(String variable, Object value) {
    if (constants.containsKey(variable)) {
      if (configuration.isAllowOverwriteConstants()) {
        constants.put(variable, new EvaluationValue(value));
      } else {
        throw new UnsupportedOperationException(
          String.format("Can't set value for constant '%s'", variable));
      }
    }
    return this;
  }

  public Expression withConstants(Map<String, ?> values) {
    for (Map.Entry<String, ?> entry : values.entrySet()) {
      withConstant(entry.getKey(), entry.getValue());
    }
    return this;
  }

    /**
		 * Create an AST representation for an expression string. The node can then be used as a
		 * sub-expression. Subexpressions are not cached.
		 *
		 * @param expression The expression string.
		 * @return The root node of the expression AST representation.
		 * @throws ParseException On any parsing error.
		 */
  public ASTNode createExpressionNode(String expression) throws ParseException {
    Tokenizer tokenizer = new Tokenizer(expression, configuration);
    ShuntingYardConverter converter =
        new ShuntingYardConverter(expression, tokenizer.parse(), configuration);
    return converter.toAbstractSyntaxTree();
  }

  /**
   * Converts a double value to an {@link EvaluationValue} by considering the configured {@link
   * java.math.MathContext}.
   *
   * @param value The double value to covert.
   * @return An {@link EvaluationValue} of type {@link EvaluationValue.DataType#NUMBER}.
   */
  public EvaluationValue convertDoubleValue(double value) {
    return new EvaluationValue(value, configuration.getMathContext());
  }

  /**
   * Returns the list of all nodes of the abstract syntax tree.
   *
   * @return The list of all nodes in the parsed expression.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public List<ASTNode> getAllASTNodes() throws ParseException {
    return getAllASTNodesForNode(getAbstractSyntaxTree());
  }

  private List<ASTNode> getAllASTNodesForNode(ASTNode node) {
    List<ASTNode> nodes = new ArrayList<>();
    nodes.add(node);
    for (ASTNode child : node.getParameters()) {
      nodes.addAll(getAllASTNodesForNode(child));
    }
    return nodes;
  }

  /**
   * Returns all variables that are used i the expression, excluding the constants like e.g. <code>
   * PI</code> or <code>TRUE</code> and <code>FALSE</code>.
   *
   * @return All used variables excluding constants.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public Set<String> getUsedVariables() throws ParseException {
    Set<String> variables = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    for (ASTNode node : getAllASTNodes()) {
      if (node.getToken().getType() == Token.TokenType.VARIABLE_OR_CONSTANT
          && !constants.containsKey(node.getToken().getValue())) {
        variables.add(node.getToken().getValue());
      }
    }

    return variables;
  }

  /**
   * Returns all variables that are used in the expression, but have no value assigned.
   *
   * @return All variables that have no value assigned.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public Set<String> getUndefinedVariables(VariableResolver variableResolver) throws ParseException {
    Set<String> variables = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    for (String variable : getUsedVariables()) {
      if (variableResolver.getData(variable) == null) {
        variables.add(variable);
      }
    }
    return variables;
  }
}
