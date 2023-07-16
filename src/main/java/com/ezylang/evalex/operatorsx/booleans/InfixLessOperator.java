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

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.operators.Precedence;
import com.ezylang.evalex.parser.Token;

/** Less of two values. */
public class InfixLessOperator extends AbstractInfixComparableOperator {

  public InfixLessOperator() {
    super(Precedence.OPERATOR_PRECEDENCE_COMPARISON);
  }

  @Override protected <T extends Comparable<T>, V extends Value.ComparableValue<T>> Value<?> evaluateComparable(Expression expression, Token operatorToken,
    V leftOperand, V rightOperand) {
    return Value.of(leftOperand.compareTo(rightOperand) < 0);
  }
}
