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
package com.ezylang.evalex.operatorsx.booleans;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.operators.Precedence;
import com.ezylang.evalex.operatorsx.AbstractPrefixOperator;
import com.ezylang.evalex.parser.Token;

/** Boolean negation of value. */
public class PrefixNotOperator extends AbstractPrefixOperator {

  public PrefixNotOperator() {
    super(Precedence.OPERATOR_PRECEDENCE_UNARY);
  }
  
  @Override
  public Value<?> evaluate(
      Expression expression, Token operatorToken, Value<?> operand) throws EvaluationException {
    return Value.of(!requireValueType(operatorToken, operand, Value.BooleanValue.class).wrapped());
  }
}
