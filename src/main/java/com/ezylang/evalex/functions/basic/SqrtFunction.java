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

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.functions.validations.NonNegativeNumberValidator;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/** Square root function, uses the standard {@link BigDecimal#sqrt(MathContext)} implementation. */
public class SqrtFunction extends AbstractFunction.Single<Value.NumberValue> {

  public SqrtFunction() {
    super(FunctionParameterDefinition.of(Value.NumberValue.class,"value")
      .withValidators(new NonNegativeNumberValidator()));
  }

  @Override public Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, Value.NumberValue parameterValue) {
    /*
     * From The Java Programmers Guide To numerical Computing
     * (Ronald Mak, 2003)
     */
    BigDecimal x = parameterValue.wrapped();
    MathContext mathContext = expression.getConfiguration().getMathContext();

    if (x.compareTo(BigDecimal.ZERO) == 0) {
      return Value.of(BigDecimal.ZERO);
    }
    BigInteger n = x.movePointRight(mathContext.getPrecision() << 1).toBigInteger();

    int bits = (n.bitLength() + 1) >> 1;
    BigInteger ix = n.shiftRight(bits);
    BigInteger ixPrev;
    BigInteger test;
    do {
      ixPrev = ix;
      ix = ix.add(n.divide(ix)).shiftRight(1);
      // Give other threads a chance to work
      Thread.yield();
      test = ix.subtract(ixPrev).abs();
    } while (test.compareTo(BigInteger.ZERO) != 0 && test.compareTo(BigInteger.ONE) != 0);

    return Value.of(new BigDecimal(ix, mathContext.getPrecision()));
  }
}
