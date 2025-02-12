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
package com.ezylang.evalex.parser;

import com.ezylang.evalex.config.Configuration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenizerImplicitMultiplicationTest extends BaseParserTest {

  @Test
  void testImplicitBraces() throws ParseException {
    assertAllTokensParsedCorrectly(
        "(a+b)(a-b)",
      Token.of(1, "(", TokenType.BRACE_OPEN),
      Token.of(2, "a", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(3, "+", TokenType.INFIX_OPERATOR),
      Token.of(4, "b", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(5, ")", TokenType.BRACE_CLOSE),
      Token.of(6, "*", TokenType.INFIX_OPERATOR),
      Token.of(6, "(", TokenType.BRACE_OPEN),
      Token.of(7, "a", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(8, "-", TokenType.INFIX_OPERATOR),
      Token.of(9, "b", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(10, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testImplicitNumberBraces() throws ParseException {
    assertAllTokensParsedCorrectly(
        "2(x)",
      Token.of(1, "2", TokenType.NUMBER_LITERAL),
      Token.of(2, "*", TokenType.INFIX_OPERATOR),
      Token.of(2, "(", TokenType.BRACE_OPEN),
      Token.of(3, "x", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(4, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testImplicitNumberNoBraces() throws ParseException {
    assertAllTokensParsedCorrectly(
        "2x",
      Token.of(1, "2", TokenType.NUMBER_LITERAL),
      Token.of(2, "*", TokenType.INFIX_OPERATOR),
      Token.of(2, "x", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testImplicitNumberVariable() throws ParseException {
    assertAllTokensParsedCorrectly(
        "2x",
      Token.of(1, "2", TokenType.NUMBER_LITERAL),
      Token.of(2, "*", TokenType.INFIX_OPERATOR),
      Token.of(2, "x", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testImplicitMultiplicationNotAllowed() {
    Configuration config =
        Configuration.builder().isImplicitMultiplicationAllowed(false).build();

    assertThatThrownBy(() -> new Tokenizer("2(x+y)", config).parse())
        .isEqualTo(new ParseException(2, 2, "(", "Missing operator"));
  }
}
