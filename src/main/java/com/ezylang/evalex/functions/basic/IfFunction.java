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
package com.ezylang.evalex.functions.basic;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.parser.Token;

/**
 * Conditional evaluation function. If parameter <code>condition</code> is <code>true</code>, the
 * <code>resultIfTrue</code> value is returned, else the <code>resultIfFalse</code> value. <code>
 * resultIfTrue</code> and <code>resultIfFalse</code> are only evaluated (lazily evaluated),
 * <b>after</b> the condition was evaluated.
 */
public class IfFunction extends AbstractFunction.Triple<Value.BooleanValue, Value.ExpressionValue, Value.ExpressionValue> {

  public  IfFunction() {
    super(
      FunctionParameterDefinition.of(Value.BooleanValue.class, "condition"),
      FunctionParameterDefinition.lazyWith(Value.ExpressionValue.class, "resultIfTrue"),
      FunctionParameterDefinition.lazyWith(Value.ExpressionValue.class, "resultIfFalse")
    );
  }

  @Override public Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, Value.BooleanValue condition,
    Value.ExpressionValue ifTrue, Value.ExpressionValue ifFalse) throws EvaluationException {
    if (condition.wrapped()) {
      return expression.evaluateSubtree(variableResolver, ifTrue.wrapped());
    } else {
      return expression.evaluateSubtree(variableResolver, ifFalse.wrapped());
    }
  }
}
